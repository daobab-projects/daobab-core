package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface District<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: DISTRICT,
     * db type: VARCHAR
     */
    default String getDistrict() {
        return readParam("District");
    }

    default E setDistrict(String val) {
        return storeParam("District", val);
    }

    default Column<E, String, District> colDistrict() {
        return new Column<E, String, District>() {

            @Override
            public String getColumnName() {
                return "DISTRICT";
            }

            @Override
            public String getFieldName() {
                return "District";
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
            public String getValue(District entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "District");
                return entity.getDistrict();
            }

            @Override
            public District setValue(District entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "District");
                return (District) entity.setDistrict(param);
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
