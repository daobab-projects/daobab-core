package io.daobab.target.protection;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface AccessProtector {
    AccessProtector setEntityAccess(Entity entity, Access... accessRights);

    AccessProtector setEntityAccess(String entityName, Access... accessRights);

    AccessProtector setColumnAccess(Column column, Access... accessRights);

    AccessProtector setEntityAccess(Access accessRights, Entity... entities);

    AccessProtector setEntityAccess(Access accessRights, String... entities);

    AccessProtector setColumnAccess(Access accessRights, Column... columns);

    DefaultAccessStrategy getDefaultStrategy();

    AccessProtector setDefaultStrategy(DefaultAccessStrategy defaultAccessStrategy);

    void validateEntityAllowedFor(Entity entity, OperationType operation);

    void validateEntityAllowedFor(String entityName, OperationType operation);

    List<Column> removeViolatedColumns(List<Column> src, OperationType operation);

    <E extends Entity> List<Column<E, ?, ?>> removeViolatedColumns3(List<Column<E, ?, ?>> src, OperationType operation);

    List<TableColumn> removeViolatedInfoColumns3(List<TableColumn> src, OperationType operation);

    List<Column<Entity, ?, ?>> removeViolatedColumns2(List<Column<Entity, ?, ?>> src, OperationType operation);

    List<TableColumn> removeViolatedInfoColumns(List<TableColumn> src, OperationType operation);

    void validateColumnAllowedFor(Column column, OperationType operation);

    boolean isEntityAllowedFor(Entity entity, OperationType operation);

    boolean isEntityAllowedFor(String entityName, OperationType operation);

    boolean isColumnAllowedFor(Column column, OperationType operation);

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
