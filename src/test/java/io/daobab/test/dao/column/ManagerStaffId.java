package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface ManagerStaffId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: MANAGER_STAFF_ID,
     * db type: TINYINT
     */
    default Integer getManagerStaffId() {
        return readParam("ManagerStaffId");
    }

    default E setManagerStaffId(Integer val) {
        return storeParam("ManagerStaffId", val);
    }

    default Column<E, Integer, ManagerStaffId> colManagerStaffId() {
        return new Column<E, Integer, ManagerStaffId>() {

            @Override
            public String getColumnName() {
                return "MANAGER_STAFF_ID";
            }

            @Override
            public String getFieldName() {
                return "ManagerStaffId";
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
            public Integer getValue(ManagerStaffId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "ManagerStaffId");
                return entity.getManagerStaffId();
            }

            @Override
            public ManagerStaffId setValue(ManagerStaffId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "ManagerStaffId");
                return (ManagerStaffId) entity.setManagerStaffId(param);
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
