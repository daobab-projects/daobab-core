package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface CamelName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getCamelName() {
        return getColumnParam("CamelName");
    }

    default E setCamelName(String val) {
        setColumnParam("CamelName", val);
        return (E) this;
    }

    default Column<E, String, CamelName> colCamelName() {
        return new Column<E, String, CamelName>() {

            @Override
            public String getColumnName() {
                return "CAMEL_NAME";
            }

            @Override
            public String getFieldName() {
                return "CamelName";
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
            public String getValue(CamelName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "CamelName");
                return entity.getCamelName();
            }

            @Override
            public void setValue(CamelName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "CamelName");
                entity.setCamelName(param);
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
