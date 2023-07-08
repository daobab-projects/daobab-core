package io.daobab.target.database.remote;

import io.daobab.creation.EntityCreator;
import io.daobab.error.ReadRemoteException;
import io.daobab.error.RemoteDaobabException;
import io.daobab.error.RemoteTargetCanNotHandleOpenedTransactionException;
import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.target.BaseTarget;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.single.Plates;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.converter.dateformat.DatabaseDateConverter;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.query.*;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryEntity;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryField;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryPlate;
import io.daobab.target.database.transaction.OpenTransactionDataBaseTargetImpl;
import io.daobab.transaction.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class RemoteDatabaseClient extends BaseTarget implements QueryTarget {

    protected Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public boolean getShowSql() {
        return false;
    }

    protected abstract ResponseWrapper callEndpoint(Query<? extends Entity, ?, ?> query, boolean singleResult);

    @Override
    public OpenTransactionDataBaseTargetImpl beginTransaction() {
        return null;
    }

    @Override
    public List<Entity> getTables() {
        return new ArrayList<>();
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> Entities<E> readEntityList(DataBaseQueryEntity<E> query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return EntityCreator.createEntityListFromJson(query.getEntityClass(), (String) response.getContent());

    }

    @Override
    public <E extends Entity> Entities<E> readEntityList(FrozenDataBaseQueryEntity<E> query, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConvertersArr) {
        return null;
    }

    @Override
    public <E extends Entity> E readEntity(DataBaseQueryEntity<E> query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return EntityCreator.createEntityFromJson(query.getEntityClass(), (String) response.getContent());
    }

    @Override
    public <E extends Entity> E readEntity(FrozenDataBaseQueryEntity<E> query, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConvertersArr) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity, F> F readField(DataBaseQueryField<E, F> query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return (F) response.getContent();
    }

    @Override
    public <E extends Entity, F> F readField(FrozenDataBaseQueryField<E, F> query, List<Object> parameters, Column<?, ?, ?> column, DatabaseTypeConverter<?, ?> typeConverter) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity, F> List<F> readFieldList(DataBaseQueryField<E, F> query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return (List<F>) response.getContent();
    }

    @Override
    public <E extends Entity, F> List<F> readFieldList(FrozenDataBaseQueryField<E, F> query, List<Object> parameters, Column<?, ?, ?> column, DatabaseTypeConverter<?, ?> typeConverter) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlateBuffer readPlateList(DataBaseQueryPlate query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        List<Map<String, Map<String, Object>>> listMap = (List<Map<String, Map<String, Object>>>) response.getContent();
        List<Plate> rv = new ArrayList<>();
        try {
            for (Map<String, Map<String, Object>> map : listMap) {
                Plate entity = new Plate(query.getFields());
                entity.putAll(map);
                rv.add(entity);
            }

            return new PlateBuffer(rv);
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @Override
    public Plates readPlateList(FrozenDataBaseQueryPlate query, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConverters) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Plate readPlate(DataBaseQueryPlate query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            Plate rv = new Plate(query.getFields());
            rv.putAll((Map<String, Map<String, Object>>) response.getContent());
            return rv;
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @Override
    public Plate readPlate(FrozenDataBaseQueryPlate query, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConverters) {
        return null;
    }

    @Override
    public <E extends Entity> int delete(DataBaseQueryDelete<E> query, boolean transaction) {
        if (!transaction) throw new RemoteTargetCanNotHandleOpenedTransactionException();
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            return (Integer) response.getContent();
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @Override
    public <E extends Entity> int update(DataBaseQueryUpdate<E> query, boolean transaction) {
        if (!transaction) throw new RemoteTargetCanNotHandleOpenedTransactionException();
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            return (Integer) response.getContent();
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> E insert(DataBaseQueryInsert<E> query, boolean transaction) {
        if (!transaction) throw new RemoteTargetCanNotHandleOpenedTransactionException();
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            return (E) response.getContent();
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @Override
    public <E extends Entity> int delete(DataBaseQueryDelete<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public <E extends Entity> int update(DataBaseQueryUpdate<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public <E extends Entity> E insert(DataBaseQueryInsert<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public <O extends ProcedureParameters, I extends ProcedureParameters> O callProcedure(String name, I in, O out) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> query) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> query, IdentifierStorage identifierStorage) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public DatabaseDateConverter getDatabaseDateConverter() {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }
}
