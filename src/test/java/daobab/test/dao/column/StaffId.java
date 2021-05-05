package daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface StaffId<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: STAFF_ID,
     * db type: TINYINT
     */
    default Integer getStaffId() {
        return getColumnParam("StaffId");
    }

    default E setStaffId(Integer val) {
        setColumnParam("StaffId", val);
        return (E) this;
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
            public void setValue(StaffId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "StaffId");
                entity.setStaffId(param);
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