package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface MetaColumnName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getColumnName() {
        return getColumnParam("ColumnName");
    }

    default E setColumnName(String val) {
        setColumnParam("ColumnName", val);
        return (E) this;
    }

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

            @Override
            public String getValue(MetaColumnName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ColumnName");
                return entity.getColumnName();
            }

            @Override
            public void setValue(MetaColumnName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ColumnName");
                entity.setColumnName(param);
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
