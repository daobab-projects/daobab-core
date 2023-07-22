package io.daobab.target.statistic.column;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;

public interface ExecutionTime<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Long getExecutionTime() {
        return readParam("ExecutionTime");
    }

    default E setExecutionTime(Long val) {
        return storeParam("ExecutionTime", val);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Long, ExecutionTime> colExecutionTime() {
        return DaobabCache.getColumn("ExecutionTime", "EXECUTION_TIME", (Table<?>) this, Long.class);
    }


}
