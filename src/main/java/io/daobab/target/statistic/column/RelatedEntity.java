package io.daobab.target.statistic.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface RelatedEntity<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getRelatedEntity() {
        return readParam("RelatedEntity");
    }

    default E setRelatedEntity(String val) {
        return storeParam("RelatedEntity", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, RelatedEntity> colRelatedEntity() {
        return ColumnCache.INSTANCE.getColumn("RelatedEntity", "RELATED_ENTITY", (Table<?>) this, String.class);
    }


}
