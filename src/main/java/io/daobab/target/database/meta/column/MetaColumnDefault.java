package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface MetaColumnDefault<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getColumnDefault() {
        return readParam("ColumnDefault");
    }

    default E setColumnDefault(String val) {
        return storeParam("ColumnDefault", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, MetaColumnDefault> colColumnDefault() {
        return ColumnCache.INSTANCE.getColumn("ColumnDefault", "COLUMN_DEF", (Table<?>) this, String.class);
    }

}
