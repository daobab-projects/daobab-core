package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.target.database.connection.JdbcType;

public interface Datatype<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default JdbcType getDatatype() {
        return readParam("Datatype");
    }

    default E setDatatype(JdbcType val) {
        return storeParam("Datatype", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, JdbcType, Datatype> colDatatype() {
        return DaobabCache.getColumn("Datatype", "DATATYPE", (Table<?>) this, JdbcType.class);
    }

}
