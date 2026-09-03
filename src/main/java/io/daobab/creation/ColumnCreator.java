package io.daobab.creation;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;
import io.daobab.model.Table;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;

import java.util.Objects;

public class ColumnCreator {

    private ColumnCreator() {
    }

    public static <E extends Table<?>, F, R extends RelatedTo<E> & MapHandler<E>> Column<E, F, R> createInnerTypeColumn(String fieldName, String columnName, E entity, Class<F> clazz, Class innerTypeClass) {

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
            public Class getInnerTypeClass() {
                return innerTypeClass;
            }

            @Override
            public F getValue(R entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), fieldName);
                return entity.readParam(fieldName);
            }

            @Override
            public R setValue(R entity, F param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), fieldName);
                return (R) entity.storeParam(fieldName, param);
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
                return Objects.equals(toString(), other.toString());
            }
        };

    }


    public static <E extends Table<?>, F, R extends RelatedTo<E> & MapHandler<E>> Column<E, F, R> createColumn(String fieldName, String columnName, E entity, Class<F> clazz) {
        return createColumn(fieldName, columnName, entity, clazz, null);
    }

    /**
     * Creates a column pinned to the given {@link DatabaseTypeConverter}: the returned column overrides
     * {@link Column#getColumnTypeConverter()} to yield {@code converterClass}, so the
     * {@code DatabaseConverterManager} uses it in preference to the automatically resolved converter.
     * A {@code null} {@code converterClass} keeps the default behaviour (automatic converter resolution).
     *
     * @param converterClass the converter class pinned to the column, or {@code null} for automatic resolution
     */
    public static <E extends Table<?>, F, R extends RelatedTo<E> & MapHandler<E>> Column<E, F, R> createColumn(String fieldName, String columnName, E entity, Class<F> clazz, Class<? extends DatabaseTypeConverter> converterClass) {

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
            @SuppressWarnings({"unchecked", "rawtypes"})
            public Class<DatabaseTypeConverter<?, F>> getColumnTypeConverter() {
                return (Class) converterClass;
            }

            @Override
            public F getValue(R entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), fieldName);
                return entity.readParam(fieldName);
            }

            @Override
            public R setValue(R entity, F param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), fieldName);
                return (R) entity.storeParam(fieldName, param);
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
                return Objects.equals(toString(), other.toString());
            }
        };


    }

}
