package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface AddressId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: ADDRESS_ID,
     * db type: SMALLINT
     */
    default Integer getAddressId() {
        return readParam("AddressId");
    }

    default E setAddressId(Integer val) {
        return storeParam("AddressId", val);
    }

    default Column<E, Integer, AddressId> colAddressId() {
        return new Column<E, Integer, AddressId>() {

            @Override
            public String getColumnName() {
                return "ADDRESS_ID";
            }

            @Override
            public String getFieldName() {
                return "AddressId";
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
            public Integer getValue(AddressId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "AddressId");
                return entity.getAddressId();
            }

            @Override
            public AddressId setValue(AddressId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "AddressId");
                return (AddressId) entity.setAddressId(param);
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
