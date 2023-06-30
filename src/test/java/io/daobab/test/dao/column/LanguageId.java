package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface LanguageId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: LANGUAGE_ID,
     * db type: TINYINT
     */
    default Integer getLanguageId() {
        return getColumnParam("LanguageId");
    }

    default E setLanguageId(Integer val) {
        return setColumnParam("LanguageId", val);
    }

    default Column<E, Integer, LanguageId> colLanguageId() {
        return new Column<E, Integer, LanguageId>() {

            @Override
            public String getColumnName() {
                return "LANGUAGE_ID";
            }

            @Override
            public String getFieldName() {
                return "LanguageId";
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
            public Integer getValue(LanguageId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "LanguageId");
                return entity.getLanguageId();
            }

            @Override
            public LanguageId setValue(LanguageId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "LanguageId");
                return (LanguageId) entity.setLanguageId(param);
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
