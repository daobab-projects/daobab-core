package io.daobab.target.statistic.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;
import io.daobab.target.statistic.dictionary.CallStatus;

public interface Status<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default CallStatus getStatus() {
        return readParam("Status");
    }

    default E setStatus(CallStatus val) {
        return storeParam("Status", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, CallStatus, Status> colStatus() {
        return ColumnCache.INSTANCE.getColumn("Status", "STATUS", (Table<?>) this, CallStatus.class);
    }


}
