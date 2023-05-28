package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.math.BigDecimal;
import java.util.Objects;

public interface ReplacementCost<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: REPLACEMENT_COST,
     * db type: DECIMAL
     */
    default BigDecimal getReplacementCost() {
        return readParam("ReplacementCost");
    }

    default E setReplacementCost(BigDecimal val) {
        return storeParam("ReplacementCost", val);
    }

    default Column<E, BigDecimal, ReplacementCost> colReplacementCost() {
        return new Column<E, BigDecimal, ReplacementCost>() {

            @Override
            public String getColumnName() {
                return "REPLACEMENT_COST";
            }

            @Override
            public String getFieldName() {
                return "ReplacementCost";
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
            public BigDecimal getValue(ReplacementCost entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "ReplacementCost");
                return entity.getReplacementCost();
            }

            @Override
            public ReplacementCost setValue(ReplacementCost entity, BigDecimal param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(entityClass(), "ReplacementCost");
                return (ReplacementCost) entity.setReplacementCost(param);
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
