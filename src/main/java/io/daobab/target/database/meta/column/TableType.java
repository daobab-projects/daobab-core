package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface TableType<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getTableType() {
        return readParam("TableType");
    }

    default E setTableType(String val) {
        return storeParam("TableType", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, TableType> colTableType() {
        return DaobabCache.getColumn("TableType", "TABLE_TYPE", (Table<?>) this, String.class);
    }

}
