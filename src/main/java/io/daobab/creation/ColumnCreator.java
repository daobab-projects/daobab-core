package io.daobab.creation;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;
import io.daobab.model.Table;

import java.util.Objects;

public class ColumnCreator {


    public static <E extends Table, F, R extends RelatedTo<E> & MapHandler<E>> Column<E, F, R> createColumn(String fieldName, String columnName, E entity, Class<F> clazz) {

        return new Column<E, F, R>() {

            @Override
            public String getColumnName() {
                return columnName;
            }

            @Override
            public String getFieldName() {
                return fieldName;
            }

            @Override
            public E getInstance() {
                return entity;
            }

            @Override
            public Class<F> getFieldClass() {
                return clazz;
            }

            @Override
            public F getValue(R entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), fieldName);
                return entity.getColumnParam(fieldName);
            }

            @Override
            public R setValue(R entity, F param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), fieldName);
                return (R) entity.setColumnParam(fieldName, param);
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
