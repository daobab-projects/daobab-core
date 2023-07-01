package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface StaffId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: STAFF_ID,
     * db type: TINYINT
     */
    default Integer getStaffId() {
        return readParam("StaffId");
    }

    default E setStaffId(Integer val) {
        return storeParam("StaffId", val);
    }

    default Column<E, Integer, StaffId> colStaffId() {
        return new Column<E, Integer, StaffId>() {

            @Override
            public String getColumnName() {
                return "STAFF_ID";
            }

            @Override
            public String getFieldName() {
                return "StaffId";
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
            public Integer getValue(StaffId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "StaffId");
                return entity.getStaffId();
            }

            @Override
            public StaffId setValue(StaffId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "StaffId");
                return (StaffId) entity.setStaffId(param);
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
