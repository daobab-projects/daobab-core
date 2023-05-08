package io.daobab.target.database.query;

import io.daobab.error.AtLeastOneColumnToUpdateIsRequired;
import io.daobab.error.MandatoryTargetException;
import io.daobab.model.*;
import io.daobab.parser.ParserGeneral;
import io.daobab.query.base.QueryType;
import io.daobab.statement.condition.SetFields;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.connection.QueryResolverTransmitter;
import io.daobab.transaction.Propagation;

import java.util.Map;

import static io.daobab.statement.where.WhereAnd.and;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public final class DataBaseQueryUpdate<E extends Entity> extends DataBaseQueryBase<E, DataBaseQueryUpdate<E>> implements ParserGeneral {

    private SetFields setFields;
    private E entity;

    @SuppressWarnings("unused")
    private DataBaseQueryUpdate() {
    }

    public DataBaseQueryUpdate(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    //This is for the entire object update
    public <E1 extends PrimaryKey<E, F, ?>, F> DataBaseQueryUpdate(QueryTarget target, E1 entity) {
        if (target == null) throw new MandatoryTargetException();
        init(target, entity);

        SetFields sf = new SetFields();
        for (TableColumn tableColumn : target.getColumnsForTable(entity)) {
            Column column = tableColumn.getColumn();
            if (column.getColumnName().equals(entity.colID().getColumnName())) continue;
            if (column.getThisValue() == null) {
                sf.setNull(column);
            } else {
                sf.setValue((Column<?, Object, ?>) column, column.getThisValue());
            }
        }
        set(sf);
        where(and().equal(entity.colID(), entity.getId()));

        setEntity((E) entity);
    }

    public DataBaseQueryUpdate(QueryTarget target, E entity, boolean something) {
        if (target == null) throw new MandatoryTargetException();
        init(target, entity);

        setEntity(entity);
    }

    @SuppressWarnings("unchecked")
    public DataBaseQueryUpdate(QueryTarget target, SetFields setFields) {
        if (setFields == null || setFields.getCounter() == 0) {
            throw new AtLeastOneColumnToUpdateIsRequired();
        }
        init(target, setFields.getFieldForPointer(1).getEntityName());

        set(setFields);
        setEntity((E) setFields.getFieldForPointer(1).getInstance());
    }

    public Integer execute(boolean transaction) {
        return getTarget().update(this, transaction);
    }

    public Integer execute(Propagation propagation) {
        return getTarget().update(this, propagation);
    }

    public Integer execute() {
        return getTarget().update(this, Propagation.SUPPORTS);
    }

    public String getSQLQuery(QueryResolverTransmitter target) {
        return target.toUpdateSqlQuery(this).getQuery().toString();
    }

    public SetFields getSetFields() {
        return setFields;
    }

    public DataBaseQueryUpdate<E> set(SetFields setFields) {
        this.setFields = setFields;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public <F, R extends EntityRelation> DataBaseQueryUpdate<E> set(Column<E, F, R> key, R value) {
        set(new SetFields().setValue(key, value));
        return this;
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
