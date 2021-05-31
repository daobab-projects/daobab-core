package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;
import io.daobab.target.statistic.dictionary.CallStatus;

import java.util.Objects;

public interface Status<E extends EntityMap> extends EntityRelationMap<E> {

    default CallStatus getStatus() {
        return getColumnParam("Status");
    }

    default E setStatus(CallStatus val) {
        setColumnParam("Status", val);
        return (E) this;
    }

    default Column<E, CallStatus, Status> colStatus() {
        return new Column<E, CallStatus, Status>() {

            @Override
            public String getColumnName() {
                return "STATUS";
            }

            @Override
            public String getFieldName() {
                return "Status";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<CallStatus> getFieldClass() {
                return CallStatus.class;
            }

            @Override
            public CallStatus getValue(Status entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Status");
                return entity.getStatus();
            }

            @Override
            public void setValue(Status entity, CallStatus param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Status");
                entity.setStatus(param);
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