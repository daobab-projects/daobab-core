package io.daobab.query;

import io.daobab.error.TargetNoCacheNoEntityManagerException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryType;
import io.daobab.result.Entities;
import io.daobab.result.EntitiesProvider;
import io.daobab.result.EntityByteBuffer;
import io.daobab.statement.condition.Count;
import io.daobab.statement.inner.InnerQueryEntity;
import io.daobab.statement.inner.InnerQueryField;
import io.daobab.statement.inner.InnerSelectManyEntities;
import io.daobab.target.QueryTarget;
import io.daobab.target.database.DataBaseTarget;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public final class QueryEntity<E extends Entity> extends Query<E, QueryEntity<E>> implements InnerQueryEntity<E>, EntitiesProvider<E> {

    @SuppressWarnings("unused")
    private QueryEntity() {
    }

    QueryEntity(QueryTarget target, Column<E, ?, ?> column) {
        init(target, column.getEntityName());
    }

    public QueryEntity(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public QueryEntity(QueryTarget target, E entity) {
        init(target, entity);
        setFields(new ArrayList<>(entity.columns().size()));
        entity.columns().forEach(e -> getFields().add(e));
    }

    public QueryEntity(String nativeQuery, QueryTarget target, E entity) {
        this(target,entity);
        this._nativeQuery = nativeQuery;
    }


    /**
     * Prepare Select to usage into Where clause as subselect
     *
     * @return
     */
    @Override
    public InnerSelectManyEntities<E> innerResult() {

        if (this.getTarget().isBuffer()) {
            return new InnerSelectManyEntities<>(findMany());
        } else {
            return new InnerSelectManyEntities<>(this);
        }
    }



    public long countBy(Count cnt) {
        setTempCount(cnt);
        if (getTarget().isBuffer()) {
            if (cnt.countEntities()) {
                return findMany().size();
            } else {
                //TODO: czy tu ma byc _unique??
                return 0;////return new Long(resultFieldUniqueSetFromCache((ColumnDefinition<E, ?,?>)cnt.getFieldForPointer(1)).size());
            }

        }
        if (getTarget() instanceof DataBaseTarget) {
            DataBaseTarget emprov = (DataBaseTarget) getTarget();
            return emprov.count(this);


        }
        throw new TargetNoCacheNoEntityManagerException(getTarget());
    }


    @Override
    public long countAny() {
        return countBy(Count.any());
    }


    //---- RESULT SECTION


    @Override
    public Entities<E> findMany() {
        return getTarget().readEntityList(modifyQuery());
    }

    @Override
    public Optional<E> findFirst() {
        return Optional.ofNullable(getTarget().readEntity(modifyQuery()));
    }

    @Override
    public <E1 extends Entity, F, R extends EntityRelation> InnerQueryField<E1, F> limitToField(Column<E1, F, R> field) {
        // TODO Auto-generated method stub
        QueryField<E1, F> sbdl = new QueryField<>(getTarget(), field);
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
