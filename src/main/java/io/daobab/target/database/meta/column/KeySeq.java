package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface KeySeq<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getKeySeq() {
        return readParam("KeySeq");
    }

    default E setKeySeq(String val) {
        return storeParam("KeySeq", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, KeySeq> colKeySeq() {
        return DaobabCache.getColumn("KeySeq", "KEY_SEQ", (Table<?>) this, String.class);
    }


}
