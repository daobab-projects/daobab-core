package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.sql.Timestamp;
import java.util.Objects;

public interface RentalDate<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: RENTAL_DATE,
     * db type: TIMESTAMP
     */
    default Timestamp getRentalDate() {
        return getColumnParam("RentalDate");
    }

    default E setRentalDate(Timestamp val) {
        setColumnParam("RentalDate", val);
        return (E) this;
    }

    default Column<E, Timestamp, RentalDate> colRentalDate() {
        return new Column<E, Timestamp, RentalDate>() {

            @Override
            public String getColumnName() {
                return "RENTAL_DATE";
            }

            @Override
            public String getFieldName() {
                return "RentalDate";
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
            public Timestamp getValue(RentalDate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "RentalDate");
                return entity.getRentalDate();
            }

            @Override
            public void setValue(RentalDate entity, Timestamp param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "RentalDate");
                entity.setRentalDate(param);
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