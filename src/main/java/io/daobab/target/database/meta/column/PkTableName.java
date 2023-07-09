package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface PkTableName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkTableName() {
        return readParam("PkTableName");
    }

    default E setPkTableName(String val) {
        return storeParam("PkTableName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, PkTableName> colPkTableName() {
        return ColumnCache.INSTANCE.getColumn("PkTableName", "PKTABLE_NAME", (Table<?>) this, String.class);
    }


}
