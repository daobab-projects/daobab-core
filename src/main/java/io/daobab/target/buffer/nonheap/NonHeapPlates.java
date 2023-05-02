package io.daobab.target.buffer.nonheap;

import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.target.buffer.nonheap.access.field.BitField;
import io.daobab.target.buffer.nonheap.access.index.BitIndex;
import io.daobab.target.buffer.nonheap.index.BitBufferIndexBase;
import io.daobab.target.buffer.query.*;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.buffer.transaction.OpenedTransactionBufferTarget;
import io.daobab.target.protection.OperationType;
import io.daobab.transaction.Propagation;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class NonHeapPlates extends NonHeapBuffer<Plate> {

    public NonHeapPlates(List<Plate> plates) {
        this(plates, new BitFieldRegistry());
    }

    public NonHeapPlates(List<Plate> plates, BitFieldRegistry bitFieldRegistry) {
        this(plates == null || plates.isEmpty() ? null : plates.get(0), plates == null ? 8 : plates.size(), bitFieldRegistry);
        if (plates != null) {
            plates.forEach(this::add);
        }

        int columnNo = 0;
        for (TableColumn tableColumn : columns) {
            Optional<BitIndex> bb = bitFieldRegistry.createIndex(tableColumn, plates);
            indexRepository[columnNo] = bb.orElse(null);
            if (indexRepository[columnNo] != null) {
                isIndexRepositoryEmpty = false;
            }
            columnNo++;
        }
    }

    public NonHeapPlates(Plate entity) {
        this(entity, 8, new BitFieldRegistry());
    }

    public NonHeapPlates(Plate entity, BitFieldRegistry bitFieldRegistry) {
        this(entity, 8, bitFieldRegistry);
    }

    public NonHeapPlates(Plate entity, int capacity, BitFieldRegistry bitFieldRegistry) {
        super(bitFieldRegistry);
        adjustForCapacity(capacity);//1 << pageMaxCapacityBytes;
        this.columns = getColumnsForTable(entity);
        List<TableColumn> columns = getColumnsForTable(entity);
        columnsOrder = new HashMap<>();
        columnsPositionsQueue = new Integer[columns.size()];
        bitFields = new BitField[columns.size()];
        indexRepository = new BitBufferIndexBase[columns.size()];
        int offsetCounter = 1;
        int columnNo = 0;

        for (TableColumn tableColumn : columns) {
            Column<?, ?, ?> col = tableColumn.getColumn();

            Optional<BitField<Object>> bitField = bitFieldRegistry.getBitFieldFor(tableColumn);
            if (!bitField.isPresent()) {
                throw new DaobabException(this, "Class %s is not registered in %s", col.getFieldClass(), bitFieldRegistry.getClass().getName());
            }
            bitFields[columnNo] = bitField.get();
            int fieldSize = bitFields[columnNo].calculateSpace(tableColumn);

            if (!bitFieldRegistry.mayBeBitBuffered(col)) {
                throw new DaobabException("Column %s cannot be bitbuffered", col.getColumnName());
//                columnsPositionsQueue[columnOrderIntoEntity] = null;
//                columnOrderIntoEntity++;
//                continue;
            }

            columnsPositionsQueue[columnNo] = offsetCounter;
            columnsOrder.put(col.getColumnName(), columnNo);
//            indexRepository[columnNo] = determineIndex(tableColumn);
//            if (indexRepository[columnNo] != null) {
//                isIndexRepositoryEmpty = false;
//            }
            offsetCounter = offsetCounter + fieldSize;
            columnNo++;
        }
        totalEntitySpace = offsetCounter;
        pages = new ArrayList<>();
        pages.add(ByteBuffer.allocateDirect(totalEntitySpace << pageMaxCapacityBytes));

        totalBufferSize = pages.size() * (totalEntitySpace << pageMaxCapacityBytes);

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void remove(int position) {
        Plate entityToRemove = get(position);
        int entityLocation = locations.get(position);
        locations.remove(position);
        removed.add(position);
        additionalParameters.remove(entityLocation);

        if (entityToRemove == null) {
            return;
        }

        for (TableColumn tableColumn : getColumnsForTable(entityToRemove)) {
            Column column = tableColumn.getColumn();
            int pointer = columnsOrder.get(column.getColumnName());

            BitBufferIndexBase index = indexRepository[pointer];
            if (index != null) {
                index.removeValue(column.getValueOf((EntityRelation) entityToRemove), position);
            }
        }

        totalBufferActiveElements.decrementAndGet();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean add(Plate entity) {
        int entityLocation = getNextFreeLocation();

        int page = entityLocation >> pageMaxCapacityBytes;
        int rowAtPage = entityLocation - (page << pageMaxCapacityBytes);

        if (page > pages.size() - 1) {
            pages.add(ByteBuffer.allocateDirect(totalEntitySpace << pageMaxCapacityBytes));
        }

        for (TableColumn tableColumn : getColumnsForTable(entity)) {
            Column column = tableColumn.getColumn();
            Integer pointer = columnsOrder.get(column.getColumnName());
            Object value = column.getValueOf((EntityRelation) entity);
            if (pointer == null) {
                Map<String, Object> additionalValues = additionalParameters.getOrDefault(entityLocation, new HashMap<>());
                additionalValues.put(column.toString(), value);
                continue;
            }
            int posCol = rowAtPage * totalEntitySpace + columnsPositionsQueue[pointer];

            pages.get(page).position(0);
            bitFields[pointer].writeValue(pages.get(page), posCol, value);
            BitBufferIndexBase index = indexRepository[pointer];
            if (index != null) {
                index.addValue(value, entityLocation);
            }
        }

        mergeIfNecessary();

        return true;
    }

    @Override
    public Plate getPlate(int i, Collection<TableColumn> chosenColumns) {
        Plate rv = new Plate();

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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Plate get(int i) {
        Plate rv = new Plate();

        int entityLocation = locations.get(i);
        int page = entityLocation >> pageMaxCapacityBytes;
        int rowAtPage = entityLocation - (page << pageMaxCapacityBytes);
        int posEntity = rowAtPage * totalEntitySpace;
        int cnt = 0;
        ByteBuffer pageBuffer = pages.get(page);
        for (TableColumn tableColumn : columns) {
            Column column = tableColumn.getColumn();
            Integer posCol = columnsPositionsQueue[cnt];
            if (posCol == null) {
                HashMap<String, Object> additionalValues = additionalParameters.get(entityLocation);
                if (additionalValues != null) {
                    column.setValue((EntityRelation) rv, additionalValues.get(column.toString()));
                }
                cnt++;
                continue;
            }
            pageBuffer.position(0);
            column.setValue((EntityRelation) rv, bitFields[cnt].readValue(pageBuffer, posEntity + posCol));
            cnt++;
        }
        return rv;
    }

    public List<Plate> finalFilter(Query<?, ?, ?> query) {
        List<Integer> ids = finalFilter(filterByIndexes(null, query.getWhereWrapper()), query);
        List<Plate> rv = new NonHeapPlateList(this, ids, query.getFields());
        return rv;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <E extends Entity> E readEntity(BufferQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Query q = query;
        return (E) finalFilter(q).get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E1 extends Entity> Entities<E1> readEntityList(BufferQueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Query<E1, ?, ?> q = query;
        List<Plate> list = finalFilter(q);
        return new EntityList<>((List<E1>) list.stream()
                .map(p -> p.toEntity((Class<EntityMap>) query.getEntityClass(), query.getFields()))
                .collect(Collectors.toList()), query.getEntityClass());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <E extends Entity, F> F readField(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        BufferQueryField q = query;
        return (F) finalFilterField(q).get(0);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <E extends Entity, F> List<F> readFieldList(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        BufferQueryField q = query;

        long start = System.currentTimeMillis();
        List<F> list = finalFilterField(q);
        long stop = System.currentTimeMillis();

        return list;
    }

    @Override
    public Plate readPlate(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        return readPlateList(query).get(0);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Plates readPlateList(BufferQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        Query q = query;
        List<Integer> ids = finalFilter(filterByIndexes(null, query.getWhereWrapper()), q);

        List<TableColumn> col = query.getFields();
        List<TableColumn> col2 = new ArrayList<>(col.size());
        for (TableColumn c : col) {
            col2.add(c);
        }
        NonHeapPlateList rv = new NonHeapPlateList(this, ids, col2);
        return new PlateBuffer(rv);
    }

    @Override
    public <E extends Entity> int delete(BufferQueryDelete<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        return 0;
    }

    @Override
    public <E extends Entity> int update(BufferQueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(BufferQueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public <E extends Entity> int delete(BufferQueryDelete<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        return 0;
    }

    @Override
    public <E extends Entity> int update(BufferQueryUpdate<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(BufferQueryInsert<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public OpenedTransactionBufferTarget beginTransaction() {
        return null;
    }

    @Override
    public List<Entity> getTables() {
        return Collections.emptyList();
    }

    @Override
    public boolean isTransactionActive() {
        return false;
    }

}
