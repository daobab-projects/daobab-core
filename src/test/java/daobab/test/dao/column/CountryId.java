package daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface CountryId<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: COUNTRY_ID,
     * db type: SMALLINT
     */
    default Integer getCountryId() {
        return getColumnParam("CountryId");
    }

    default E setCountryId(Integer val) {
        setColumnParam("CountryId", val);
        return (E) this;
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
            public void setValue(CountryId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "CountryId");
                entity.setCountryId(param);
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