package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface FkTableSchemaName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getFkSchemaName() {
        return getColumnParam("FkSchemaName");
    }

    default E setFkSchemaName(String val) {
        setColumnParam("FkSchemaName", val);
        return (E) this;
    }

    default Column<E, String, FkTableSchemaName> colFkSchemaName() {
        return new Column<E, String, FkTableSchemaName>() {

            @Override
            public String getColumnName() {
                return "FKTABLE_SCHEM";
            }

            @Override
            public String getFieldName() {
                return "FkSchemaName";
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
            public String getValue(FkTableSchemaName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FkSchemaName");
                return entity.getFkSchemaName();
            }

            @Override
            public void setValue(FkTableSchemaName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FkSchemaName");
                entity.setFkSchemaName(param);
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