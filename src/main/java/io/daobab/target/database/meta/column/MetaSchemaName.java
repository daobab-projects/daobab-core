package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface MetaSchemaName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getSchemaName() {
        return getColumnParam("SchemaName");
    }

    default E setSchemaName(String val) {
        setColumnParam("SchemaName", val);
        return (E) this;
    }

    default Column<E, String, MetaSchemaName> colSchemaName() {
        return new Column<E, String, MetaSchemaName>() {

            @Override
            public String getColumnName() {
                return "SCHEMA_NAME";
            }

            @Override
            public String getFieldName() {
                return "SchemaName";
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
            public String getValue(MetaSchemaName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "SchemaName");
                return entity.getSchemaName();
            }

            @Override
            public void setValue(MetaSchemaName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "SchemaName");
                entity.setSchemaName(param);
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
