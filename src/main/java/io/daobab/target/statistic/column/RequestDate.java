package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.sql.Timestamp;
import java.util.Objects;

public interface RequestDate<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    default Timestamp getRequestDate() {
        return readParam("RequestDate");
    }

    default E setRequestDate(Timestamp val) {
        return storeParam("RequestDate", val);
    }

    default Column<E, Timestamp, RequestDate> colRequestDate() {
        return new Column<E, Timestamp, RequestDate>() {

            @Override
            public String getColumnName() {
                return "REQUEST_DATE";
            }

            @Override
            public String getFieldName() {
                return "RequestDate";
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
            public Timestamp getValue(RequestDate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "RequestDate");
                return entity.getRequestDate();
            }

            @Override
            public RequestDate setValue(RequestDate entity, Timestamp param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "RequestDate");
                return (RequestDate) entity.setRequestDate(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getEntityClass().getName() + "." + getFieldName();
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
