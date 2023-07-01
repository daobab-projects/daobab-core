package io.daobab.test.dao.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface Active<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: ACTIVE,
     * db type: BOOLEAN
     */
    default Boolean getActive() {
        return getColumnParam("Active");
    }

    default E setActive(Boolean val) {
        return setColumnParam("Active", val);
    }

    @SuppressWarnings("unchecked")
    default Column<E, Boolean, Active<E>> colActive() {
        return ColumnCache.INSTANCE.getColumn("Active", "ACTIVE", (Table<?>) this, Boolean.class);
    }

}
