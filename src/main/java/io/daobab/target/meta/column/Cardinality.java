package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Cardinality<E extends EntityMap> extends EntityRelationMap<E> {

    default Integer getCardinality() {
        return getColumnParam("Cardinality");
    }

    default E setCardinality(Integer val) {
        setColumnParam("Cardinality", val);
        return (E) this;
    }

    default Column<E, Integer, Cardinality> colCardinality() {
        return new Column<E, Integer, Cardinality>() {

            @Override
            public String getColumnName() {
                return "CARDINALITY";
            }

            @Override
            public String getFieldName() {
                return "Cardinality";
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
            public Integer getValue(Cardinality entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Cardinality");
                return entity.getCardinality();
            }

            @Override
            public void setValue(Cardinality entity, Integer param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Cardinality");
                entity.setCardinality(param);
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