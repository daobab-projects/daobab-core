package io.daobab.target.database.query;

import io.daobab.error.MandatoryTargetException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.query.base.QueryType;
import io.daobab.target.database.QueryTarget;
import io.daobab.transaction.Propagation;

import java.util.Collections;
import java.util.Map;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
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
        if (column == null) throw new MandatoryTargetException();
        init(target, target.getEntityName(column.getEntityClass()));
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

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }

}
