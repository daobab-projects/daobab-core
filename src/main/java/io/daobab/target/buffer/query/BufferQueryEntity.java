package io.daobab.target.buffer.query;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.base.QueryType;
import io.daobab.target.buffer.single.Entities;
import io.daobab.result.EntitiesProvider;
import io.daobab.target.buffer.bytebyffer.EntityByteBuffer;
import io.daobab.statement.condition.Count;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryField;
import io.daobab.target.buffer.BufferQueryTarget;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
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

    public long countBy(Count cnt) {
        setTempCount(cnt);
        if (cnt.countEntities()) {
            return findMany().size();
        } else {
            //TODO: czy tu ma byc _unique??
            return 0;////return new Long(resultFieldUniqueSetFromCache((ColumnDefinition<E, ?,?>)cnt.getFieldForPointer(1)).size());
        }
    }

    @Override
    public long countAny() {
        return countBy(Count.any());
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
    public <E1 extends Entity, F, R extends EntityRelation> InnerQueryField<E1, F> limitToField(Column<E1, F, R> field) {
        // TODO Auto-generated method stub
        BufferQueryField<E1, F> sbdl = new BufferQueryField<>(getTarget(), field);
        sbdl = sbdl.where(getWhereWrapper());//.setOrderBy(this.orderBy);

        return sbdl;
    }

    @Override
    public EntityByteBuffer<E> createByteBuffer() {
        return new EntityByteBuffer<E>(this.findMany());
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }


}
