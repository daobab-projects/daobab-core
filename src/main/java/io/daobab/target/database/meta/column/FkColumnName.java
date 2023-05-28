package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface FkColumnName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFkColumnName() {
        return readParam("FkColumnName");
    }

    default E setFkColumnName(String val) {
        return storeParam("FkColumnName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, FkColumnName> colFkColumnName() {
        return DaobabCache.getColumn("FkColumnName", "FKCOLUMN_NAME", (Table<?>) this, String.class);
    }


}
