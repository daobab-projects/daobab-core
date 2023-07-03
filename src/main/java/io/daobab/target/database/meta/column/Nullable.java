package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Nullable<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Boolean getNullable() {
        return readParam("Nullable");
    }

    default E setNullable(Boolean val) {
        return storeParam("Nullable", val);
    }

    default Column<E, Boolean, Nullable> colNullable() {
        return new Column<E, Boolean, Nullable>() {

            @Override
            public String getColumnName() {
                return "NULLABLE";
            }

            @Override
            public String getFieldName() {
                return "Nullable";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Boolean> getFieldClass() {
                return Boolean.class;
            }

            @Override
            public Boolean getValue(Nullable entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Nullable");
                return entity.getNullable();
            }

            @Override
            public Nullable setValue(Nullable entity, Boolean param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Nullable");
                return (Nullable) entity.setNullable(param);
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
