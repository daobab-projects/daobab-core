package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface LastName<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: LAST_NAME,
     * db type: VARCHAR
     */
    default String getLastName() {
        return getColumnParam("LastName");
    }

    default E setLastName(String val) {
        setColumnParam("LastName", val);
        return (E) this;
    }

    default Column<E, String, LastName> colLastName() {
        return new Column<E, String, LastName>() {

            @Override
            public String getColumnName() {
                return "LAST_NAME";
            }

            @Override
            public String getFieldName() {
                return "LastName";
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
            public String getValue(LastName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "LastName");
                return entity.getLastName();
            }

            @Override
            public void setValue(LastName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "LastName");
                entity.setLastName(param);
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