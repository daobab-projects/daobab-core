package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface MetaColumnName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getColumnName() {
        return readParam("ColumnName");
    }

    @SuppressWarnings("unchecked")
    default E setColumnName(String val) {
        return storeParam("ColumnName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, MetaColumnName> colColumnName() {
        return DaobabCache.getColumn("ColumnName", "COLUMN_NAME", (Table<?>) this, String.class);
    }


}
