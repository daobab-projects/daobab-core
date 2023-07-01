package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface SpecialFeatures<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: SPECIAL_FEATURES,
     * db type: VARCHAR
     */
    default String getSpecialFeatures() {
        return readParam("SpecialFeatures");
    }

    default E setSpecialFeatures(String val) {
        return storeParam("SpecialFeatures", val);
    }

    default Column<E, String, SpecialFeatures> colSpecialFeatures() {
        return new Column<E, String, SpecialFeatures>() {

            @Override
            public String getColumnName() {
                return "SPECIAL_FEATURES";
            }

            @Override
            public String getFieldName() {
                return "SpecialFeatures";
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
            public String getValue(SpecialFeatures entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "SpecialFeatures");
                return entity.getSpecialFeatures();
            }

            @Override
            public SpecialFeatures setValue(SpecialFeatures entity, String param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "SpecialFeatures");
                return (SpecialFeatures) entity.setSpecialFeatures(param);
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
