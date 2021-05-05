package daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface District<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: DISTRICT,
     * db type: VARCHAR
     */
    default String getDistrict() {
        return getColumnParam("District");
    }

    default E setDistrict(String val) {
        setColumnParam("District", val);
        return (E) this;
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "District");
                return entity.getDistrict();
            }

            @Override
            public void setValue(District entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "District");
                entity.setDistrict(param);
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