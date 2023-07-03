package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface MetaColumnDefault<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getColumnDefault() {
        return readParam("ColumnDefault");
    }

    default E setColumnDefault(String val) {
        return storeParam("ColumnDefault", val);
    }

    default Column<E, String, MetaColumnDefault> colColumnDefault() {
        return new Column<E, String, MetaColumnDefault>() {

            @Override
            public String getColumnName() {
                return "COLUMN_DEF";
            }

            @Override
            public String getFieldName() {
                return "ColumnDefault";
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
            public String getValue(MetaColumnDefault entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "ColumnDefault");
                return entity.getColumnDefault();
            }

            @Override
            public MetaColumnDefault setValue(MetaColumnDefault entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "ColumnDefault");
                return (MetaColumnDefault) entity.setColumnDefault(param);
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
