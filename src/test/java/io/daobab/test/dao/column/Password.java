package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Password<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: PASSWORD,
     * db type: VARCHAR
     */
    default String getPassword() {
        return readParam("Password");
    }

    default E setPassword(String val) {
        return storeParam("Password", val);
    }

    default Column<E, String, Password> colPassword() {
        return new Column<E, String, Password>() {

            @Override
            public String getColumnName() {
                return "PASSWORD";
            }

            @Override
            public String getFieldName() {
                return "Password";
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
            public String getValue(Password entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Password");
                return entity.getPassword();
            }

            @Override
            public Password setValue(Password entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Password");
                return (Password) entity.setPassword(param);
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
