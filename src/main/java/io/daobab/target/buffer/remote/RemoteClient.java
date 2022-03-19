package io.daobab.target.buffer.remote;

import io.daobab.error.ReadRemoteException;
import io.daobab.error.RemoteDaobabException;
import io.daobab.error.RemoteTargetCanNotHandleOpenedTransactionException;
import io.daobab.model.Entity;
import io.daobab.model.EntityMap;
import io.daobab.model.Plate;
import io.daobab.model.ResponseWrapper;
import io.daobab.query.base.Query;
import io.daobab.target.BaseTarget;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.query.*;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.buffer.transaction.OpenedTransactionBufferTarget;
import io.daobab.transaction.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public abstract class RemoteClient extends BaseTarget implements BufferQueryTarget {

    protected Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public boolean isLogQueriesEnabled() {
        return false;
    }

    protected abstract ResponseWrapper callEndpoint(Query<? extends Entity, ?, ?> query, boolean singleResult);

    @Override
    public OpenedTransactionBufferTarget beginTransaction() {
        return null;
    }

    @Override
    public List<Entity> getTables() {
        return new LinkedList<>();
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> Entities<E> readEntityList(BufferQueryEntity<E> query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }

        List<Map<String, Object>> listmap = (List<Map<String, Object>>) response.getContent();
        List<E> rv = new ArrayList<>();
        try {
            for (Map<String, Object> map : listmap) {
                EntityMap entity = (EntityMap) query.getEntityClass().getDeclaredConstructor().newInstance();
                entity.putAll(map);
                rv.add((E) entity);
            }

            return new EntityList<>(rv, query.getEntityClass());
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> E readEntity(BufferQueryEntity<E> query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }

        try {
            EntityMap rv = (EntityMap) query.getEntityClass().getDeclaredConstructor().newInstance();
            rv.putAll((Map<String, Object>) response.getContent());
            return (E) rv;
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity, F> F readField(BufferQueryField<E, F> query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return (F) response.getContent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity, F> List<F> readFieldList(BufferQueryField<E, F> query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return (List<F>) response.getContent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlateBuffer readPlateList(BufferQueryPlate query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        List<Map<String, Map<String, Object>>> listMap = (List<Map<String, Map<String, Object>>>) response.getContent();
        List<Plate> rv = new LinkedList<>();
        try {
            for (Map<String, Map<String, Object>> map : listMap) {
                Plate entity = new Plate();
                entity.putAll(map);
                rv.add(entity);
            }

            return new PlateBuffer(rv);
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Plate readPlate(BufferQueryPlate query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            Plate rv = new Plate();
            rv.putAll((Map<String, Map<String, Object>>) response.getContent());
            return rv;
        } catch (Exception e) {
            throw new ReadRemoteException(e);
        }
    }

    @Override
    public <E extends Entity> int delete(BufferQueryDelete<E> query, boolean transaction) {
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
    public <E extends Entity> int update(BufferQueryUpdate<E> query, boolean transaction) {
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
    public <E extends Entity> E insert(BufferQueryInsert<E> query, boolean transaction) {
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
    public <E extends Entity> int delete(BufferQueryDelete<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public <E extends Entity> int update(BufferQueryUpdate<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

    @Override
    public <E extends Entity> E insert(BufferQueryInsert<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }

}
