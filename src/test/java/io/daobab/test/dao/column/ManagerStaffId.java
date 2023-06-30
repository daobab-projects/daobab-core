package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface ManagerStaffId<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: MANAGER_STAFF_ID,
     * db type: TINYINT
     */
    default Integer getManagerStaffId() {
        return getColumnParam("ManagerStaffId");
    }

    default E setManagerStaffId(Integer val) {
        return setColumnParam("ManagerStaffId", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ManagerStaffId");
                return entity.getManagerStaffId();
            }

            @Override
            public ManagerStaffId setValue(ManagerStaffId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ManagerStaffId");
                return (ManagerStaffId) entity.setManagerStaffId(param);
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
