package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface CategoryId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: CATEGORY_ID,
     * db type: TINYINT
     */
    default Integer getCategoryId() {
        return readParam("CategoryId");
    }

    default E setCategoryId(Integer val) {
        return storeParam("CategoryId", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "CategoryId");
                return entity.getCategoryId();
            }

            @Override
            public CategoryId setValue(CategoryId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "CategoryId");
                return (CategoryId) entity.setCategoryId(param);
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
