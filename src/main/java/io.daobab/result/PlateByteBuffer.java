package io.daobab.result;

import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.result.bytebuffer.BitField;
import io.daobab.result.index.BitBufferIndexBase;
import io.daobab.target.OpenedTransactionTarget;
import io.daobab.target.protection.OperationType;
import io.daobab.transaction.Propagation;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class PlateByteBuffer extends BaseByteBuffer<Plate> {

    public PlateByteBuffer(List<Plate> entities) {
        this(entities == null || entities.isEmpty() ? null : entities.get(0), entities == null ? 8 : entities.size());
        if (entities != null) {
            entities.stream().forEach(this::add);
        }
    }

    public PlateByteBuffer(Plate entity) {
        this(entity, 8);
    }

    public PlateByteBuffer(Plate entity, int capacity) {
        adjustForCapacity(capacity);//1 << pageMaxCapacityBytes;
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

    public void remove(int position) {
        Plate entityToRemove = get(position);
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

    //    @Override
    public boolean add(Plate entity) {
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

    public Plate get(int i) {
        Plate rv = new Plate();

        int entityLocation = locations.get(i);
        int page = entityLocation >> pageMaxCapacityBytes;
        int rowAtPage = entityLocation - (page << pageMaxCapacityBytes);
        int posEntity = rowAtPage * totalEntitySpace;
        int cnt = 0;
        ByteBuffer pageBuffer = pages.get(page);
        for (TableColumn ecol : columns) {
            Column col = ecol.getColumn();
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
            col.setValue((EntityRelation) rv, bitFieldOperations[cnt].readValue(pageBuffer, posEntity + posCol));
            cnt++;
        }
        return rv;
    }

    public List<Plate> finalFilter(Query<?, ?> query) {
        List<Integer> ids = finalFilter(filterUsingIndexes(null, query.getWhereWrapper()), query);
        List<Plate> rv = new PlateByteBufferList(this, ids, query.getFields());
        return rv;
    }

    @Override
    public <E extends Entity> E readEntity(QueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Query q = query;
        return (E) finalFilter(q).get(0);
    }

    @Override
    public <E1 extends Entity> Entities<E1> readEntityList(QueryEntity<E1> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        Query q = query;
        List<Plate> list = finalFilter(q);
        EntityList rv = new EntityList<E1>((List<E1>) list.stream().map(p -> p.toEntity(query.getEntityClass(), query.getFields())).collect(Collectors.toList()), query.getEntityClass());
        return rv;
    }

    @Override
    public <E extends Entity, F> F readField(QueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        QueryField q = query;
        return (F) finalFilterField(q).get(0);
    }

    @Override
    public <E extends Entity, F> List<F> readFieldList(QueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        QueryField q = query;

        long start = System.currentTimeMillis();
        List<F> list = finalFilterField(q);
        long stop = System.currentTimeMillis();
        System.out.println("filter result: " + list.size() + " time: " + (stop - start));

        return list;
    }

    @Override
    public Plate readPlate(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        return readPlateList(query).get(0);
    }

    @Override
    public Plates readPlateList(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        Query q = query;
        List<Integer> ids = finalFilter(filterUsingIndexes(null, query.getWhereWrapper()), q);

        List<TableColumn> col = query.getFields();
        List<TableColumn> col2 = new ArrayList<>(col.size());
        for (TableColumn c : col) {
            col2.add(c);
        }
        PlateByteBufferList rv = new PlateByteBufferList(this, ids, col2);
        return new PlateBuffer(rv);
    }

    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        return 0;
    }

    @Override
    public <E extends Entity> int update(QueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(QueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        return 0;
    }

    @Override
    public <E extends Entity> int update(QueryUpdate<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        return 0;
    }

    @Override
    public <E extends Entity> E insert(QueryInsert<E> query, Propagation propagation) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        return null;
    }

    @Override
    public boolean isBuffer() {
        return true;
    }

    @Override
    public boolean isConnectedToDatabase() {
        return false;
    }

    @Override
    public OpenedTransactionTarget beginTransaction() {
        return null;
    }

    @Override
    public List<Entity> getTables() {
        return null;
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
    public <E extends Entity> String toSqlQuery(Query<E, ?> query) {
        throw new DaobabException("This target does not produce sql query");
    }
}
