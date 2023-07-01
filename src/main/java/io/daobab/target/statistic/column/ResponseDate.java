package io.daobab.target.statistic.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

import java.sql.Timestamp;

public interface ResponseDate<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: PAYMENT_DATE,
     * db type: TIMESTAMP
     */
    default Timestamp getResponseDate() {
        return readParam("ResponseDate");
    }

    default E setResponseDate(Timestamp val) {
        return storeParam("ResponseDate", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Timestamp, ResponseDate> colResponseDate() {
        return ColumnCache.INSTANCE.getColumn("ResponseDate", "SEND_DATE", (Table<?>) this, Timestamp.class);
    }


}
