package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface TableColumnName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getTableColumnName() {
        return getColumnParam("TableColumnName");
    }

    default E setTableColumnName(String val) {
        setColumnParam("TableColumnName", val);
        return (E) this;
    }

    default Column<E, String, TableColumnName> colTableColumnName() {
        return new Column<E, String, TableColumnName>() {

            @Override
            public String getColumnName() {
                return "TABLE_COLUMN_NAME";
            }

            @Override
            public String getFieldName() {
                return "TableColumnName";
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
            public String getValue(TableColumnName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "TableColumnName");
                return entity.getTableColumnName();
            }

            @Override
            public void setValue(TableColumnName entity, String param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "TableColumnName");
                entity.setTableColumnName(param);
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