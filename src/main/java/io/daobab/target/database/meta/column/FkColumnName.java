package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
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
        return ColumnCache.INSTANCE.getColumn("FkColumnName", "FKCOLUMN_NAME", (Table<?>) this, String.class);
    }


}
