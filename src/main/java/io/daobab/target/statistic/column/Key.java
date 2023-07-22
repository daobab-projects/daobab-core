package io.daobab.target.statistic.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface Key<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getKey() {
        return readParam("Key");
    }

    default E setKey(String val) {
        return storeParam("Key", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, Key> colKey() {
        return DaobabCache.getColumn("Key", "Key", (Table<?>) this, String.class);
    }

}
