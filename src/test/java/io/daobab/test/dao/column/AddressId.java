package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface AddressId<E extends Entity> extends EntityRelationMap<E> {


    /**
     * db name: ADDRESS_ID,
     * db type: SMALLINT
     */
    default Integer getAddressId() {
        return getColumnParam("AddressId");
    }

    default E setAddressId(Integer val) {
        return setColumnParam("AddressId", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "AddressId");
                return entity.getAddressId();
            }

            @Override
            public AddressId setValue(AddressId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "AddressId");
                return (AddressId) entity.setAddressId(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getEntityName() + "." + getFieldName();
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
