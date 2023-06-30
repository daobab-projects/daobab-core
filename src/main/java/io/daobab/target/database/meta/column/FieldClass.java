package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface FieldClass<E extends Entity> extends EntityRelationMap<E> {

    default Class getFieldClass() {
        return getColumnParam("FieldClass");
    }

    default E setFieldClass(Class val) {
        return setColumnParam("FieldClass", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FieldClass");
                return entity.getFieldClass();
            }

            @Override
            public FieldClass setValue(FieldClass entity, Class param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FieldClass");
                return (FieldClass) entity.setFieldClass(param);
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
