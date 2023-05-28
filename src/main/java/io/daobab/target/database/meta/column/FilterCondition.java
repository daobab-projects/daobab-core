package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface FilterCondition<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFilterCondition() {
        return readParam("FilterCondition");
    }

    default E setFilterCondition(String val) {
        return storeParam("FilterCondition", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, FilterCondition> colFilterCondition() {
        return DaobabCache.getColumn("FilterCondition", "FILTER_CONDITION", (Table<?>) this, String.class);
    }


}
