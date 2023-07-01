package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface FirstName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: FIRST_NAME,
     * db type: VARCHAR
     */
    default String getFirstName() {
        return readParam("FirstName");
    }

    default E setFirstName(String val) {
        return storeParam("FirstName", val);
    }

    default Column<E, String, FirstName> colFirstName() {
        return new Column<E, String, FirstName>() {

            @Override
            public String getColumnName() {
                return "FIRST_NAME";
            }

            @Override
            public String getFieldName() {
                return "FirstName";
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
            public String getValue(FirstName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FirstName");
                return entity.getFirstName();
            }

            @Override
            public FirstName setValue(FirstName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FirstName");
                return (FirstName) entity.setFirstName(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getEntityClass().getName() + "." + getFieldName();
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
