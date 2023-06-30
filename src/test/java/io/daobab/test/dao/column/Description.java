package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Description<E extends Entity> extends EntityRelationMap<E> {


    /**
     * db name: DESCRIPTION,
     * db type: VARCHAR
     */
    default String getDescription() {
        return getColumnParam("Description");
    }

    default E setDescription(String val) {
        return setColumnParam("Description", val);
    }

    default Column<E, String, Description> colDescription() {
        return new Column<E, String, Description>() {

            @Override
            public String getColumnName() {
                return "DESCRIPTION";
            }

            @Override
            public String getFieldName() {
                return "Description";
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
            public String getValue(Description entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Description");
                return entity.getDescription();
            }

            @Override
            public Description setValue(Description entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Description");
                return (Description) entity.setDescription(param);
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
