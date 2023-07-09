package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;
import io.daobab.target.database.meta.column.dict.MetaRule;

public interface UpdateRule<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default MetaRule getUpdateRule() {
        return readParam("UpdateRule");
    }

    default E setUpdateRule(MetaRule val) {
        return storeParam("UpdateRule", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, MetaRule, UpdateRule> colUpdateRule() {
        return ColumnCache.INSTANCE.getColumn("UpdateRule", "UPDATE_RULE", (Table<?>) this, MetaRule.class);
    }


}
