package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.sql.Timestamp;
import java.util.Objects;

public interface ReturnDate<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: RETURN_DATE,
     * db type: TIMESTAMP
     */
    default Timestamp getReturnDate() {
        return getColumnParam("ReturnDate");
    }

    default E setReturnDate(Timestamp val) {
        return setColumnParam("ReturnDate", val);
    }

    default Column<E, Timestamp, ReturnDate> colReturnDate() {
        return new Column<E, Timestamp, ReturnDate>() {

            @Override
            public String getColumnName() {
                return "RETURN_DATE";
            }

            @Override
            public String getFieldName() {
                return "ReturnDate";
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
            public Timestamp getValue(ReturnDate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ReturnDate");
                return entity.getReturnDate();
            }

            @Override
            public ReturnDate setValue(ReturnDate entity, Timestamp param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ReturnDate");
                return (ReturnDate) entity.setReturnDate(param);
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
