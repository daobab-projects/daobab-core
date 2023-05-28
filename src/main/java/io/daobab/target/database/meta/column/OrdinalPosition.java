package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface OrdinalPosition<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getOrdinalPosition() {
        return readParam("OrdinalPosition");
    }

    default E setOrdinalPosition(Integer val) {
        return storeParam("OrdinalPosition", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Integer, OrdinalPosition> colOrdinalPosition() {
        return DaobabCache.getColumn("OrdinalPosition", "ORDINAL_POSITION", (Table<?>) this, Integer.class);
    }

}
