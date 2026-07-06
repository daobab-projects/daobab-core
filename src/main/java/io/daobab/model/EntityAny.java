package io.daobab.model;

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

}
