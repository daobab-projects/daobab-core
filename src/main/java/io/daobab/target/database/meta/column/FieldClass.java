package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface FieldClass<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Class getFieldClass() {
        return readParam("FieldClass");
    }

    default E setFieldClass(Class val) {
        return storeParam("FieldClass", val);
    }

    default Column<E, Class, FieldClass> colFieldClass() {
        return new Column<E, Class, FieldClass>() {

            @Override
            public String getColumnName() {
                return "DATATYPE";
            }

            @Override
            public String getFieldName() {
                return "FieldClass";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class getFieldClass() {
                return Class.class.getClass();
            }

            @Override
            public Class<?> getValue(FieldClass entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "FieldClass");
                return entity.getFieldClass();
            }

            @Override
            public FieldClass setValue(FieldClass entity, Class param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "FieldClass");
                return (FieldClass) entity.setFieldClass(param);
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
