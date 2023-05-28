package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface MetaSchemaName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getSchemaName() {
        return readParam("SchemaName");
    }

    default E setSchemaName(String val) {
        return storeParam("SchemaName", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, MetaSchemaName> colSchemaName() {
        return DaobabCache.getColumn("SchemaName", "SCHEMA_NAME", (Table<?>) this, String.class);
    }

}
