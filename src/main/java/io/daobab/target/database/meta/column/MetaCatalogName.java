package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface MetaCatalogName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getCatalogName() {
        return readParam("CatalogName");
    }

    default E setCatalogName(String val) {
        return storeParam("CatalogName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, MetaCatalogName> colCatalogName() {
        return DaobabCache.getColumn("CatalogName", "CAT_NAME", (Table<?>) this, String.class);
    }


}
