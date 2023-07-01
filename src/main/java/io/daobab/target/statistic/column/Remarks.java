package io.daobab.target.statistic.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface Remarks<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getRemarks() {
        return readParam("Remarks");
    }

    default E setRemarks(String val) {
        return storeParam("Remarks", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, Remarks> colRemarks() {
        return ColumnCache.INSTANCE.getColumn("Remarks", "REMARKS", (Table<?>) this, String.class);
    }

}
