package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Address<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: ADDRESS,
     * db type: VARCHAR
     */
    default String getAddress() {
        return getColumnParam("Address");
    }

    default E setAddress(String val) {
        setColumnParam("Address", val);
        return (E) this;
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Address");
                return entity.getAddress();
            }

            @Override
            public void setValue(Address entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Address");
                entity.setAddress(param);
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