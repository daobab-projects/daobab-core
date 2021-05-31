package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface FilterCondition<E extends EntityMap> extends EntityRelationMap<E> {

    default String getFilterCondition() {
        return getColumnParam("FilterCondition");
    }

    default E setFilterCondition(String val) {
        setColumnParam("FilterCondition", val);
        return (E) this;
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FilterCondition");
                return entity.getFilterCondition();
            }

            @Override
            public void setValue(FilterCondition entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FilterCondition");
                entity.setFilterCondition(param);
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