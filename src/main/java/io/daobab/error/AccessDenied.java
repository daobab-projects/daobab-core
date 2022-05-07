package io.daobab.error;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.target.protection.OperationType;

public class AccessDenied extends DaobabException {

    public AccessDenied(Entity entity, OperationType operation) {
        this(entity.getEntityName(), operation);
    }

    public AccessDenied(String entityName, OperationType operation) {
        super("Entity " + entityName + " is disallowed for operation: " + operation);
    }

    @SuppressWarnings("rawtypes")
    public AccessDenied(Column column, OperationType operation) {
        super("Column " + column.getEntityName() + "." + column.getColumnName() + " is disallowed for operation: " + operation);
    }
}
