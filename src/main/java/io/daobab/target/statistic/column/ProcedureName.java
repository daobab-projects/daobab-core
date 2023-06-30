package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface ProcedureName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getProcedureName() {
        return getColumnParam("ProcedureName");
    }

    default E setProcedureName(String val) {
        return setColumnParam("ProcedureName", val);
    }

    default Column<E, String, ProcedureName> colProcedureName() {
        return new Column<E, String, ProcedureName>() {

            @Override
            public String getColumnName() {
                return "PROCEDURE_NAME";
            }

            @Override
            public String getFieldName() {
                return "ProcedureName";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<String> getFieldClass() {
                return String.class;
            }

            @Override
            public String getValue(ProcedureName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ProcedureName");
                return entity.getProcedureName();
            }

            @Override
            public ProcedureName setValue(ProcedureName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ProcedureName");
                return (ProcedureName) entity.setProcedureName(param);
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
