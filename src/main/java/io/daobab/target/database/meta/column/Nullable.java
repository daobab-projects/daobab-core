package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface Nullable<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Boolean getNullable() {
        return readParam("Nullable");
    }

    default E setNullable(Boolean val) {
        return storeParam("Nullable", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Boolean, Nullable> colNullable() {
        return DaobabCache.getColumn("Nullable", "NULLABLE", (Table<?>) this, Boolean.class);
    }

}
