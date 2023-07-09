package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface PkTableCatalogName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkCatalogName() {
        return readParam("PkTableCatalogName");
    }

    default E setPkCatalogName(String val) {
        return storeParam("PkTableCatalogName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, PkTableCatalogName> colPkCatalogName() {
        return ColumnCache.INSTANCE.getColumn("PkTableCatalogName", "PKTABLE_CAT", (Table<?>) this, String.class);
    }

}
