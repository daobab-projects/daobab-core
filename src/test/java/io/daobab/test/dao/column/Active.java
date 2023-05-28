package io.daobab.test.dao.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface Active<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: ACTIVE,
     * db type: BOOLEAN
     */
    default Boolean getActive() {
        return readParam("Active");
    }

    default E setActive(Boolean val) {
        return storeParam("Active", val);
    }

    @SuppressWarnings("unchecked")
    default Column<E, Boolean, Active<E>> colActive() {
        return DaobabCache.getColumn("Active", "ACTIVE", (Table<?>) this, Boolean.class);
    }

}
