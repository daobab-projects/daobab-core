package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface MetaColumnSize<E extends EntityMap> extends EntityRelationMap<E> {

    default Integer getColumnSize() {
        return getColumnParam("ColumnSize");
    }

    default E setColumnSize(Integer val) {
        setColumnParam("ColumnSize", val);
        return (E) this;
    }

    default Column<E, Integer, MetaColumnSize> colColumnSize() {
        return new Column<E, Integer, MetaColumnSize>() {

            @Override
            public String getColumnName() {
                return "COLUMN_SIZE";
            }

            @Override
            public String getFieldName() {
                return "ColumnSize";
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
            public Integer getValue(MetaColumnSize entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ColumnSize");
                return entity.getColumnSize();
            }

            @Override
            public void setValue(MetaColumnSize entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ColumnSize");
                entity.setColumnSize(param);
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