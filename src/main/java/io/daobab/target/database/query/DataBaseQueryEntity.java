package io.daobab.target.database.query;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.base.QueryType;
import io.daobab.result.EntitiesProvider;
import io.daobab.statement.condition.Count;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.target.buffer.bytebyffer.EntityByteBuffer;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.QueryTarget;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
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
        setFields(new ArrayList<>(entity.columns().size()));
        entity.columns().forEach(e -> getFields().add(e));
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


    //---- RESULT SECTION

    @Override
    public Entities<E> findMany() {
        return getTarget().readEntityList(modifyQuery(this));
    }

    @Override
    public Optional<E> findFirst() {
        return Optional.ofNullable(getTarget().readEntity(modifyQuery(this)));
    }

    @Override
    public <E1 extends Entity, F, R extends EntityRelation> InnerQueryFieldsProvider<E1, F> limitToField(Column<E1, F, R> column) {
        DataBaseQueryField<E1, F> dataBaseQueryField = new DataBaseQueryField<>(getTarget(), column);
        return dataBaseQueryField.where(getWhereWrapper());
    }

    @Override
    public EntityByteBuffer<E> createByteBuffer() {
        return new EntityByteBuffer<E>(this.findMany());
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }

    @Override
    public String toSqlQuery() {
        return getTarget().toSqlQuery(this);
    }

}
