package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface CamelName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getCamelName() {
        return readParam("CamelName");
    }

    default E setCamelName(String val) {
        return storeParam("CamelName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, CamelName> colCamelName() {
        return ColumnCache.INSTANCE.getColumn("CamelName", "CAMEL_NAME", (Table<?>) this, String.class);

    }

}
