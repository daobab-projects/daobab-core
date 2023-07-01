package io.daobab.target.protection;

import io.daobab.error.AccessDenied;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;
import io.daobab.target.Target;
import io.daobab.target.database.QueryTarget;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class BasicAccessProtector implements AccessProtector {

    private final Map<String, Set<Access>> entityRights = new HashMap<>();
    @SuppressWarnings("rawtypes")
    private final Map<Column, Set<Access>> columnRights = new HashMap<>();
    private DefaultAccessStrategy defaultAccessStrategy = DefaultAccessStrategy.ALLOW;
    private boolean enabled = false;

    private final Target target;

    public BasicAccessProtector(Target target) {
        this.target = target;
    }

    @Override
    public AccessProtector setEntityAccess(Entity entity, Access... accessRights) {
        Set<Access> ar = new HashSet<>();
        Collections.addAll(ar, accessRights);
        entityRights.put(target.getEntityName(entity.entityClass()), ar);
        setEnabled(true);
        return this;
    }

    @Override
    public AccessProtector setEntityAccess(String entityName, Access... accessRights) {
        Set<Access> ar = new HashSet<>();
        Collections.addAll(ar, accessRights);
        entityRights.put(entityName, ar);
        setEnabled(true);
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AccessProtector setColumnAccess(Column column, Access... accessRights) {
        Set<Access> ar = new HashSet<>();
        Collections.addAll(ar, accessRights);
        columnRights.put(column, ar);
        setEnabled(true);
        return this;
    }

    @Override
    public AccessProtector setEntityAccess(QueryTarget target, Access accessRights, Entity... entities) {
        if (accessRights == null || entities == null) return this;
        for (Entity entity : entities) {
            Set<Access> ar = entityRights.getOrDefault(target.getEntityName(entity.entityClass()), new HashSet<>());
            Collections.addAll(ar, accessRights);
            entityRights.put(target.getEntityName(entity.entityClass()), ar);
        }
        setEnabled(true);
        return this;
    }

    @Override
    public AccessProtector setEntityAccess(Access accessRights, String... entities) {
        if (accessRights == null || entities == null) return this;
        for (String entityName : entities) {
            Set<Access> ar = entityRights.getOrDefault(entityName, new HashSet<>());
            Collections.addAll(ar, accessRights);
            entityRights.put(entityName, ar);
        }
        setEnabled(true);
        return this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AccessProtector setColumnAccess(Access accessRights, Column... columns) {
        if (accessRights == null || columns == null) return this;
        for (Column column : columns) {
            Set<Access> ar = columnRights.getOrDefault(column, new HashSet<>());
            Collections.addAll(ar, accessRights);
            columnRights.put(column, ar);
        }

        setEnabled(true);
        return this;
    }

    @Override
    public DefaultAccessStrategy getDefaultStrategy() {
        return defaultAccessStrategy;
    }

    @Override
    public AccessProtector setDefaultStrategy(DefaultAccessStrategy defaultAccessStrategy) {
        setEnabled(true);
        this.defaultAccessStrategy = defaultAccessStrategy;
        return this;
    }

    @Override
    public void validateEntityAllowedFor(Entity entity, OperationType operation) {
        if (isEnabled() && !isEntityAllowedFor(entity, operation)) {
            throw new AccessDenied(entity, operation);
        }
    }

    @Override
    public void validateEntityAllowedFor(String entityName, OperationType operation) {
        if (isEnabled() && !isEntityAllowedFor(entityName, operation)) {
            throw new AccessDenied(entityName, operation);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Column> removeViolatedColumns(List<Column> src, OperationType operation) {
        if (!isEnabled()) return src;
        src.removeIf(column -> !isColumnAllowedFor(column, operation));
        return src;
    }

    @Override
    public <E extends Entity> List<Column<E, ?, ?>> removeViolatedColumns3(List<Column<E, ?, ?>> src, OperationType operation) {
        if (!isEnabled()) return src;
        src.removeIf(column -> !isColumnAllowedFor(column, operation));
        return src;
    }

    @Override
    public List<TableColumn> removeViolatedInfoColumns3(List<TableColumn> src, OperationType operation) {
        if (!isEnabled()) return src;
        src.removeIf(column -> !isColumnAllowedFor(column.getColumn(), operation));
        return src;
    }

    @Override
    public List<Column<Entity, ?, ?>> removeViolatedColumns2(List<Column<Entity, ?, ?>> src, OperationType operation) {
        if (!isEnabled()) return src;
        src.removeIf(entityColumn -> !isColumnAllowedFor(entityColumn, operation));
        return src;
    }

    @Override
    public List<TableColumn> removeViolatedInfoColumns(List<TableColumn> src, OperationType operation) {
        if (!isEnabled()) return src;
        src.removeIf(entityColumn -> !isColumnAllowedFor(entityColumn.getColumn(), operation));
        return src;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void validateColumnAllowedFor(Column column, OperationType operation) {
        if (isEnabled() && !isColumnAllowedFor(column, operation)) {
            throw new AccessDenied(column, operation);
        }
    }

    @Override
    public boolean isEntityAllowedFor(Entity entity, OperationType operation) {
        return isEntityAllowedFor(target.getEntityName(entity.entityClass()), operation);
    }

    @Override
    public boolean isEntityAllowedFor(String entityName, OperationType operation) {
        if (!isEnabled()) return true;
        return OperationType.isAllowed(operation, getDefaultStrategy(), entityRights.get(entityName));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isColumnAllowedFor(Column column, OperationType operation) {
        if (!isEnabled()) return true;
        Set<Access> rights = columnRights.get(column);
        boolean entityIsAllowed = isEntityAllowedFor(target.getEntityName(column.getEntityClass()), operation);
        if (!entityIsAllowed) {
            return false;
        }
        boolean columnIsSpecified = rights != null;

        if (!columnIsSpecified) {
            return DefaultAccessStrategy.ALLOW.equals(getDefaultStrategy());
        }
        return OperationType.isAllowed(operation, getDefaultStrategy(), rights);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
