package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface ColumnCount<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getColumnCount() {
        return readParam("ColumnCount");
    }

    default E setColumnCount(Integer val) {
        return storeParam("ColumnCount", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Integer, ColumnCount> colColumnCount() {
        return DaobabCache.getColumn("ColumnCount", "COLUMN_AMOUNT", (Table<?>) this, Integer.class);
    }

}
