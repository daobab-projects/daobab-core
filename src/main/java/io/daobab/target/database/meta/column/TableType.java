package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface TableType<E extends EntityMap> extends EntityRelationMap<E> {

    default String getTableType() {
        return getColumnParam("TableType");
    }

    default E setTableType(String val) {
        setColumnParam("TableType", val);
        return (E) this;
    }

    default Column<E, String, TableType> colTableType() {
        return new Column<E, String, TableType>() {

            @Override
            public String getColumnName() {
                return "TABLE_TYPE";
            }

            @Override
            public String getFieldName() {
                return "TableType";
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
            public String getValue(TableType entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "TableType");
                return entity.getTableType();
            }

            @Override
            public void setValue(TableType entity, String param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "TableType");
                entity.setTableType(param);
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
