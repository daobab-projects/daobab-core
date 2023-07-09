package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface Unique<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Boolean getUnique() {
        return readParam("Unique");
    }

    default E setUnique(Boolean val) {
        return storeParam("Unique", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Boolean, Unique> colUnique() {
        return ColumnCache.INSTANCE.getColumn("Unique", "Unique", (Table<?>) this, Boolean.class);
    }


}
