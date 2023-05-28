package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Phone<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: PHONE,
     * db type: VARCHAR
     */
    default String getPhone() {
        return readParam("Phone");
    }

    default E setPhone(String val) {
        return storeParam("Phone", val);
    }

    default Column<E, String, Phone> colPhone() {
        return new Column<E, String, Phone>() {

            @Override
            public String getColumnName() {
                return "PHONE";
            }

            @Override
            public String getFieldName() {
                return "Phone";
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
            public String getValue(Phone entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Phone");
                return entity.getPhone();
            }

            @Override
            public Phone setValue(Phone entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Phone");
                return (Phone) entity.setPhone(param);
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
