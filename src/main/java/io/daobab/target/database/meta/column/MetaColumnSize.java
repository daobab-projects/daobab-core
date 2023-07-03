package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface MetaColumnSize<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getColumnSize() {
        return readParam("ColumnSize");
    }

    default E setColumnSize(Integer val) {
        return storeParam("ColumnSize", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "ColumnSize");
                return entity.getColumnSize();
            }

            @Override
            public MetaColumnSize setValue(MetaColumnSize entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "ColumnSize");
                return (MetaColumnSize) entity.setColumnSize(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return entityClass().getName() + "." + getFieldName();
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
