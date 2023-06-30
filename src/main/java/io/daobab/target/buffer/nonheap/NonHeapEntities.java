package io.daobab.target.buffer.nonheap;

import io.daobab.error.ByteBufferIOException;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.result.EntitiesProvider;
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

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NonHeapEntities<E extends Entity> extends NonHeapBuffer<E> implements EntitiesProvider<E> {

    protected Class<E> entityClass;

    public NonHeapEntities(List<E> entities) {
        this(entities, new BitFieldRegistry());
    }

    public NonHeapEntities(List<E> entities, BitFieldRegistry bitFieldRegistry) {

        super(bitFieldRegistry);
        E entity = entities == null || entities.isEmpty() ? null : entities.get(0);
        adjustForCapacity(8);//1 << pageMaxCapacityBytes;
        this.entityClass = (Class<E>) entity.getEntityClass();
        this.columns = getColumnsForTable(entity);
        List<TableColumn> columns = getColumnsForTable(entity);
        columnsOrder = new HashMap<>();
        columnsPositionsQueue = new Integer[columns.size()];
        bitFields = new BitField[columns.size()];
        indexRepository = new BitBufferIndexBase[columns.size()];
        int offsetCounter = 1;
        int columnNo = 0;

        for (TableColumn tableColumn : columns) {
            Column column = tableColumn.getColumn();
            Optional<BitField<Object>> bitField = bitFieldRegistry.getBitFieldFor(tableColumn);
            if (!bitField.isPresent()) {
                throw new DaobabException(this, "Class %s is not registered in %s", column.getFieldClass(), bitFieldRegistry.getClass().getName());
            }
            bitFields[columnNo] = bitField.get();
            int fieldSize = bitFields[columnNo].calculateSpace(tableColumn);
            if (!bitFieldRegistry.mayBeBitBuffered(column)) {
                throw new DaobabException("Column %s cannot be bitbuffered", column.getColumnName());
            }
            columnsPositionsQueue[columnNo] = offsetCounter;
            columnsOrder.put(column.getColumnName(), columnNo);

            offsetCounter = offsetCounter + fieldSize;
            columnNo++;
        }
        totalEntitySpace = offsetCounter;
        pages = new ArrayList<>();
        pages.add(ByteBuffer.allocateDirect(totalEntitySpace << pageMaxCapacityBytes));

        totalBufferSize = pages.size() * (totalEntitySpace << pageMaxCapacityBytes);


        if (entities != null) {
            entities.forEach(this::add);
        }

        columnNo = 0;
        for (TableColumn tableColumn : columns) {
            Optional<BitIndex> index = bitFieldRegistry.createIndex(tableColumn, entities);
            indexRepository[columnNo] = index.orElse(null);
            log.debug("column " + columnNo + " index: " + indexRepository[columnNo]);
            if (indexRepository[columnNo] != null) {
                isIndexRepositoryEmpty = false;
            }
            columnNo++;
        }
    }

    public NonHeapEntities(E entity) {
        this(entity, new BitFieldRegistry());
    }

    public NonHeapEntities(E entity, BitFieldRegistry bitFieldRegistry) {
        this(entity, 8, bitFieldRegistry);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public NonHeapEntities(E entity, int capacity, BitFieldRegistry bitFieldRegistry) {
        super(bitFieldRegistry);
        adjustForCapacity(capacity);//1 << pageMaxCapacityBytes;
        this.entityClass = (Class<E>) entity.getEntityClass();
        this.columns = getColumnsForTable(entity);
        List<TableColumn> columns = entity.columns();
        columnsOrder = new HashMap<>();
        columnsPositionsQueue = new Integer[columns.size()];
        bitFields = new BitField[columns.size()];
        indexRepository = new BitBufferIndexBase[columns.size()];
        int offsetCounter = 1;
        int columnNo = 0;

        for (TableColumn tableColumn : columns) {
            Column column = tableColumn.getColumn();
            Optional<BitField<Object>> bitField = bitFieldRegistry.getBitFieldFor(tableColumn);
            if (!bitField.isPresent()) {
                throw new DaobabException(this, "Class %s is not registered in %s", column.getFieldClass(), bitFieldRegistry.getClass().getName());
            }
            bitFields[columnNo] = bitField.get();
            int fieldSize = bitFields[columnNo].calculateSpace(tableColumn);
            if (!bitFieldRegistry.mayBeBitBuffered(column)) {
                throw new DaobabException("Column %s cannot be bitbuffered", column.getColumnName());
            }
            columnsPositionsQueue[columnNo] = offsetCounter;
            columnsOrder.put(column.getColumnName(), columnNo);

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
        E entityToRemove = get(position);
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
    public boolean add(E entity) {
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
        Plate rv = new Plate(chosenColumns);

        int entityLocation = locations.get(i);
        int page = entityLocation >> pageMaxCapacityBytes;
        int rowAtPage = entityLocation - (page << pageMaxCapacityBytes);
        int posEntity = rowAtPage * totalEntitySpace;
        int cnt = 0;
        for (TableColumn tableColumn : chosenColumns) {
            Integer posCol = columnsPositionsQueue[cnt];
            if (posCol == null) {
                HashMap<String, Object> additionalValues = additionalParameters.get(entityLocation);
                if (additionalValues != null) {
                    rv.setValue(tableColumn, additionalValues.get(tableColumn.toString()));
                }
                cnt++;
                continue;
            }
            pages.get(page).position(0);
            rv.setValue(tableColumn, bitFields[cnt].readValue(pages.get(page), posEntity + posCol));
            cnt++;
        }
        return rv;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public E get(int i) {
        try {
            E rv = entityClass.getDeclaredConstructor().newInstance();

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
                        rv = (E) column.setValue((EntityRelation) rv, additionalValues.get(column.toString()));
                    }
                    cnt++;
                    continue;
                }
                pageBuffer.position(0);
                try {
                    column.setValue((EntityRelation) rv, bitFields[cnt].readValue(pageBuffer, posEntity + posCol));
                } catch (Exception e) {
                    throw new ByteBufferIOException(tableColumn, e);
                }
                cnt++;
            }
            return rv;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new DaobabEntityCreationException(entityClass, e);
        }
    }

    public List<E> finalFilter(Query<E, ?, ?> query) {
        List<Integer> ids = finalFilter(filterByIndexes(null, query.getWhereWrapper()), query);
        return new NonHeapEntityList<>(this, ids);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <E extends Entity> E readEntity(BufferQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return (E) finalFilter((Query) query).get(0);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <E1 extends Entity> Entities<E1> readEntityList(BufferQueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return new EntityList<>(finalFilter((Query) query), query.getEntityClass());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <E extends Entity, F> F readField(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return (F) finalFilterField((BufferQueryField) query).get(0);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <E extends Entity, F> List<F> readFieldList(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);

        return finalFilterField((BufferQueryField) query);
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

    @Override
    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public Entities<E> findMany() {
        try {
            return findAll(this.entityClass.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new DaobabEntityCreationException(this.entityClass, e);
        }
    }

    @Override
    public Optional<E> findFirst() {
        return Optional.ofNullable(findOne());
    }

    @Override
    public NonHeapEntities<E> toNonHeap() {
        return this;
    }
}
