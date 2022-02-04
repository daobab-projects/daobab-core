package io.daobab.target.database.remote;

import io.daobab.error.DaobabException;
import io.daobab.generator.DictRemoteKey;
import io.daobab.model.ResponseWrapper;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.*;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.OperationType;

import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class Endpoint {

    public static ResponseWrapper invoke(QueryTarget exposedTarget, Map<String, Object> query) {
        return invoke(exposedTarget, exposedTarget.getAccessProtector(), query);
    }

    public static ResponseWrapper invoke(QueryTarget exposedTarget, AccessProtector protector, Map<String, Object> query) {
        try {
            ResponseWrapper r = new ResponseWrapper();
            boolean accessProtectorAvailable = protector != null && protector.isEnabled();

            String queryclass = (String) query.get(DictRemoteKey.QUERY_CLASS);
            boolean singleResult = (boolean) query.get(DictRemoteKey.SINGLE_RESULT);

            if (DataBaseQueryEntity.class.getName().equals(queryclass)) {
                DataBaseQueryEntity<?> q = new DataBaseQueryEntity<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.validateEntityAllowedFor(q.getEntityName(), OperationType.READ);
                    protector.removeViolatedInfoColumns3(q.getFields(), OperationType.READ);
                }
                r.setContent(singleResult ? exposedTarget.readEntity(q) : exposedTarget.readEntityList(q));

            } else if (DataBaseQueryField.class.getName().equals(queryclass)) {
                DataBaseQueryField<?, ?> q = new DataBaseQueryField<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.removeViolatedInfoColumns3(q.getFields(), OperationType.READ);
                }
                if (!q.getFields().isEmpty()) {
                    r.setContent(singleResult ? exposedTarget.readField(q) : exposedTarget.readFieldList(q));
                }
            } else if (DataBaseQueryPlate.class.getName().equals(queryclass)) {
                DataBaseQueryPlate q = new DataBaseQueryPlate(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.removeViolatedInfoColumns3(q.getFields(), OperationType.READ);
                }
                if (!q.getFields().isEmpty()) {
                    r.setContent(singleResult ? exposedTarget.readPlate(q) : exposedTarget.readPlateList(q));
                }
            } else if (DataBaseQueryDelete.class.getName().equals(queryclass)) {
                DataBaseQueryDelete<?> q = new DataBaseQueryDelete<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.validateEntityAllowedFor(q.getEntityName(), OperationType.DELETE);
                }
                r.setContent(exposedTarget.delete(q, true));
            } else if (DataBaseQueryUpdate.class.getName().equals(queryclass)) {
                DataBaseQueryUpdate<?> q = new DataBaseQueryUpdate<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.validateEntityAllowedFor(q.getEntityName(), OperationType.UPDATE);
                }
                r.setContent(exposedTarget.update(q, true));
            } else if (DataBaseQueryInsert.class.getName().equals(queryclass)) {
                DataBaseQueryInsert<?> q = new DataBaseQueryInsert<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.validateEntityAllowedFor(q.getEntityName(), OperationType.INSERT);
                }
                r.setContent(exposedTarget.insert(q, true));
            }

            r.setStatus("0");

            return r;
        } catch (DaobabException e) {
            ResponseWrapper r = new ResponseWrapper();
            r.setStatus("-200");
            r.setContent(e.getStatusDesc());
            r.setException(e);
            return r;
        } catch (Exception e) {
            ResponseWrapper r = new ResponseWrapper();
            r.setStatus("-100");
            r.setException(e);
            return r;
        }
    }


}
