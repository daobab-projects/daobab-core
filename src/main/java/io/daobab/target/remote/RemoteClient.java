package io.daobab.target.remote;

import io.daobab.error.RemoteDaobabException;
import io.daobab.error.RemoteReadingException;
import io.daobab.error.RemoteTargetCanNotHandleOpenedTransactionException;
import io.daobab.model.*;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.result.Entities;
import io.daobab.result.EntityList;
import io.daobab.result.PlateBuffer;
import io.daobab.target.BaseTarget;
import io.daobab.target.OpenedTransactionTarget;
import io.daobab.target.QueryTarget;
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
public abstract class RemoteClient extends BaseTarget implements QueryTarget {

    protected Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public boolean isLogQueriesEnabled() {
        return false;
    }


    protected abstract ResponseWrapper callEndpoint(Query<? extends Entity, ?> query, boolean singleResult);

    @Override
    public boolean isBuffer() {
        return false;
    }

    @Override
    public boolean isConnectedToDatabase() {
        return false;
    }

    @Override
    public OpenedTransactionTarget beginTransaction() {
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

    @Override
    public <E extends Entity> Entities<E> readEntityList(QueryEntity<E> query) {
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
            throw new RemoteReadingException(e);
        }
    }

    @Override
    public <E extends Entity> E readEntity(QueryEntity<E> query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }

        try {
            EntityMap rv = (EntityMap) query.getEntityClass().getDeclaredConstructor().newInstance();
            rv.putAll((Map<String, Object>) response.getContent());
            return (E) rv;
        } catch (Exception e) {
            throw new RemoteReadingException(e);
        }
    }

    @Override
    public <E extends Entity, F> F readField(QueryField<E, F> query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return (F) response.getContent();
    }

    @Override
    public <E extends Entity, F> List<F> readFieldList(QueryField<E, F> query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        return (List<F>) response.getContent();
    }

    @Override
    public PlateBuffer readPlateList(QueryPlate query) {
        ResponseWrapper response = callEndpoint(query, false);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        List<Map<String, Map<String, Object>>> listmap = (List<Map<String, Map<String, Object>>>) response.getContent();
        List<Plate> rv = new LinkedList<>();
        try {
            for (Map<String, Map<String, Object>> map : listmap) {
                Plate entity = new Plate();
                entity.putAll(map);
                rv.add(entity);
            }

            return new PlateBuffer(rv);
        } catch (Exception e) {
            throw new RemoteReadingException(e);
        }

    }

    @Override
    public Plate readPlate(QueryPlate query) {
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            Plate rv = new Plate();
            rv.putAll((Map<String, Map<String, Object>>) response.getContent());
            return rv;
        } catch (Exception e) {
            throw new RemoteReadingException(e);
        }
    }


    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, boolean transaction) {
        if (!transaction) throw new RemoteTargetCanNotHandleOpenedTransactionException();
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            return (Integer) response.getContent();
        } catch (Exception e) {
            throw new RemoteReadingException(e);
        }
    }


    @Override
    public <E extends Entity> int update(QueryUpdate<E> query, boolean transaction) {
        if (!transaction) throw new RemoteTargetCanNotHandleOpenedTransactionException();
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            return (Integer) response.getContent();
        } catch (Exception e) {
            throw new RemoteReadingException(e);
        }
    }


    @Override
    public <E extends Entity> E insert(QueryInsert<E> query, boolean transaction) {
        if (!transaction) throw new RemoteTargetCanNotHandleOpenedTransactionException();
        ResponseWrapper response = callEndpoint(query, true);
        if (Integer.parseInt(response.getStatus()) < 0) {
            throw new RemoteDaobabException(response);
        }
        try {
            return (E) response.getContent();
        } catch (Exception e) {
            throw new RemoteReadingException(e);
        }
    }

    @Override
    public <E extends Entity> int delete(QueryDelete<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }


    @Override
    public <E extends Entity> int update(QueryUpdate<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }


    @Override
    public <E extends Entity> E insert(QueryInsert<E> query, Propagation propagation) {
        throw new RemoteTargetCanNotHandleOpenedTransactionException();
    }


    @Override
    public <E extends Entity> String toSqlQuery(Query<E, ?> query) {
        return null;
    }

    @Override
    public <O extends ProcedureParameters, I extends ProcedureParameters> O callProcedure(String name, I in, O out) {
        return null;
    }
}
