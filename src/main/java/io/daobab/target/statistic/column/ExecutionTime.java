package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface ExecutionTime<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default Long getExecutionTime() {
        return readParam("ExecutionTime");
    }

    default E setExecutionTime(Long val) {
        return storeParam("ExecutionTime", val);
    }

    default Column<E, Long, ExecutionTime> colExecutionTime() {
        return new Column<E, Long, ExecutionTime>() {

            @Override
            public String getColumnName() {
                return "EXECUTION_TIME";
            }

            @Override
            public String getFieldName() {
                return "ExecutionTime";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Long> getFieldClass() {
                return Long.class;
            }

            @Override
            public Long getValue(ExecutionTime entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ExecutionTime");
                return entity.getExecutionTime();
            }

            @Override
            public ExecutionTime setValue(ExecutionTime entity, Long param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ExecutionTime");
                return (ExecutionTime) entity.setExecutionTime(param);
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
