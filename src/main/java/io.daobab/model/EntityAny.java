package io.daobab.model;

import java.util.Collections;
import java.util.List;


public final class EntityAny implements Entity {

    @Override
    public String getEntityName() {
        return "DAOBAB_ANY_ENTITY_SPECIAL_CLASS";
    }


    @Override
    public List<TableColumn> columns() {
        return Collections.emptyList();
    }
}
