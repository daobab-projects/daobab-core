package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface FkName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getFkName() {
        return getColumnParam("FkName");
    }

    default E setFkName(String val) {
        setColumnParam("FkName", val);
        return (E) this;
    }

    default Column<E, String, FkName> colFkName() {
        return new Column<E, String, FkName>() {

            @Override
            public String getColumnName() {
                return "FK_NAME";
            }

            @Override
            public String getFieldName() {
                return "FkName";
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
            public String getValue(FkName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FkName");
                return entity.getFkName();
            }

            @Override
            public void setValue(FkName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FkName");
                entity.setFkName(param);
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
