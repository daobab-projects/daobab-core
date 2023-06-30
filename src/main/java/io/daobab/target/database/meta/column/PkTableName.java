package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface PkTableName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkTableName() {
        return getColumnParam("PkTableName");
    }

    default E setPkTableName(String val) {
        return setColumnParam("PkTableName", val);
    }

    default Column<E, String, PkTableName> colPkTableName() {
        return new Column<E, String, PkTableName>() {

            @Override
            public String getColumnName() {
                return "PKTABLE_NAME";
            }

            @Override
            public String getFieldName() {
                return "PkTableName";
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
            public String getValue(PkTableName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PkTableName");
                return entity.getPkTableName();
            }

            @Override
            public PkTableName setValue(PkTableName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PkTableName");
                return (PkTableName) entity.setPkTableName(param);
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
