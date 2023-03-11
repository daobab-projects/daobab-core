package io.daobab.target.database.query;

import io.daobab.model.*;
import io.daobab.query.base.QueryType;
import io.daobab.result.EntitiesProvider;
import io.daobab.statement.condition.Count;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.target.buffer.noheap.NoHeapEntities;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.QueryTarget;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("unused")
public final class DataBaseQueryEntity<E extends Entity> extends DataBaseQueryBase<E, DataBaseQueryEntity<E>> implements InnerQueryEntity<E>, EntitiesProvider<E> {

    @SuppressWarnings("unused")
    private DataBaseQueryEntity() {
    }

    DataBaseQueryEntity(QueryTarget target, Column<E, ?, ?> column) {
        init(target, column.getEntityName());
    }

    public DataBaseQueryEntity(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public DataBaseQueryEntity(QueryTarget target, E entity) {
        init(target, entity);
        setFields(new ArrayList<>(target.getColumnsForTable(entity).size()));
        target.getColumnsForTable(entity).forEach(e -> getFields().add(e));
    }

    public DataBaseQueryEntity(String nativeQuery, QueryTarget target, E entity) {
        this(target, entity);
        this._nativeQuery = nativeQuery;
    }

    @Override
    public long countAny() {
        setTempCount(Count.any());
        return getTarget().count(this);
    }

    @Override
    public Entities<E> findMany() {
        return getTarget().readEntityList(modifyQuery(this));
    }

    @Override
    public Optional<E> findFirst() {
        return Optional.ofNullable(getTarget().readEntity(modifyQuery(this)));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <E1 extends Entity, F, R extends EntityRelation> InnerQueryFieldsProvider<E1, F> limitToField(Column<E1, F, R> column) {
        DataBaseQueryField<E1, F> dataBaseQueryField = new DataBaseQueryField<>(getTarget(), column);
        return dataBaseQueryField.where(getWhereWrapper());
    }

    @Override
    public NoHeapEntities<E> toNoHeap() {
        return new NoHeapEntities<>(this.findMany());
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }


    @SuppressWarnings({"java:S2175", "rawtypes", "unchecked"})
    public DataBaseQueryEntity<E> skip(Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) return this;
        List<Column<E, ?, ?>> toRemove = new ArrayList<>();
        List<TableColumn> tableAllColumns = getTarget().getColumnsForTable(columns[0].getInstance());
        Collections.addAll(toRemove, columns);

        Entity entity = columns[0].getInstance();
        boolean primaryKeyEntity = entity instanceof PrimaryKey;
        if (primaryKeyEntity) {
            Column pkColumn = ((PrimaryKey) entity).colID();
            if (Arrays.stream(columns).anyMatch(c -> c.equalsColumn(pkColumn))) {
                //PK must stay if the entity has it.
                toRemove.remove(pkColumn);
            }
        }

        setFields(tableAllColumns.stream()
                .filter(t -> toRemove.stream().noneMatch(c -> c.equalsColumn(t.getColumn())))
                .filter(t -> getFields().stream().anyMatch(c -> c.getColumn().equalsColumn(t.getColumn())))
                .collect(Collectors.toList()));
        return this;
    }

    @SuppressWarnings({"java:S2175", "rawtypes", "unchecked"})
    public DataBaseQueryEntity<E> only(Column<E, ?, ?>... columns) {
        if (columns == null || columns.length == 0) return this;
        List<Column<E, ?, ?>> toLeave = new ArrayList<>();
        List<TableColumn> tableAllColumns = getTarget().getColumnsForTable(columns[0].getInstance());
        Collections.addAll(toLeave, columns);

        Entity entity = columns[0].getInstance();
        boolean primaryKeyEntity = entity instanceof PrimaryKey;
        if (primaryKeyEntity) {
            Column pkColumn = ((PrimaryKey) entity).colID();
            if (Arrays.stream(columns).noneMatch(c -> c.equalsColumn(pkColumn))) {
                //PK must stay if the entity has it.
                toLeave.add(pkColumn);
            }
        }
        setFields(tableAllColumns.stream()
                .filter(t -> toLeave.stream().anyMatch(c -> c.equalsColumn(t.getColumn())))
                .filter(t -> getFields().stream().anyMatch(c -> c.getColumn().equalsColumn(t.getColumn())))
                .collect(Collectors.toList()));
        return this;
    }

}
