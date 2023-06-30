package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.math.BigDecimal;
import java.util.Objects;

public interface RentalId<E extends Entity> extends EntityRelationMap<E> {


    /**
     * db name: RENTAL_ID,
     * db type: INTEGER
     */
    default BigDecimal getRentalId() {
        return getColumnParam("RentalId");
    }

    default E setRentalId(BigDecimal val) {
        return setColumnParam("RentalId", val);
    }

    default Column<E, BigDecimal, RentalId> colRentalId() {
        return new Column<E, BigDecimal, RentalId>() {

            @Override
            public String getColumnName() {
                return "RENTAL_ID";
            }

            @Override
            public String getFieldName() {
                return "RentalId";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<BigDecimal> getFieldClass() {
                return BigDecimal.class;
            }

            @Override
            public BigDecimal getValue(RentalId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "RentalId");
                return entity.getRentalId();
            }

            @Override
            public RentalId setValue(RentalId entity, BigDecimal param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "RentalId");
                return (RentalId) entity.setRentalId(param);
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
