package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface ErrorDesc<E extends Entity> extends EntityRelationMap<E> {

    default String getErrorDesc() {
        return getColumnParam("ErrorDesc");
    }

    default E setErrorDesc(String val) {
        return setColumnParam("ErrorDesc", val);
    }

    default Column<E, String, ErrorDesc> colErrorDesc() {
        return new Column<E, String, ErrorDesc>() {

            @Override
            public String getColumnName() {
                return "ErrorDesc";
            }

            @Override
            public String getFieldName() {
                return "ErrorDesc";
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
            public String getValue(ErrorDesc entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ErrorDesc");
                return entity.getErrorDesc();
            }

            @Override
            public ErrorDesc setValue(ErrorDesc entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ErrorDesc");
                return (ErrorDesc) entity.setErrorDesc(param);
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
