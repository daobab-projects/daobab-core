package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Ascending<E extends EntityMap> extends EntityRelationMap<E> {

    default String getAscending() {
        return getColumnParam("Ascending");
    }

    default E setAscending(String val) {
        setColumnParam("Ascending", val);
        return (E) this;
    }

    default Column<E, String, Ascending> colAscending() {
        return new Column<E, String, Ascending>() {

            @Override
            public String getColumnName() {
                return "Ascending";
            }

            @Override
            public String getFieldName() {
                return "Ascending";
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
            public String getValue(Ascending entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Ascending");
                return entity.getAscending();
            }

            @Override
            public void setValue(Ascending entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Ascending");
                entity.setAscending(param);
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