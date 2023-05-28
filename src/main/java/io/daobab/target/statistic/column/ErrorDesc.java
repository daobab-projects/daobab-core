package io.daobab.target.statistic.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface ErrorDesc<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getErrorDesc() {
        return readParam("ErrorDesc");
    }

    default E setErrorDesc(String val) {
        return storeParam("ErrorDesc", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, ErrorDesc> colErrorDesc() {
        return DaobabCache.getColumn("ErrorDesc", "ErrorDesc", (Table<?>) this, String.class);
    }


}
