package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Ascending<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getAscending() {
        return readParam("Ascending");
    }

    default E setAscending(String val) {
        return storeParam("Ascending", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Ascending");
                return entity.getAscending();
            }

            @Override
            public Ascending setValue(Ascending entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Ascending");
                return (Ascending) entity.setAscending(param);
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
