package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface PkTableSchemaName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getPkSchemaName() {
        return getColumnParam("PkSchemaName");
    }

    default E setPkSchemaName(String val) {
        setColumnParam("PkSchemaName", val);
        return (E) this;
    }

    default Column<E, String, PkTableSchemaName> colPkSchemaName() {
        return new Column<E, String, PkTableSchemaName>() {

            @Override
            public String getColumnName() {
                return "PKTABLE_SCHEM";
            }

            @Override
            public String getFieldName() {
                return "PkSchemaName";
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
            public String getValue(PkTableSchemaName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PkSchemaName");
                return entity.getPkSchemaName();
            }

            @Override
            public void setValue(PkTableSchemaName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PkSchemaName");
                entity.setPkSchemaName(param);
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
