package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.sql.Timestamp;
import java.util.Objects;

public interface ResponseDate<E extends Entity> extends EntityRelationMap<E> {


    /**
     * db name: PAYMENT_DATE,
     * db type: TIMESTAMP
     */
    default Timestamp getResponseDate() {
        return getColumnParam("ResponseDate");
    }

    @SuppressWarnings("unchecked")
    default E setResponseDate(Timestamp val) {
        return setColumnParam("ResponseDate", val);
    }

    default Column<E, Timestamp, ResponseDate> colResponseDate() {
        return new Column<E, Timestamp, ResponseDate>() {

            @Override
            public String getColumnName() {
                return "SEND_DATE";
            }

            @Override
            public String getFieldName() {
                return "ResponseDate";
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
            public Timestamp getValue(ResponseDate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ResponseDate");
                return entity.getResponseDate();
            }

            @Override
            public ResponseDate setValue(ResponseDate entity, Timestamp param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ResponseDate");
                return (ResponseDate) entity.setResponseDate(param);
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
