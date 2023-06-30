package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Username<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: USERNAME,
     * db type: VARCHAR
     */
    default String getUsername() {
        return getColumnParam("Username");
    }

    default E setUsername(String val) {
        return setColumnParam("Username", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Username");
                return entity.getUsername();
            }

            @Override
            public Username setValue(Username entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Username");
                return (Username) entity.setUsername(param);
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
