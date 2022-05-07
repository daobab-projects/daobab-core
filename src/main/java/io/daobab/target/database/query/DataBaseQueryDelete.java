package io.daobab.target.database.query;

import io.daobab.error.TargetMandatoryException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.query.base.QueryType;
import io.daobab.target.database.QueryTarget;
import io.daobab.transaction.Propagation;

import java.util.Collections;
import java.util.Map;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public final class DataBaseQueryDelete<E extends Entity> extends DataBaseQueryBase<E, DataBaseQueryDelete<E>> {

    private E entity;

    @SuppressWarnings("unused")
    private DataBaseQueryDelete() {
    }

    public DataBaseQueryDelete(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    public DataBaseQueryDelete(QueryTarget target, E entity) {
        init(target, entity);
        setEntity(entity);
    }

    public DataBaseQueryDelete(QueryTarget target, Column<E, ?, ?> column) {
        if (column == null) throw new TargetMandatoryException();
        init(target, column.getEntityName());
        setFields(Collections.singletonList(getInfoColumn(column)));
    }

    public int execute(boolean transaction) {
        return getTarget().delete(this, transaction);
    }

    public int execute(Propagation propagation) {
        return getTarget().delete(this, propagation);
    }

    public int execute() {
        return getTarget().delete(this, Propagation.SUPPORTS);
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

//    @SuppressWarnings({"unchecked","rawtypes"})
//    public QueryEntity<E> toSelect(){
//        QueryEntity<E> query;
//       if (getEntity()==null){
//           if (getFields().isEmpty()){
//               throw new DaobabException("You cannot convert delete query to select query without entity or column provided.");
//           }
//           query = new QueryEntity<>(getTarget(), getFields().get(0).getColumn());
//       }else{
//           query= new QueryEntity<>(getTarget(),getEntity());
//       }
//
//        query.setWhereWrapper(getWhereWrapper());
//        query.setJoins(getJoins());
//        query.setLimit(getLimit());
//        query.orderBy(getOrderBy());
//        query.having(getHavingWrapper());
//        query.countBy(getCount());
//        query._groupBy = (getGroupBy());
//        return query;
//    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }

    @Override
    public String toSqlQuery() {
        return getTarget().toSqlQuery(this);
    }
}
