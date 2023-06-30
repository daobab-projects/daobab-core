package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.math.BigDecimal;
import java.util.Objects;

public interface Amount<E extends Entity> extends EntityRelationMap<E> {


    /**
     * db name: AMOUNT,
     * db type: DECIMAL
     */
    default BigDecimal getAmount() {
        return getColumnParam("Amount");
    }

    default E setAmount(BigDecimal val) {
        return setColumnParam("Amount", val);
    }

    default Column<E, BigDecimal, Amount> colAmount() {
        return new Column<E, BigDecimal, Amount>() {

            @Override
            public String getColumnName() {
                return "AMOUNT";
            }

            @Override
            public String getFieldName() {
                return "Amount";
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
            public BigDecimal getValue(Amount entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Amount");
                return entity.getAmount();
            }

            @Override
            public Amount setValue(Amount entity, BigDecimal param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Amount");
                return (Amount) entity.setAmount(param);
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
