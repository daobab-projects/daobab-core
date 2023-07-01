package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Remarks<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getRemarks() {
        return readParam("Remarks");
    }

    default E setRemarks(String val) {
        return storeParam("Remarks", val);
    }

    default Column<E, String, Remarks> colRemarks() {
        return new Column<E, String, Remarks>() {

            @Override
            public String getColumnName() {
                return "REMARKS";
            }

            @Override
            public String getFieldName() {
                return "Remarks";
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
            public String getValue(Remarks entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Remarks");
                return entity.getRemarks();
            }

            @Override
            public Remarks setValue(Remarks entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Remarks");
                return (Remarks) entity.setRemarks(param);
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
