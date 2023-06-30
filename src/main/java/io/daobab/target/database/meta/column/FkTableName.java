package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface FkTableName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFkTableName() {
        return getColumnParam("FkTableName");
    }

    default E setFkTableName(String val) {
        return setColumnParam("FkTableName", val);
    }

    default Column<E, String, FkTableName> colFkTableName() {
        return new Column<E, String, FkTableName>() {

            @Override
            public String getColumnName() {
                return "FKTABLE_NAME";
            }

            @Override
            public String getFieldName() {
                return "FkTableName";
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
            public String getValue(FkTableName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FkTableName");
                return entity.getFkTableName();
            }

            @Override
            public FkTableName setValue(FkTableName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FkTableName");
                return (FkTableName) entity.setFkTableName(param);
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
