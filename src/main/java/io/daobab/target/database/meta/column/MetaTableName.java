package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface MetaTableName<E extends Entity> extends EntityRelationMap<E> {

    default String getTableName() {
        return getColumnParam("TableName");
    }

    default E setTableName(String val) {
        return setColumnParam("TableName", val);
    }

    default Column<E, String, MetaTableName> colTableName() {
        return new Column<E, String, MetaTableName>() {

            @Override
            public String getColumnName() {
                return "TABLE_NAME";
            }

            @Override
            public String getFieldName() {
                return "TableName";
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
            public String getValue(MetaTableName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "TableName");
                return entity.getTableName();
            }

            @Override
            public MetaTableName setValue(MetaTableName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "TableName");
                return (MetaTableName) entity.setTableName(param);
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
