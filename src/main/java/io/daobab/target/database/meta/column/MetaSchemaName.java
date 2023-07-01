package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface MetaSchemaName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getSchemaName() {
        return readParam("SchemaName");
    }

    default E setSchemaName(String val) {
        return storeParam("SchemaName", val);
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
            public MetaSchemaName setValue(MetaSchemaName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "SchemaName");
                return (MetaSchemaName) entity.setSchemaName(param);
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
