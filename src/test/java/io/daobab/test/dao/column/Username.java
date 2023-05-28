package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Username<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: USERNAME,
     * db type: VARCHAR
     */
    default String getUsername() {
        return readParam("Username");
    }

    default E setUsername(String val) {
        return storeParam("Username", val);
    }

    default Column<E, String, Username> colUsername() {
        return new Column<E, String, Username>() {

            @Override
            public String getColumnName() {
                return "USERNAME";
            }

            @Override
            public String getFieldName() {
                return "Username";
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
            public String getValue(Username entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Username");
                return entity.getUsername();
            }

            @Override
            public Username setValue(Username entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Username");
                return (Username) entity.setUsername(param);
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
