package io.daobab.target.database.meta.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.target.database.meta.column.dict.MetaRule;

public interface DeleteRule<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default MetaRule getDeleteRule() {
        return readParam("DeleteRule");
    }

    default E setDeleteRule(MetaRule val) {
        return storeParam("DeleteRule", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, MetaRule, DeleteRule> colDeleteRule() {
        return DaobabCache.getColumn("DeleteRule", "DELETE_RULE", (Table<?>) this, MetaRule.class);
    }

}
