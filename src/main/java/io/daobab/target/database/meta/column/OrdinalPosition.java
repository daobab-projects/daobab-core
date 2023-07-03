package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface OrdinalPosition<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Integer getOrdinalPosition() {
        return readParam("OrdinalPosition");
    }

    default E setOrdinalPosition(Integer val) {
        return storeParam("OrdinalPosition", val);
    }

    default Column<E, Integer, OrdinalPosition> colOrdinalPosition() {
        return new Column<E, Integer, OrdinalPosition>() {

            @Override
            public String getColumnName() {
                return "ORDINAL_POSITION";
            }

            @Override
            public String getFieldName() {
                return "OrdinalPosition";
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
            public Integer getValue(OrdinalPosition entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "OrdinalPosition");
                return entity.getOrdinalPosition();
            }

            @Override
            public OrdinalPosition setValue(OrdinalPosition entity, Integer param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(entityClass(), "OrdinalPosition");
                return (OrdinalPosition) entity.setOrdinalPosition(param);
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
