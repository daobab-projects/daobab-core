package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface MetaIndexType<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getIndexType() {
        return readParam("IndexType");
    }

    default E setIndexType(String val) {
        return storeParam("IndexType", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, MetaIndexType> colIndexType() {
        return DaobabCache.getColumn("IndexType", "INDEX_TYPE", (Table<?>) this, String.class);
    }

}
