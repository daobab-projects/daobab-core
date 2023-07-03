package io.daobab.error;

import io.daobab.converter.duplicator.duplication.EntityDuplication;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.target.protection.OperationType;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class AccessDenied extends DaobabException {

    public AccessDenied(Entity entity, OperationType operation) {
        this(EntityDuplication.getEntityName(entity.entityClass(), null), operation);
    }

    public AccessDenied(String entityName, OperationType operation) {
        super("Entity " + entityName + " is disallowed for operation: " + operation);
    }

    @SuppressWarnings("rawtypes")
    public AccessDenied(Column column, OperationType operation) {
        super("Column " + EntityDuplication.getEntityName(column.entityClass(), null) + "." + column.getColumnName() + " is disallowed for operation: " + operation);
    }
}
