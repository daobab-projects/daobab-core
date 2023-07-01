package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Name<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: NAME,
     * db type: VARCHAR
     */
    default String getName() {
        return readParam("Name");
    }

    default E setName(String val) {
        return storeParam("Name", val);
    }

    default Column<E, String, Name> colName() {
        return new Column<E, String, Name>() {

            @Override
            public String getColumnName() {
                return "NAME";
            }

            @Override
            public String getFieldName() {
                return "Name";
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
            public String getValue(Name entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Name");
                return entity.getName();
            }

            @Override
            public Name setValue(Name entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Name");
                return (Name) entity.setName(param);
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
