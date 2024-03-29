package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface PkTableSchemaName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkSchemaName() {
        return readParam("PkSchemaName");
    }

    default E setPkSchemaName(String val) {
        return storeParam("PkSchemaName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, PkTableSchemaName> colPkSchemaName() {
        return DaobabCache.getColumn("PkSchemaName", "PKTABLE_SCHEM", (Table<?>) this, String.class);
    }

}
