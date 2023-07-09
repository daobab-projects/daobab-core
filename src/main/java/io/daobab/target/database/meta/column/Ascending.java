package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface Ascending<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getAscending() {
        return readParam("Ascending");
    }

    default E setAscending(String val) {
        return storeParam("Ascending", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, Ascending> colAscending() {
        return ColumnCache.INSTANCE.getColumn("Ascending", "Ascending", (Table<?>) this, String.class);
    }


}
