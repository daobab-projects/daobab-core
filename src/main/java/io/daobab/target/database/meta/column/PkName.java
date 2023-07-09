package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface PkName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkName() {
        return readParam("PkName");
    }

    default E setPkName(String val) {
        return storeParam("PkName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, PkName> colPkName() {
        return ColumnCache.INSTANCE.getColumn("PkName", "PK_NAME", (Table<?>) this, String.class);
    }


}
