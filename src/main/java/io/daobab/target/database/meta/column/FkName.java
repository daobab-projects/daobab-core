package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface FkName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFkName() {
        return readParam("FkName");
    }

    default E setFkName(String val) {
        return storeParam("FkName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, FkName> colFkName() {
        return DaobabCache.getColumn("FkName", "FK_NAME", (Table<?>) this, String.class);
    }

}
