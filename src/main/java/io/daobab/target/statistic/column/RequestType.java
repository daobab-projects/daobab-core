package io.daobab.target.statistic.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.query.base.QueryType;

public interface RequestType<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default QueryType getRequestType() {
        return readParam("RequestType");
    }

    default E setRequestType(QueryType val) {
        return storeParam("RequestType", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, QueryType, RequestType> colRequestType() {
        return DaobabCache.getColumn("RequestType", "REQUEST_TYPE", (Table<?>) this, QueryType.class);
    }


}
