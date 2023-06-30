package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Active<E extends Entity> extends EntityRelationMap<E> {


    /**
     * db name: ACTIVE,
     * db type: BOOLEAN
     */
    default Boolean getActive() {
        return getColumnParam("Active");
    }

    default E setActive(Boolean val) {
        return setColumnParam("Active", val);
    }

    default Column<E, Boolean, Active> colActive() {
        return new Column<E, Boolean, Active>() {

            @Override
            public String getColumnName() {
                return "ACTIVE";
            }

            @Override
            public String getFieldName() {
                return "Active";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Boolean> getFieldClass() {
                return Boolean.class;
            }

            @Override
            public Boolean getValue(Active entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Active");
                return entity.getActive();
            }

            @Override
            public Active setValue(Active entity, Boolean param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Active");
                return (Active) entity.setActive(param);
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
