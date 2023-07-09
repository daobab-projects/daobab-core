package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
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
        return ColumnCache.INSTANCE.getColumn("PkSchemaName", "PKTABLE_SCHEM", (Table<?>) this, String.class);
    }

}
