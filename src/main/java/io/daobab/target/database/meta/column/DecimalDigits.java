package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface DecimalDigits<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getDecimalDigits() {
        return readParam("DecimalDigits");
    }

    default E setDecimalDigits(Integer val) {
        return storeParam("DecimalDigits", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Integer, DecimalDigits> colDecimalDigits() {
        return DaobabCache.getColumn("DecimalDigits", "DECIMAL_DIGITS", (Table<?>) this, Integer.class);
    }

}
