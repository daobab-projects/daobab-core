package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface CategoryId<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: CATEGORY_ID,
     * db type: TINYINT
     */
    default Integer getCategoryId() {
        return getColumnParam("CategoryId");
    }

    default E setCategoryId(Integer val) {
        setColumnParam("CategoryId", val);
        return (E) this;
    }

    default Column<E, Integer, CategoryId> colCategoryId() {
        return new Column<E, Integer, CategoryId>() {

            @Override
            public String getColumnName() {
                return "CATEGORY_ID";
            }

            @Override
            public String getFieldName() {
                return "CategoryId";
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
            public Integer getValue(CategoryId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "CategoryId");
                return entity.getCategoryId();
            }

            @Override
            public void setValue(CategoryId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "CategoryId");
                entity.setCategoryId(param);
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