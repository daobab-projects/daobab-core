package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface FkTableCatalogName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFkCatalogName() {
        return readParam("FkCatalogName");
    }

    default E setFkCatalogName(String val) {
        return storeParam("FkCatalogName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, FkTableCatalogName> colFkCatalogName() {
        return DaobabCache.getColumn("FkCatalogName", "FKTABLE_CAT", (Table<?>) this, String.class);
    }

}
