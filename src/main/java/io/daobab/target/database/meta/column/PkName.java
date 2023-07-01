package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface PkName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkName() {
        return readParam("PkName");
    }

    default E setPkName(String val) {
        return storeParam("PkName", val);
    }

    default Column<E, String, PkName> colPkName() {
        return new Column<E, String, PkName>() {

            @Override
            public String getColumnName() {
                return "PK_NAME";
            }

            @Override
            public String getFieldName() {
                return "PkName";
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
            public String getValue(PkName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PkName");
                return entity.getPkName();
            }

            @Override
            public PkName setValue(PkName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PkName");
                return (PkName) entity.setPkName(param);
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
