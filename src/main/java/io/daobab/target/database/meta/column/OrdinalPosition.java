package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface OrdinalPosition<E extends EntityMap> extends EntityRelationMap<E> {

    default Integer getOrdinalPosition() {
        return getColumnParam("OrdinalPosition");
    }

    default E setOrdinalPosition(Integer val) {
        setColumnParam("OrdinalPosition", val);
        return (E) this;
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "OrdinalPosition");
                return entity.getOrdinalPosition();
            }

            @Override
            public void setValue(OrdinalPosition entity, Integer param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "OrdinalPosition");
                entity.setOrdinalPosition(param);
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
