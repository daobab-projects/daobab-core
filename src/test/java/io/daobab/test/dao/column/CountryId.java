package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface CountryId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: COUNTRY_ID,
     * db type: SMALLINT
     */
    default Integer getCountryId() {
        return getColumnParam("CountryId");
    }

    default E setCountryId(Integer val) {
        return setColumnParam("CountryId", val);
    }

    default Column<E, Integer, CountryId> colCountryId() {
        return new Column<E, Integer, CountryId>() {

            @Override
            public String getColumnName() {
                return "COUNTRY_ID";
            }

            @Override
            public String getFieldName() {
                return "CountryId";
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
            public Integer getValue(CountryId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "CountryId");
                return entity.getCountryId();
            }

            @Override
            public CountryId setValue(CountryId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "CountryId");
                return (CountryId) entity.setCountryId(param);
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
