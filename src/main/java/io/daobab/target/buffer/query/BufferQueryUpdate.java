package io.daobab.target.buffer.query;

import io.daobab.error.AtLeastOneColumnToUpdateIsRequired;
import io.daobab.error.TargetMandatoryException;
import io.daobab.model.*;
import io.daobab.parser.ParserGeneral;
import io.daobab.query.base.QueryType;
import io.daobab.statement.condition.SetFields;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.transaction.Propagation;

import java.util.Map;

import static io.daobab.statement.where.WhereAnd.and;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public final class BufferQueryUpdate<E extends Entity> extends BufferQueryBase<E, BufferQueryUpdate<E>> implements ParserGeneral {

    private SetFields setFields;
    private E entity;

    @SuppressWarnings("unused")
    private BufferQueryUpdate() {
    }

    public BufferQueryUpdate(BufferQueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    //This is for the whole object update
    public <E1 extends PrimaryKey<E, F, ?>, F> BufferQueryUpdate(BufferQueryTarget target, E1 entity) {
        if (target == null) throw new TargetMandatoryException();
        init(target, entity);

        SetFields sf = new SetFields();
        for (TableColumn ecol : entity.columns()) {
            Column col = ecol.getColumn();
            if (col.getColumnName().equals(entity.colID().getColumnName())) continue;
            if (col.getThisValue() == null) {
                sf.setNull(col);
            } else {
                sf.setValue((Column<?, Object, ?>) col, col.getThisValue());
            }
        }
        set(sf);
        where(and().equal(entity.colID(), entity.getId()));

        setEntity((E) entity);
    }

    public BufferQueryUpdate(BufferQueryTarget target, E entity, boolean something) {
        if (target == null) throw new TargetMandatoryException();
        init(target, entity);

        setEntity(entity);
    }

    @SuppressWarnings("unchecked")
    public BufferQueryUpdate(BufferQueryTarget target, SetFields setFields) {
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

    public SetFields getSetFields() {
        return setFields;
    }

    public BufferQueryUpdate<E> set(SetFields setFields) {
        this.setFields = setFields;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public <F, R extends EntityRelation> BufferQueryUpdate<E> set(Column<E, F, R> key, R value) {
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
