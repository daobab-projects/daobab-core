package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface Cardinality<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getCardinality() {
        return readParam("Cardinality");
    }

    default E setCardinality(Integer val) {
        return storeParam("Cardinality", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Integer, Cardinality> colCardinality() {
        return ColumnCache.INSTANCE.getColumn("Cardinality", "CARDINALITY", (Table<?>) this, Integer.class);
    }

}
