package io.daobab.model;

import java.util.Collections;
import java.util.List;

/**
 * A placeholder entity standing for "any entity", used internally where a concrete entity type is not known.
 * It has no columns.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@TableInformation(name = "DAOBAB_ANY_ENTITY_SPECIAL_CLASS")
public final class EntityAny extends Table<EntityAny> {

    /**
     * No columns.
     */
    @Override
    public List<TableColumn> columns() {
        return Collections.emptyList();
    }

}
