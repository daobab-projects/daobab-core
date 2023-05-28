package io.daobab.target.buffer.query;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.query.base.QueryType;
import io.daobab.result.EntitiesProvider;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.nonheap.NonHeapEntities;
import io.daobab.target.buffer.single.Entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public final class BufferQueryEntity<E extends Entity> extends BufferQueryBase<E, BufferQueryEntity<E>> implements InnerQueryEntity<E>, EntitiesProvider<E> {

    @SuppressWarnings("unused")
    private BufferQueryEntity() {
    }

    BufferQueryEntity(BufferQueryTarget target, Column<E, ?, ?> column) {
        init(target, target.getEntityName(column.entityClass()));
    }

    public BufferQueryEntity(BufferQueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public BufferQueryEntity(BufferQueryTarget target, E entity) {
        init(target, entity);
        setFields(new ArrayList<>(entity.columns().size()));
        entity.columns().forEach(e -> getFields().add(e));
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

    @SuppressWarnings("rawtypes")
    @Override
    public <E1 extends Entity, F, R extends RelatedTo> InnerQueryFieldsProvider<E1, F> limitToField(Column<E1, F, R> field) {
        BufferQueryField<E1, F> queryField = new BufferQueryField<>(getTarget(), field);
        return queryField.where(getWhereWrapper());
    }

    @Override
    public NonHeapEntities<E> toNonHeap() {
        return new NonHeapEntities<>(this.findMany());
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }

}
