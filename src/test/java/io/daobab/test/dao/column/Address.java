package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Address<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: ADDRESS,
     * db type: VARCHAR
     */
    default String getAddress() {
        return readParam("Address");
    }

    default E setAddress(String val) {
        return storeParam("Address", val);
    }

    default Column<E, String, Address> colAddress() {
        return new Column<E, String, Address>() {

            @Override
            public String getColumnName() {
                return "ADDRESS";
            }

            @Override
            public String getFieldName() {
                return "Address";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<String> getFieldClass() {
                return String.class;
            }

            @Override
            public String getValue(Address entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Address");
                return entity.getAddress();
            }

            @Override
            public Address setValue(Address entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Address");
                return (Address) entity.setAddress(param);
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
