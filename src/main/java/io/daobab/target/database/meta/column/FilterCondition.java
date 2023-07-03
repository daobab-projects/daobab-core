package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface FilterCondition<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFilterCondition() {
        return readParam("FilterCondition");
    }

    default E setFilterCondition(String val) {
        return storeParam("FilterCondition", val);
    }

    default Column<E, String, FilterCondition> colFilterCondition() {
        return new Column<E, String, FilterCondition>() {

            @Override
            public String getColumnName() {
                return "FILTER_CONDITION";
            }

            @Override
            public String getFieldName() {
                return "FilterCondition";
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
            public String getValue(FilterCondition entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "FilterCondition");
                return entity.getFilterCondition();
            }

            @Override
            public FilterCondition setValue(FilterCondition entity, String param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(entityClass(), "FilterCondition");
                return (FilterCondition) entity.setFilterCondition(param);
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
