package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Unique<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Boolean getUnique() {
        return readParam("Unique");
    }

    default E setUnique(Boolean val) {
        return storeParam("Unique", val);
    }

    default Column<E, Boolean, Unique> colUnique() {
        return new Column<E, Boolean, Unique>() {

            @Override
            public String getColumnName() {
                return "Unique";
            }

            @Override
            public String getFieldName() {
                return "Unique";
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
            public Boolean getValue(Unique entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Unique");
                return entity.getUnique();
            }

            @Override
            public Unique setValue(Unique entity, Boolean param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Unique");
                return (Unique) entity.setUnique(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getEntityClass().getName() + "." + getFieldName();
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
