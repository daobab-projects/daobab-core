package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface CityId<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: CITY_ID,
     * db type: SMALLINT
     */
    default Integer getCityId() {
        return getColumnParam("CityId");
    }

    default E setCityId(Integer val) {
        setColumnParam("CityId", val);
        return (E) this;
    }

    default Column<E, Integer, CityId> colCityId() {
        return new Column<E, Integer, CityId>() {

            @Override
            public String getColumnName() {
                return "CITY_ID";
            }

            @Override
            public String getFieldName() {
                return "CityId";
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
            public Integer getValue(CityId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "CityId");
                return entity.getCityId();
            }

            @Override
            public void setValue(CityId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "CityId");
                entity.setCityId(param);
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