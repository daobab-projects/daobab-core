package io.daobab.target.buffer.bytebyffer;

import io.daobab.error.ByteBufferIOException;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.TargetNotSupports;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.result.*;
import io.daobab.result.bytebuffer.BitField;
import io.daobab.result.index.BitBufferIndexBase;
import io.daobab.target.buffer.transaction.OpenedTransactionBufferTarget;
import io.daobab.target.buffer.query.*;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.protection.OperationType;
import io.daobab.transaction.Propagation;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.*;

public class EntityByteBuffer<E extends Entity> extends BaseByteBuffer<E> implements EntitiesProvider<E> {

    protected Class<E> entityClass;

    public EntityByteBuffer(List<E> entities) {
        this(entities == null || entities.isEmpty() ? null : entities.get(0), entities == null ? 8 : entities.size());
        if (entities != null) {
            entities.forEach(this::add);
        }
    }

    public EntityByteBuffer(E entity) {
        this(entity, 8);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public EntityByteBuffer(E entity, int capacity) {
        adjustForCapacity(capacity);//1 << pageMaxCapacityBytes;
        this.entityClass = (Class<E>) entity.getEntityClass();
        this.columns = entity.columns();
        List<TableColumn> columns = entity.columns();
        columnOrderIntoEntityHashMap = new HashMap<>();
        columnsPositionsQueue = new Integer[columns.size()];
        bitFieldOperations = new BitField[columns.size()];
        indexRepository = new BitBufferIndexBase[columns.size()];
        int offsetCounter = 1;
        int columnOrderIntoEntity = 0;

        for (TableColumn ecol : columns) {
            Column col = ecol.getColumn();
            bitFieldOperations[columnOrderIntoEntity] = determineBitField(col);
            int fieldSize = bitFieldOperations[columnOrderIntoEntity].calculateSpace(ecol);
            if (!mayBeBitBuffered(col)) {
                columnsPositionsQueue[columnOrderIntoEntity] = null;
                columnOrderIntoEntity++;
                continue;
            }
            columnsPositionsQueue[columnOrderIntoEntity] = offsetCounter;
            columnOrderIntoEntityHashMap.put(col.getColumnName(), columnOrderIntoEntity);
            indexRepository[columnOrderIntoEntity] = determineIndex(ecol);
            if (indexRepository[columnOrderIntoEntity] != null) {
                isIndexRepositoryEmpty = false;
            }
            offsetCounter = offsetCounter + fieldSize;
            columnOrderIntoEntity++;
        }
        totalEntitySpace = offsetCounter;
        pages = new ArrayList<>();
        pages.add(ByteBuffer.allocateDirect(totalEntitySpace << pageMaxCapacityBytes));

        totalBufferSize = pages.size() * (totalEntitySpace << pageMaxCapacityBytes);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public void remove(int position) {
        E entityToRemove = get(position);
        int entityLocation = locations.get(position);
        locations.remove(position);
        removed.add(position);
        additionalParameters.remove(entityLocation);

        if (entityToRemove == null) {
            return;
        }

        for (TableColumn ecol : entityToRemove.columns()) {
            Column col = ecol.getColumn();
            int pointer = columnOrderIntoEntityHashMap.get(col.getColumnName());

            BitBufferIndexBase index = indexRepository[pointer];
            if (index != null) {
                index.removeValue(col.getValueOf((EntityRelation) entityToRemove), position);
            }
        }
        totalBufferActiveElements.decrementAndGet();
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public boolean add(E entity) {
        int entityLocation = getNextFreeLocation();

        int page = entityLocation >> pageMaxCapacityBytes;
        int rowAtPage = entityLocation - (page << pageMaxCapacityBytes);

        if (page > pages.size() - 1) {
            pages.add(ByteBuffer.allocateDirect(totalEntitySpace << pageMaxCapacityBytes));
        }

        for (TableColumn ecol : entity.columns()) {
            Column col = ecol.getColumn();
            Integer pointer = columnOrderIntoEntityHashMap.get(col.getColumnName());
            Object value = col.getValueOf((EntityRelation) entity);
            if (pointer == null) {
                Map<String, Object> additionalValues = additionalParameters.getOrDefault(entityLocation, new HashMap<>());
                additionalValues.put(col.toString(), value);
                continue;
            }
            int posCol = rowAtPage * totalEntitySpace + columnsPositionsQueue[pointer];

            pages.get(page).position(0);
            bitFieldOperations[pointer].writeValue(pages.get(page), posCol, value);
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
            rv.setValue(col, bitFieldOperations[cnt].readValue(pages.get(page), posEntity + posCol));
            cnt++;
        }
        return rv;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
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
                Column col = tableColumn.getColumn();
                Integer posCol = columnsPositionsQueue[cnt];
                if (posCol == null) {
                    HashMap<String, Object> additionalValues = additionalParameters.get(entityLocation);
                    if (additionalValues != null) {
                        col.setValue((EntityRelation) rv, additionalValues.get(col.toString()));
                    }
                    cnt++;
                    continue;
                }
                pageBuffer.position(0);
                try {
                    col.setValue((EntityRelation) rv, bitFieldOperations[cnt].readValue(pageBuffer, posEntity + posCol));
                } catch (Exception e) {
                    throw new ByteBufferIOException(tableColumn, e);
                }
                cnt++;
            }
            return rv;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<E> finalFilter(Query<E,?,?> query) {
        List<Integer> ids = finalFilter(filterUsingIndexes(null, query.getWhereWrapper()), query);
        return new EntityByteBufferList<>(this, ids);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    @Override
    public <E extends Entity> E readEntity(BufferQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return (E) finalFilter((Query) query).get(0);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    @Override
    public <E1 extends Entity> Entities<E1> readEntityList(BufferQueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return new EntityList<>(finalFilter((Query) query), query.getEntityClass());
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    @Override
    public <E extends Entity, F> F readField(BufferQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return (F) finalFilterField((BufferQueryField) query).get(0);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
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
        List<Integer> ids = finalFilter(filterUsingIndexes(null, query.getWhereWrapper()), query);

        List<TableColumn> col = query.getFields();
        List<TableColumn> col2 = new ArrayList<>(col.size());
        col2.addAll(col);
        PlateByteBufferList rv = new PlateByteBufferList(this, ids, col2);
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
    public boolean isLogQueriesEnabled() {
        return false;
    }


    @Override
    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public Entities<E> findMany() {
        try {
            return findAll(this.entityClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DaobabEntityCreationException(this.entityClass, e);
        }
    }

    @Override
    public Optional<E> findFirst() {
        return Optional.ofNullable(findOne());
    }

    @Override
    public long countAny() {
        return size();
    }
}
