package io.daobab.target.statistic.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

import java.sql.Timestamp;

public interface RequestDate<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    default Timestamp getRequestDate() {
        return readParam("RequestDate");
    }

    default E setRequestDate(Timestamp val) {
        return storeParam("RequestDate", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Timestamp, RequestDate> colRequestDate() {
        return ColumnCache.INSTANCE.getColumn("RequestDate", "REQUEST_DATE", (Table<?>) this, Timestamp.class);
    }

}
