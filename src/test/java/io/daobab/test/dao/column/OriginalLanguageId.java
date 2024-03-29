package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface OriginalLanguageId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: ORIGINAL_LANGUAGE_ID,
     * db type: TINYINT
     */
    default Integer getOriginalLanguageId() {
        return readParam("OriginalLanguageId");
    }

    default E setOriginalLanguageId(Integer val) {
        return storeParam("OriginalLanguageId", val);
    }

    default Column<E, Integer, OriginalLanguageId> colOriginalLanguageId() {
        return new Column<E, Integer, OriginalLanguageId>() {

            @Override
            public String getColumnName() {
                return "ORIGINAL_LANGUAGE_ID";
            }

            @Override
            public String getFieldName() {
                return "OriginalLanguageId";
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
            public Integer getValue(OriginalLanguageId entity) {
                if (entity == null)
                    throw new AttemptToReadFromNullEntityException(entityClass(), "OriginalLanguageId");
                return entity.getOriginalLanguageId();
            }

            @Override
            public OriginalLanguageId setValue(OriginalLanguageId entity, Integer param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(entityClass(), "OriginalLanguageId");
                return (OriginalLanguageId) entity.setOriginalLanguageId(param);
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
