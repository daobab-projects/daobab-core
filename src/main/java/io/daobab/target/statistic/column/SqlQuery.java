package io.daobab.target.statistic.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface SqlQuery<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getSqlQuery() {
        return readParam("SqlQuery");
    }

    default E setSqlQuery(String val) {
        return storeParam("SqlQuery", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, SqlQuery> colSqlQuery() {
        return DaobabCache.getColumn("SqlQuery", "SqlQuery", (Table<?>) this, String.class);
    }


}
