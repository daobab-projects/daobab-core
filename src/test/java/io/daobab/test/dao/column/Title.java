package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Title<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: TITLE,
     * db type: VARCHAR
     */
    default String getTitle() {
        return getColumnParam("Title");
    }

    default E setTitle(String val) {
        return setColumnParam("Title", val);
    }

    default Column<E, String, Title> colTitle() {
        return new Column<E, String, Title>() {

            @Override
            public String getColumnName() {
                return "TITLE";
            }

            @Override
            public String getFieldName() {
                return "Title";
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
            public String getValue(Title entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Title");
                return entity.getTitle();
            }

            @Override
            public Title setValue(Title entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Title");
                return (Title) entity.setTitle(param);
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
