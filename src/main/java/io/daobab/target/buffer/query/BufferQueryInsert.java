package io.daobab.target.buffer.query;

import io.daobab.error.DaobabException;
import io.daobab.error.QueryMandatory;
import io.daobab.generator.DictRemoteKey;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryType;
import io.daobab.statement.condition.SetFields;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.transaction.Propagation;

import java.util.LinkedList;
import java.util.Map;

import static io.daobab.model.IdGeneratorType.*;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public final class BufferQueryInsert<E extends Entity> extends BufferQueryBase<E, BufferQueryInsert<E>> {

    private SetFields setFields;
    private String sequenceName;
    private Object pkNo;
    private IdGeneratorType dictIdGenerator;
    private String pkColumnName;
    private boolean pkResolved = false;
    private E entity;
    private Query<?, ?, ?> selectQuery;

    @SuppressWarnings("unused")
    private BufferQueryInsert() {
    }


    public BufferQueryInsert(BufferQueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    @SuppressWarnings("rawtypes")
    public BufferQueryInsert(BufferQueryTarget target, E entity) {

        init(target, entity);
        setEntity(entity);
        setFields(new LinkedList<>());

        SetFields sd = new SetFields();

        entity.beforeInsert(target);

        boolean entityIsPk = entity instanceof PrimaryKey;
        IdGeneratorType idgeneratorType = null;
        if (entityIsPk) {
            PrimaryKey<E, Object, EntityRelation> pk = (PrimaryKey<E, Object, EntityRelation>) entity;

            idgeneratorType = pk.getIdGeneratorType();
            if (SEQUENCE.equals(idgeneratorType)) {
                setSequenceName(pk.getSequenceName());
            } else if (GENERATOR.equals(idgeneratorType)) {
                pk.colID().setValue((EntityRelation) entity, target.getPrimaryKeyGenerator(pk).generateId(target));
            }

            setIdGenerator(idgeneratorType);
            setPkColumnName(pk.colID().getColumnName());
            setPkResolved(true);

            if (SEQUENCE.equals(idgeneratorType)) {
                Column pkcol = pk.colID();
                getFields().add(getInfoColumn(pkcol));
                sd.setValue(pkcol, 0L);
            }
        }

        for (TableColumn ec : entity.columns()) {
            Column c = ec.getColumn();
            Column<E, Object, EntityRelation> ce = (Column<E, Object, EntityRelation>) c;

            Object oo = ce.getValueOf((EntityRelation) entity);

            // no PK column into SEQUENCE,AUTO_INCREMENT
            if (entityIsPk && ((PrimaryKey) entity).colID().equals(c) && (SEQUENCE.equals(idgeneratorType) || AUTO_INCREMENT.equals(idgeneratorType))) {
                continue;
            }
            getFields().add(getInfoColumn(ce));
            sd.setValue(ce, oo);
        }
        set(sd);
    }

    public BufferQueryInsert<E> select(Query<?, ?, ?> query) {
        if (query == null) {
            throw new QueryMandatory();
        }

        if ((!(query instanceof BufferQueryEntity)) && (!(query instanceof BufferQueryPlate)) && (!(query instanceof BufferQueryField))) {
            throw new DaobabException("Insert and Select operation is allowed only for entities, plates or a single field");
        }

        if (!getTarget().equals(query.getTarget())) {
            throw new DaobabException("Insert and select query has to have the same target.");
        }

        setSelectQuery(query);
        return this;
    }

    public SetFields getSetFields() {
        return setFields;
    }

    public BufferQueryInsert<E> set(SetFields setFields) {
        this.setFields = setFields;
        return this;
    }

    public E execute(boolean transaction) {
        return getTarget().insert(this, transaction);
    }

    public E execute(Propagation propagation) {
        return getTarget().insert(this, propagation);
    }

    public E execute() {
        return getTarget().insert(this, Propagation.SUPPORTS);
    }

    @SuppressWarnings("rawtypes")
    public <F, R extends EntityRelation> BufferQueryInsert<E> set(Column<E, F, R> key, R value) {
        set(new SetFields().setValue(key, value));
        return this;
    }


    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public IdGeneratorType getDictIdGenerator() {
        return dictIdGenerator;
    }

    public void setIdGenerator(IdGeneratorType dictIdGenerator) {
        this.dictIdGenerator = dictIdGenerator;
    }

    public boolean isPkResolved() {
        return pkResolved;
    }

    public void setPkResolved(boolean pkResolved) {
        this.pkResolved = pkResolved;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    public Object getPkNo() {
        return pkNo;
    }

    public void setPkNo(Object pkNo) {
        this.pkNo = pkNo;
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fromRemote(BufferQueryTarget target, Map<String, Object> rv) {
        super.fromRemote(target, rv);
        Object remoteSequenceName = rv.get(DictRemoteKey.SEQUENCE_NAME);
        Object remotePkNo = rv.get(DictRemoteKey.PK_NO);
        Object remoteDictIdGenerator = rv.get(DictRemoteKey.ID_GENERATOR);
        Object remotePkColumnName = rv.get(DictRemoteKey.PK_COLUMN_NAME);
        Object remoteEntity = rv.get(DictRemoteKey.ENTITY);
        Object remoteSetFields = rv.get(DictRemoteKey.SET_FIELDS);
        if (sequenceName != null) {
            setSequenceName((String) remoteSequenceName);
        }
        if (remotePkNo != null) {
            setPkNo(remotePkNo);
        }
        if (remoteDictIdGenerator != null) {
            this.dictIdGenerator = (IdGeneratorType) remoteDictIdGenerator;
        }
        if (remotePkColumnName != null) {
            setPkColumnName((String) remotePkColumnName);
        }
        if (remoteEntity != null) {
            setEntity((E) remoteEntity);
        }
        if (remoteSetFields != null) {
            this.set((SetFields) remoteSetFields);
        }
    }

    @Override
    public Map<String, Object> toRemote(boolean singleResult) {
        Map<String, Object> rv = super.toRemote(singleResult);
        if (sequenceName != null) rv.put(DictRemoteKey.SEQUENCE_NAME, sequenceName);
        if (pkNo != null) rv.put(DictRemoteKey.PK_NO, pkNo);
        if (dictIdGenerator != null) rv.put(DictRemoteKey.ID_GENERATOR, dictIdGenerator);
        if (pkColumnName != null) rv.put(DictRemoteKey.PK_COLUMN_NAME, pkColumnName);
//		if (pkResolved==true)rv.put(DictRemoteKey.ENTITY_NAME,pkResolved);
        if (entity != null) rv.put(DictRemoteKey.ENTITY, entity);
        if (setFields != null) rv.put(DictRemoteKey.SET_FIELDS, setFields);
        return rv;
    }


    public Query<?, ?, ?> getSelectQuery() {
        return selectQuery;
    }

    public void setSelectQuery(Query<?, ?, ?> selectQuery) {
        this.selectQuery = selectQuery;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }

}
