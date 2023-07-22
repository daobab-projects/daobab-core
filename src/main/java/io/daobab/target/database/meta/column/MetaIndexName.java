package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface MetaIndexName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getIndexName() {
        return readParam("IndexName");
    }

    default E setIndexName(String val) {
        return storeParam("IndexName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, MetaIndexName> colIndexName() {
        return DaobabCache.getColumn("IndexName", "INDEX_NAME", (Table<?>) this, String.class);
    }

}
