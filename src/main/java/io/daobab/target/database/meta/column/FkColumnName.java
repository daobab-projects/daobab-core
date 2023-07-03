package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface FkColumnName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getFkColumnName() {
        return readParam("FkColumnName");
    }

    default E setFkColumnName(String val) {
        return storeParam("FkColumnName", val);
    }

    default Column<E, String, FkColumnName> colFkColumnName() {
        return new Column<E, String, FkColumnName>() {

            @Override
            public String getColumnName() {
                return "FKCOLUMN_NAME";
            }

            @Override
            public String getFieldName() {
                return "FkColumnName";
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
            public String getValue(FkColumnName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "FkColumnName");
                return entity.getFkColumnName();
            }

            @Override
            public FkColumnName setValue(FkColumnName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "FkColumnName");
                return (FkColumnName) entity.setFkColumnName(param);
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
