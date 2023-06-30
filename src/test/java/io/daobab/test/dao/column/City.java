package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface City<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: CITY,
     * db type: VARCHAR
     */
    default String getCity() {
        return getColumnParam("City");
    }

    default E setCity(String val) {
        return setColumnParam("City", val);
    }

    default Column<E, String, City> colCity() {
        return new Column<E, String, City>() {

            @Override
            public String getColumnName() {
                return "CITY";
            }

            @Override
            public String getFieldName() {
                return "City";
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
            public String getValue(City entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "City");
                return entity.getCity();
            }

            @Override
            public City setValue(City entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "City");
                return (City) entity.setCity(param);
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
