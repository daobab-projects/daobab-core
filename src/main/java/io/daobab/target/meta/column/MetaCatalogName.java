package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface MetaCatalogName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getCatalogName() {
        return getColumnParam("CatalogName");
    }

    default E setCatalogName(String val) {
        setColumnParam("CatalogName", val);
        return (E) this;
    }

    default Column<E, String, MetaCatalogName> colCatalogName() {
        return new Column<E, String, MetaCatalogName>() {

            @Override
            public String getColumnName() {
                return "CAT_NAME";
            }

            @Override
            public String getFieldName() {
                return "CatalogName";
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
            public String getValue(MetaCatalogName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "CatalogName");
                return entity.getCatalogName();
            }

            @Override
            public void setValue(MetaCatalogName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "CatalogName");
                entity.setCatalogName(param);
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