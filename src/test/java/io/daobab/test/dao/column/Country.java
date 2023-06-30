package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Country<E extends Entity> extends EntityRelationMap<E> {


    /**
     * db name: COUNTRY,
     * db type: VARCHAR
     */
    default String getCountry() {
        return getColumnParam("Country");
    }

    default E setCountry(String val) {
        return setColumnParam("Country", val);
    }

    default Column<E, String, Country> colCountry() {
        return new Column<E, String, Country>() {

            @Override
            public String getColumnName() {
                return "COUNTRY";
            }

            @Override
            public String getFieldName() {
                return "Country";
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
            public String getValue(Country entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Country");
                return entity.getCountry();
            }

            @Override
            public Country setValue(Country entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Country");
                return (Country) entity.setCountry(param);
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
