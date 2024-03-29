package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface MetaTableName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getTableName() {
        return readParam("TableName");
    }

    default E setTableName(String val) {
        return storeParam("TableName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, MetaTableName> colTableName() {
        return DaobabCache.getColumn("TableName", "TABLE_NAME", (Table<?>) this, String.class);
    }


}
