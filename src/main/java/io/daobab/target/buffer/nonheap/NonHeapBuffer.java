package io.daobab.target.buffer.nonheap;

import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;
import io.daobab.query.base.Query;
import io.daobab.result.IndexedFilterResult;
import io.daobab.result.predicate.AlwaysTrue;
import io.daobab.result.predicate.GeneralBitFieldWhereAnd;
import io.daobab.result.predicate.GeneralBitFieldWhereOr;
import io.daobab.statement.condition.Operator;
import io.daobab.statement.where.base.Where;
import io.daobab.statement.where.base.WhereBase;
import io.daobab.target.BaseTarget;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.nonheap.access.field.BitField;
import io.daobab.target.buffer.nonheap.index.BitBufferIndexBase;
import io.daobab.target.buffer.query.*;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.buffer.util.MemoryUsageMonitor;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.BasicAccessProtector;
import io.daobab.target.protection.OperationType;
import io.daobab.transaction.Propagation;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static io.daobab.statement.where.base.WhereBase.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class NonHeapBuffer<E> extends BaseTarget implements BufferQueryTarget, MemoryUsageMonitor {

    public int pageMaxCapacityBytes = 2;
    public int totalEntitySpace = 0;
    protected int pageMaxCapacity;
    protected AtomicInteger totalBufferActiveElements = new AtomicInteger(0);
    //in bytes, all pages
    protected int totalBufferSize;
    protected List<ByteBuffer> pages;
    protected Map<String, Integer> columnsOrder;
    protected Integer[] columnsPositionsQueue;
    protected BitField[] bitFields;
    protected List<TableColumn> columns;
    protected BitBufferIndexBase[] indexRepository;
    protected boolean isIndexRepositoryEmpty = true;
    protected List<Integer> locations = new ArrayList<>();
    protected LinkedList<Integer> removed = new LinkedList<>();
    protected Map<Integer, HashMap<String, Object>> additionalParameters = new HashMap<>();
    private AccessProtector accessProtector = new BasicAccessProtector(this);

    protected final BitFieldRegistry bitFieldRegistry;

    protected NonHeapBuffer(BitFieldRegistry bitFieldRegistry) {
        this.bitFieldRegistry = bitFieldRegistry;
    }

    protected void adjustForCapacity(int capacity) {
        int pageCapacityStarter = 1;
        int pageCapacity = pageCapacityStarter;
        int byteShifter = 0;
        while (pageCapacity < capacity) {
            pageCapacity = pageCapacityStarter << byteShifter + 1;
            byteShifter++;
        }

        pageMaxCapacity = pageCapacity;
        pageMaxCapacityBytes = byteShifter;
    }

    private boolean pagesHasToBeMerged() {
        return (pages.size() > pageMaxCapacityBytes);
    }

    @Override
    public long getBufferMemoryUsage() {
        long usage = 0;
        for (ByteBuffer page : pages) {
            usage = usage + page.limit();
        }
        return usage;
    }

    protected void mergeIfNecessary() {
        if (pagesHasToBeMerged()) {
            mergePages();
        }
    }

    private void mergePages() {
        int newPageMaxCapacityBytes = pageMaxCapacityBytes + 1;
        int newPageCapacity = 1 << newPageMaxCapacityBytes;

        List<ByteBuffer> newPages = new LinkedList<>();
        int counter = 0;

        while (counter < pages.size()) {
            //creating new buffer page which is 2x bigger
            ByteBuffer bb = ByteBuffer.allocateDirect(totalEntitySpace << newPageMaxCapacityBytes);
            byte[] ret = new byte[pages.get(counter).limit()];
            ByteBuffer pageBuffer = pages.get(counter);
            pageBuffer.position(0);
            pageBuffer.slice().get(ret);
            bb.position(0);
            bb.put(ret);

            counter++;
            if (counter < pages.size()) {
                //second page
                byte[] ret2 = new byte[pages.get(counter).limit()];
                ByteBuffer pageBuffer2 = pages.get(counter);
                pageBuffer2.position(0);
                pageBuffer2.slice().get(ret2);
                bb.put(ret2);
                counter++;
            }
            newPages.add(bb);
        }
        List<ByteBuffer> oldPages = pages;
        pages = newPages;
        oldPages.clear(); //do not keep references
        pageMaxCapacity = newPageCapacity;
        pageMaxCapacityBytes = newPageMaxCapacityBytes;
    }

    protected Integer getNextFreeLocation() {
        if (removed.isEmpty()) {
            int thisLocation = totalBufferActiveElements.getAndIncrement();
            locations.add(thisLocation);
            return thisLocation;
        } else {
            int rm = removed.removeFirst();
            locations.add(rm);
            totalBufferActiveElements.decrementAndGet();
            return rm;
        }
    }

    public int size() {
        return totalBufferActiveElements.get();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Plate getPlate(int i, Collection<TableColumn> chosenColumns) {
        Plate rv = new Plate(chosenColumns);

        int entityLocation = locations.get(i);
        int page = entityLocation >> pageMaxCapacityBytes;
        int rowAtPage = entityLocation - (page << pageMaxCapacityBytes);
        int posEntity = rowAtPage * totalEntitySpace;
        int cnt = 0;
        for (TableColumn col : chosenColumns) {
            Integer posCol = columnsPositionsQueue[cnt];
            if (posCol == null) {
                HashMap<String, Object> additionalValues = additionalParameters.get(entityLocation);
                if (additionalValues != null) {
                    rv.setValue(col, additionalValues.get(col.toString()));
                }
                cnt++;
                continue;
            }
            pages.get(page).position(0);
            rv.setValue(col, bitFields[cnt].readValue(pages.get(page), posEntity + posCol));
            cnt++;
        }
        return rv;
    }

    public Object getValue(int entityPosition, int columnPositionIntoEntity, int colOrder) {
        int entityLocation = locations.get(entityPosition);
        int page = entityLocation >> pageMaxCapacityBytes;
        int rowAtPage = entityLocation - (page << pageMaxCapacityBytes);
        int fieldPosition = (rowAtPage * totalEntitySpace) + colOrder;
        return bitFields[columnPositionIntoEntity].readValue(pages.get(page), fieldPosition);
    }

    @SuppressWarnings("unchecked")
    public Integer getColumnIntoEntityPosition(Column<?, ?, ?> column) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getColumn().equalsField(column)) {
                return i;
            }
        }
        throw new DaobabException("Column '%s' is not related to the buffer.", column.getColumnName());
    }

    public Integer getBufferPositionOfColumn(Column<?, ?, ?> column) {
        Integer columnIntoEntityPosition = getColumnIntoEntityPosition(column);
        return columnsPositionsQueue[columnIntoEntityPosition];
    }

    public Object getValueForColumnPosition(int entityPointer, int columnIntoEntityPosition, int columnBufferRowPosition) {
        int entityLocation = locations.get(entityPointer);
        int page = entityLocation >> pageMaxCapacityBytes;
        int entityAtPagePointer = entityLocation - (page << pageMaxCapacityBytes);
        return bitFields[columnIntoEntityPosition].readValue(pages.get(page), (entityAtPagePointer * totalEntitySpace) + columnBufferRowPosition);
    }

    @SuppressWarnings("unchecked")
    public <F> List<F> finalFilterField(BufferQueryField<?, F> query) {
        int columnIntoEntityPosition = getColumnIntoEntityPosition(query.getFields().get(0).getColumn());
        int columnBufferRowPosition = getBufferPositionOfColumn(query.getFields().get(0).getColumn());

        List<Integer> ids = finalFilter(filterByIndexes(null, query.getWhereWrapper()), query);
        List<F> rv = new ArrayList<>(ids.size());
        for (int i = 0; i < ids.size(); i++) {
            rv.add(i, (F) getValueForColumnPosition(ids.get(i), columnIntoEntityPosition, columnBufferRowPosition));
        }
        return rv;
    }

    @Override
    public Plate readPlate(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        return readPlateList(query).get(0);
    }

    @Override
    public Plates readPlateList(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        List<Integer> ids = finalFilter(filterByIndexes(null, query.getWhereWrapper()), query);

        List<TableColumn> col = query.getFields();
        List<TableColumn> col2 = new ArrayList<>(col.size());
        col2.addAll(col);
        return new PlateBuffer(new NonHeapPlateList(this, ids, col2));
    }

    @Override
    public <E1 extends Entity> int delete(BufferQueryDelete<E1> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        return 0;
    }

    @Override
    public <E1 extends Entity> int update(BufferQueryUpdate<E1> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E1 extends Entity> E1 insert(BufferQueryInsert<E1> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public <E1 extends Entity> int delete(BufferQueryDelete<E1> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        return 0;
    }

    @Override
    public <E1 extends Entity> int update(BufferQueryUpdate<E1> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E1 extends Entity> E1 insert(BufferQueryInsert<E1> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public List<Entity> getTables() {
        return new ArrayList<>();
    }

    @Override
    public boolean isTransactionActive() {
        return false;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776"})
    protected List<Integer> finalFilter(IndexedFilterResult rw, Query<?, ?, ?> query) {
        int counter = 0;
        Integer[] pointers = rw == null ? null : rw.getPointers();
        List<Integer> skippedSteps = rw == null ? Collections.emptyList() : rw.getSkippedWhereSteps();

        List<Integer> rv = new ArrayList<>();
        Where wrapper = query.getWhereWrapper();
        boolean usedLimitWithoutOrder = query.getOrderBy() == null && query.getLimit() != null;
        //sometimes you may just order or sth...
        boolean useWhere = wrapper != null;

        int offset = !usedLimitWithoutOrder ? 0 : query.getLimit().getOffset();
        int limit = !usedLimitWithoutOrder ? 0 : query.getLimit().getLimit() > 0 ? (offset + query.getLimit().getLimit()) : Integer.MAX_VALUE;

        Predicate<Object> generalPredicate;
        if (!useWhere) {
            generalPredicate = new AlwaysTrue();
        } else if (WhereBase.OR.equals(wrapper.getRelationBetweenExpressions())) {
            GeneralBitFieldWhereOr genPred = new GeneralBitFieldWhereOr<>(this, wrapper, skippedSteps);
            generalPredicate = genPred.isEmpty() ? new AlwaysTrue() : genPred;
        } else {
            GeneralBitFieldWhereAnd genPred = new GeneralBitFieldWhereAnd<>(this, wrapper, skippedSteps);
            generalPredicate = genPred.isEmpty() ? new AlwaysTrue() : genPred;
        }

        if (usedLimitWithoutOrder) {
            if (pointers == null) {
                for (int i = 0; i < size(); i++) {

                    if (generalPredicate.test(i)) {
                        counter++;
                        if (counter < offset) {
                            continue;
                        } else if (counter > limit) {
                            break;
                        }
                        rv.add(i);
                    }
                }
            } else {
                for (int i : pointers) {
                    if (generalPredicate.test(i)) {
                        counter++;
                        if (counter < offset) {
                            continue;
                        } else if (counter > limit) {
                            break;
                        }
                        rv.add(i);
                    }
                }
            }
        } else {
            if (pointers == null) {
                for (int i = 0; i < size(); i++) {
                    if (generalPredicate.test(i)) {
                        rv.add(i);
                    }
                }
            } else {
                for (int item : pointers) {
                    if (generalPredicate.test(item)) {
                        rv.add(item);
                    }
                }
            }
        }
        return rv;
    }

    @SuppressWarnings({"rawtypes", "java:S3776", "java:S135"})
    //Filters provides ids or all (if list is null) for provided wrapper
    protected IndexedFilterResult filterByIndexes(List<Integer> entitiesToHandle, Where wrapper) {

        //if indexRepository is empty, don't even use the index logic
        if (wrapper == null || isIndexRepositoryEmpty || !mayBeIndexed(wrapper)) {
            return new IndexedFilterResult(entitiesToHandle.toArray(new Integer[0]), Collections.emptyList());
        }

        if (wrapper.getCounter() == 2 && wrapper.getKeyForPointer(1) != null) {
            Column column1 = wrapper.getKeyForPointer(1);

            BitBufferIndexBase index = indexRepository[getColumnIntoEntityPosition(column1)];
            if (index == null) {
                return new IndexedFilterResult(entitiesToHandle.toArray(new Integer[0]), Collections.emptyList());
            }
            Operator operator1 = wrapper.getRelationForPointer(1);
            Object val1 = wrapper.getValueForPointer(1);
            List<Integer> skipSteps1 = new ArrayList<>();
            skipSteps1.add(1);
            String relations1 = wrapper.getRelationBetweenExpressions();
            switch (relations1) {
                default:
                case OR:
                case AND: {
                    return new IndexedFilterResult(index.filter(operator1, val1), skipSteps1);
                }
                case NOT: {
                    return new IndexedFilterResult(index.filterNegative(operator1, val1), skipSteps1);
                }
            }
        }

        String relations = wrapper.getRelationBetweenExpressions();

        boolean[][] flags;

        if (OR.equals(relations)) {
            flags = new boolean[1][size()];
        } else {
            flags = new boolean[wrapper.getCounter()][size()];
        }

        List<Integer> skipSteps = new ArrayList<>();
        int indexedArguments = 0;
        int lowestSize = Integer.MAX_VALUE;
        Integer[] lowestCollection = new Integer[0];
        for (int counter = 1; counter < wrapper.getCounter(); counter++) {

            skipSteps.add(counter);
            //if Where has a second Where inside...
            Where<?> innerWhere = wrapper.getInnerWhere(counter);
            if (innerWhere != null) {
                IndexedFilterResult indexedFilterResult = filterByIndexes(entitiesToHandle, innerWhere);

                if (OR.equals(relations)) {
                    for (int in : indexedFilterResult.getPointers()) {
                        flags[0][in] = true;
                    }
                } else {
                    int size = indexedFilterResult.getPointers().length;
                    for (int in : indexedFilterResult.getPointers()) {
                        flags[indexedArguments][in] = true;
                    }

                    if (lowestSize > size) {
                        lowestSize = size;
                        lowestCollection = indexedFilterResult.getPointers();
                    }

                }
                indexedArguments++;
                continue;
            }

            Column column = wrapper.getKeyForPointer(counter);

            BitBufferIndexBase index = indexRepository[getColumnIntoEntityPosition(column)];
            if (index == null) {
                continue;
            }

            Operator operator = wrapper.getRelationForPointer(counter);
            Object val = wrapper.getValueForPointer(counter);

            switch (relations) {
                default:
                case AND: {
                    Integer[] filtered = index.filter(operator, val);

                    int size = filtered.length;
                    for (int in : filtered) {
                        flags[indexedArguments][in] = true;
                    }
                    if (lowestSize > size) {
                        lowestSize = size;
                        lowestCollection = filtered;
                    }
                    break;
                }
                case OR: {
                    Integer[] filtered = index.filter(operator, val);
                    for (int in : filtered) {
                        flags[0][in] = true;
                    }
                    break;
                }
                case NOT: {
                    Integer[] filtered = index.filterNegative(operator, val);
                    for (int in : filtered) {
                        flags[indexedArguments][in] = true;
                    }
                    break;
                }
            }
            indexedArguments++;
        }

        List<Integer> pointers = new ArrayList<>();
        if (entitiesToHandle == null) {
            if (OR.equals(relations)) {
                for (int p = 0; p < flags[0].length; p++) {
                    if (flags[0][p]) {
                        pointers.add(p);
                    }
                }
            } else {
                for (int p : lowestCollection) {
                    int o = 0;
                    for (; o < indexedArguments; o++) {
                        if (!flags[o][p]) {
                            break;
                        }
                    }
                    if (o == indexedArguments) {
                        pointers.add(p);
                    }
                }
            }
        } else {
            if (OR.equals(relations)) {
                for (int p = 0; p < entitiesToHandle.size(); p++) {
                    if (flags[0][entitiesToHandle.get(p)]) {
                        pointers.add(p);
                    }
                }
            } else {
                for (int p = 0; p < entitiesToHandle.size(); p++) {
                    int o = 0;
                    for (; o < indexedArguments; o++) {
                        if (!flags[o][entitiesToHandle.get(p)]) {
                            break;
                        }
                    }
                    if (o == indexedArguments) {
                        pointers.add(p);
                    }
                }
            }
        }

        return new IndexedFilterResult(pointers.toArray(new Integer[0]), skipSteps);
    }

    @SuppressWarnings("rawtypes")
    //If there is OR, indexes may be in in use ONLY if all where arguments have them.
    private boolean mayBeIndexed(Where wrapper) {
        if (OR.equals(wrapper.getRelationBetweenExpressions())) {
            for (int counter = 1; counter < wrapper.getCounter(); counter++) {
                Where<?> innerWhere = wrapper.getInnerWhere(counter);
                if (innerWhere != null && !mayBeIndexed(innerWhere)) {
                    return false;
                }
                if (innerWhere == null) {
                    Column column = wrapper.getKeyForPointer(counter);

                    if (indexRepository[getColumnIntoEntityPosition(column)] == null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<ByteBuffer> getPages() {
        return pages;
    }

    @Override
    public AccessProtector getAccessProtector() {
        return accessProtector;
    }

    @Override
    public void setAccessProtector(AccessProtector accessProtector) {
        this.accessProtector = accessProtector;
    }


    public BitFieldRegistry getBitFieldRegistry() {
        return bitFieldRegistry;
    }


    public BitField getBifFieldForColumn(Column column) {
        int columnPosition = columnsOrder.get(column.getColumnName());
        return bitFields[columnPosition];
    }

}
