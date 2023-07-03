package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface MetaColumnName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getColumnName() {
        return readParam("ColumnName");
    }

    @SuppressWarnings("unchecked")
    default E setColumnName(String val) {
        return storeParam("ColumnName", val);
    }

    @SuppressWarnings("rawtypes")
    default Column<E, String, MetaColumnName> colColumnName() {
        return new Column<E, String, MetaColumnName>() {

            @Override
            public String getColumnName() {
                return "COLUMN_NAME";
            }

            @Override
            public String getFieldName() {
                return "ColumnName";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<String> getFieldClass() {
                return String.class;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public String getValue(MetaColumnName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "ColumnName");
                return entity.getColumnName();
            }

            @SuppressWarnings("rawtypes")
            @Override
            public MetaColumnName setValue(MetaColumnName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "ColumnName");
                return (MetaColumnName) entity.setColumnName(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return entityClass().getName() + "." + getFieldName();
            }

            @SuppressWarnings("rawtypes")
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
