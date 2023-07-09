package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface TableColumnName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getTableColumnName() {
        return readParam("TableColumnName");
    }

    default E setTableColumnName(String val) {
        return storeParam("TableColumnName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, TableColumnName> colTableColumnName() {
        return ColumnCache.INSTANCE.getColumn("TableColumnName", "TABLE_COLUMN_NAME", (Table<?>) this, String.class);
    }


}
