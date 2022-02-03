package io.daobab.target.database.query;

import io.daobab.error.TargetNoCacheNoEntityManagerException;
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
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.DataBaseTarget;

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
        this(target,entity);
        this._nativeQuery = nativeQuery;
    }


//    /**
//     * Prepare Select to usage into Where clause as subselect
//     *
//     * @return
//     */
//    @Override
//    public InnerSelectManyEntities<E> innerResult() {
//
//        if (this.getTarget().isBuffer()) {
//            return new InnerSelectManyEntities<>(findMany());
//        } else {
//            return new InnerSelectManyEntities<>(this);
//        }
//    }



    public long countBy(Count cnt) {
        setTempCount(cnt);

        DataBaseTarget emprov = (DataBaseTarget) getTarget();
        return emprov.count(this);

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
        DataBaseQueryField<E1, F> sbdl = new DataBaseQueryField<>(getTarget(), field);
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

    @Override
    public String toSqlQuery() {
        return getTarget().toSqlQuery(this);
    }

}
