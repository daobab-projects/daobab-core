package io.daobab.target.statistic.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface ProcedureName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getProcedureName() {
        return readParam("ProcedureName");
    }

    default E setProcedureName(String val) {
        return storeParam("ProcedureName", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, String, ProcedureName> colProcedureName() {
        return DaobabCache.getColumn("ProcedureName", "PROCEDURE_NAME", (Table<?>) this, String.class);
    }


}
