package io.daobab.target.buffer.query;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.base.QueryType;
import io.daobab.result.EntitiesProvider;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.noheap.NoHeapEntities;
import io.daobab.target.buffer.single.Entities;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public final class BufferQueryEntity<E extends Entity> extends BufferQueryBase<E, BufferQueryEntity<E>> implements InnerQueryEntity<E>, EntitiesProvider<E> {

    @SuppressWarnings("unused")
    private BufferQueryEntity() {
    }

    BufferQueryEntity(BufferQueryTarget target, Column<E, ?, ?> column) {
        init(target, column.getEntityName());
    }

    public BufferQueryEntity(BufferQueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public BufferQueryEntity(BufferQueryTarget target, E entity) {
        init(target, entity);
        setFields(new ArrayList<>(entity.columns().size()));
        entity.columns().forEach(e -> getFields().add(e));
    }

    @Override
    public long countAny() {
        return findMany().size();
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
    public <E1 extends Entity, F, R extends EntityRelation> InnerQueryFieldsProvider<E1, F> limitToField(Column<E1, F, R> field) {
        BufferQueryField<E1, F> queryField = new BufferQueryField<>(getTarget(), field);
        return queryField.where(getWhereWrapper());
    }

    @Override
    public NoHeapEntities<E> toNoHeap() {
        return new NoHeapEntities<>(this.findMany());
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }

}
