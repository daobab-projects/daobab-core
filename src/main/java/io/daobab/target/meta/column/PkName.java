package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface PkName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getPkName() {
        return getColumnParam("PkName");
    }

    default E setPkName(String val) {
        setColumnParam("PkName", val);
        return (E) this;
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
            public void setValue(PkName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PkName");
                entity.setPkName(param);
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