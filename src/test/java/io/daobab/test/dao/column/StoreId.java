package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface StoreId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: STORE_ID,
     * db type: TINYINT
     */
    default Integer getStoreId() {
        return readParam("StoreId");
    }

    default E setStoreId(Integer val) {
        return storeParam("StoreId", val);
    }

    default Column<E, Integer, StoreId> colStoreId() {
        return new Column<E, Integer, StoreId>() {

            @Override
            public String getColumnName() {
                return "STORE_ID";
            }

            @Override
            public String getFieldName() {
                return "StoreId";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Integer> getFieldClass() {
                return Integer.class;
            }

            @Override
            public Integer getValue(StoreId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "StoreId");
                return entity.getStoreId();
            }

            @Override
            public StoreId setValue(StoreId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "StoreId");
                return (StoreId) entity.setStoreId(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return entityClass().getName() + "." + getFieldName();
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null) return false;
                if (getClass() != obj.getClass()) return false;
                Column other = (Column) obj;
                return Objects.equals(hashCode(), other.hashCode());
            }
        };
    }

}
