package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface FkTableName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFkTableName() {
        return readParam("FkTableName");
    }

    default E setFkTableName(String val) {
        return storeParam("FkTableName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, FkTableName> colFkTableName() {
        return ColumnCache.INSTANCE.getColumn("FkTableName", "FKTABLE_NAME", (Table<?>) this, String.class);
    }

}
