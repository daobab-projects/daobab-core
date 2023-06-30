package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.math.BigDecimal;
import java.util.Objects;

public interface RentalRate<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: RENTAL_RATE,
     * db type: DECIMAL
     */
    default BigDecimal getRentalRate() {
        return getColumnParam("RentalRate");
    }

    default E setRentalRate(BigDecimal val) {
        return setColumnParam("RentalRate", val);
    }

    default Column<E, BigDecimal, RentalRate> colRentalRate() {
        return new Column<E, BigDecimal, RentalRate>() {

            @Override
            public String getColumnName() {
                return "RENTAL_RATE";
            }

            @Override
            public String getFieldName() {
                return "RentalRate";
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
            public BigDecimal getValue(RentalRate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "RentalRate");
                return entity.getRentalRate();
            }

            @Override
            public RentalRate setValue(RentalRate entity, BigDecimal param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "RentalRate");
                return (RentalRate) entity.setRentalRate(param);
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
