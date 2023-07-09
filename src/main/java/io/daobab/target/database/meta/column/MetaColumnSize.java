package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface MetaColumnSize<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getColumnSize() {
        return readParam("ColumnSize");
    }

    default E setColumnSize(Integer val) {
        return storeParam("ColumnSize", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Integer, MetaColumnSize> colColumnSize() {
        return ColumnCache.INSTANCE.getColumn("ColumnSize", "COLUMN_SIZE", (Table<?>) this, Integer.class);
    }

}
