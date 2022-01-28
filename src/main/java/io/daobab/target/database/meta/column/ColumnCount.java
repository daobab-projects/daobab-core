package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface ColumnCount<E extends EntityMap> extends EntityRelationMap<E> {

    default Integer getColumnCount() {
        return getColumnParam("ColumnCount");
    }

    default E setColumnCount(Integer val) {
        setColumnParam("ColumnCount", val);
        return (E) this;
    }

    default Column<E, Integer, ColumnCount> colColumnCount() {
        return new Column<E, Integer, ColumnCount>() {

            @Override
            public String getColumnName() {
                return "COLUMN_AMOUNT";
            }

            @Override
            public String getFieldName() {
                return "ColumnCount";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Integer> getFieldClass() {
                return Integer.class;
            }

            @Override
            public Integer getValue(ColumnCount entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ColumnCount");
                return entity.getColumnCount();
            }

            @Override
            public void setValue(ColumnCount entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ColumnCount");
                entity.setColumnCount(param);
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
