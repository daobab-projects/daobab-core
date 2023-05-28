package io.daobab.model;

import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.Collections;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@TableInformation(name = "DAOBAB_ANY_ENTITY_SPECIAL_CLASS")
public final class EntityAny extends Table<EntityAny> {

    @Override
    public List<TableColumn> columns() {
        return Collections.emptyList();
    }

    @Override
    public <T extends Target & QueryHandler> void beforeInsert(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void beforeUpdate(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void beforeDelete(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterSelect(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterInsert(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterUpdate(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterDelete(T target) {

    }
}
