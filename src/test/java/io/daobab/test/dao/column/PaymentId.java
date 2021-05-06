package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface PaymentId<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: PAYMENT_ID,
     * db type: SMALLINT
     */
    default Integer getPaymentId() {
        return getColumnParam("PaymentId");
    }

    default E setPaymentId(Integer val) {
        setColumnParam("PaymentId", val);
        return (E) this;
    }

    default Column<E, Integer, PaymentId> colPaymentId() {
        return new Column<E, Integer, PaymentId>() {

            @Override
            public String getColumnName() {
                return "PAYMENT_ID";
            }

            @Override
            public String getFieldName() {
                return "PaymentId";
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
            public Integer getValue(PaymentId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PaymentId");
                return entity.getPaymentId();
            }

            @Override
            public void setValue(PaymentId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PaymentId");
                entity.setPaymentId(param);
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