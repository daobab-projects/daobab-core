package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface FkColumnName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getFkColumnName() {
        return getColumnParam("FkColumnName");
    }

    default E setFkColumnName(String val) {
        setColumnParam("FkColumnName", val);
        return (E) this;
    }

    default Column<E, String, FkColumnName> colFkColumnName() {
        return new Column<E, String, FkColumnName>() {

            @Override
            public String getColumnName() {
                return "FKCOLUMN_NAME";
            }

            @Override
            public String getFieldName() {
                return "FkColumnName";
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
            public String getValue(FkColumnName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FkColumnName");
                return entity.getFkColumnName();
            }

            @Override
            public void setValue(FkColumnName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FkColumnName");
                entity.setFkColumnName(param);
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
