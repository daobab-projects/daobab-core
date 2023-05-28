package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface FkTableSchemaName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFkSchemaName() {
        return readParam("FkSchemaName");
    }

    default E setFkSchemaName(String val) {
        return storeParam("FkSchemaName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, FkTableSchemaName> colFkSchemaName() {
        return DaobabCache.getColumn("FkSchemaName", "FKTABLE_SCHEM", (Table<?>) this, String.class);
    }

}
