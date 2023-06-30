package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface PkColumnName<E extends Entity> extends EntityRelationMap<E> {

    default String getPkColumnName() {
        return getColumnParam("PkColumnName");
    }

    default E setPkColumnName(String val) {
        return setColumnParam("PkColumnName", val);
    }

    default Column<E, String, PkColumnName> colPkColumnName() {
        return new Column<E, String, PkColumnName>() {

            @Override
            public String getColumnName() {
                return "PKCOLUMN_NAME";
            }

            @Override
            public String getFieldName() {
                return "PkColumnName";
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
            public String getValue(PkColumnName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PkColumnName");
                return entity.getPkColumnName();
            }

            @Override
            public PkColumnName setValue(PkColumnName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PkColumnName");
                return (PkColumnName) entity.setPkColumnName(param);
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
