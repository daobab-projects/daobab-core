package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Address2<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: ADDRESS2,
     * db type: VARCHAR
     */
    default String getAddress2() {
        return getColumnParam("Address2");
    }

    default E setAddress2(String val) {
        return setColumnParam("Address2", val);
    }

    default Column<E, String, Address2> colAddress2() {
        return new Column<E, String, Address2>() {

            @Override
            public String getColumnName() {
                return "ADDRESS2";
            }

            @Override
            public String getFieldName() {
                return "Address2";
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
            public String getValue(Address2 entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Address2");
                return entity.getAddress2();
            }

            @Override
            public Address2 setValue(Address2 entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Address2");
                return (Address2) entity.setAddress2(param);
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
