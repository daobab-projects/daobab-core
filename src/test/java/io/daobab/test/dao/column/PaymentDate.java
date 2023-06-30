package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.sql.Timestamp;
import java.util.Objects;

public interface PaymentDate<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: PAYMENT_DATE,
     * db type: TIMESTAMP
     */
    default Timestamp getPaymentDate() {
        return getColumnParam("PaymentDate");
    }

    default E setPaymentDate(Timestamp val) {
        return setColumnParam("PaymentDate", val);
    }

    default Column<E, Timestamp, PaymentDate> colPaymentDate() {
        return new Column<E, Timestamp, PaymentDate>() {

            @Override
            public String getColumnName() {
                return "PAYMENT_DATE";
            }

            @Override
            public String getFieldName() {
                return "PaymentDate";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Timestamp> getFieldClass() {
                return Timestamp.class;
            }

            @Override
            public Timestamp getValue(PaymentDate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PaymentDate");
                return entity.getPaymentDate();
            }

            @Override
            public PaymentDate setValue(PaymentDate entity, Timestamp param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PaymentDate");
                return (PaymentDate) entity.setPaymentDate(param);
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
