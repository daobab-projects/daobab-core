package io.daobab.target.database.meta.column;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;

public interface FieldClass<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Class getFieldClass() {
        return readParam("FieldClass");
    }

    default E setFieldClass(Class val) {
        return storeParam("FieldClass", val);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default Column<E, Class, FieldClass> colFieldClass() {
        return ColumnCache.INSTANCE.getColumn("FieldClass", "DATATYPE", (Table<?>) this, Class.class.getClass());
    }


}
