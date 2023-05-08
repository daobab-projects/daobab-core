package io.daobab.target.buffer.remote;

import io.daobab.error.DaobabException;
import io.daobab.generator.DictRemoteKey;
import io.daobab.model.ResponseWrapper;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.query.*;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.OperationType;

import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public class Endpoint {

    public static ResponseWrapper invoke(BufferQueryTarget exposedTarget, Map<String, Object> query) {
        return invoke(exposedTarget, exposedTarget.getAccessProtector(), query);
    }

    public static ResponseWrapper invoke(BufferQueryTarget exposedTarget, AccessProtector protector, Map<String, Object> query) {
        try {
            ResponseWrapper r = new ResponseWrapper();
            boolean accessProtectorAvailable = protector != null && protector.isEnabled();

            String queryClass = (String) query.get(DictRemoteKey.QUERY_CLASS);
            boolean singleResult = (boolean) query.get(DictRemoteKey.SINGLE_RESULT);

            if (BufferQueryEntity.class.getName().equals(queryClass)) {
                BufferQueryEntity<?> q = new BufferQueryEntity<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.validateEntityAllowedFor(q.getEntityName(), OperationType.READ);
                    protector.removeViolatedInfoColumns3(q.getFields(), OperationType.READ);
                }
                r.setContent(singleResult ? exposedTarget.readEntity(q) : exposedTarget.readEntityList(q));

            } else if (BufferQueryField.class.getName().equals(queryClass)) {
                BufferQueryField<?, ?> q = new BufferQueryField<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.removeViolatedInfoColumns3(q.getFields(), OperationType.READ);
                }
                if (!q.getFields().isEmpty()) {
                    r.setContent(singleResult ? exposedTarget.readField(q) : exposedTarget.readFieldList(q));
                }
            } else if (BufferQueryPlate.class.getName().equals(queryClass)) {
                BufferQueryPlate q = new BufferQueryPlate(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.removeViolatedInfoColumns3(q.getFields(), OperationType.READ);
                }
                if (!q.getFields().isEmpty()) {
                    r.setContent(singleResult ? exposedTarget.readPlate(q) : exposedTarget.readPlateList(q));
                }
            } else if (BufferQueryDelete.class.getName().equals(queryClass)) {
                BufferQueryDelete<?> q = new BufferQueryDelete<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.validateEntityAllowedFor(q.getEntityName(), OperationType.DELETE);
                }
                r.setContent(exposedTarget.delete(q, true));
            } else if (BufferQueryUpdate.class.getName().equals(queryClass)) {
                BufferQueryUpdate<?> q = new BufferQueryUpdate<>(exposedTarget, query);
                if (accessProtectorAvailable) {
                    protector.validateEntityAllowedFor(q.getEntityName(), OperationType.UPDATE);
                }
                r.setContent(exposedTarget.update(q, true));
            } else if (BufferQueryInsert.class.getName().equals(queryClass)) {
                BufferQueryInsert<?> q = new BufferQueryInsert<>(exposedTarget, query);
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
