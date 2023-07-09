package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface PkColumnName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkColumnName() {
        return readParam("PkColumnName");
    }

    default E setPkColumnName(String val) {
        return storeParam("PkColumnName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, PkColumnName> colPkColumnName() {
        return ColumnCache.INSTANCE.getColumn("PkColumnName", "PKCOLUMN_NAME", (Table<?>) this, String.class);
    }


}
