package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface FkTableCatalogName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getFkCatalogName() {
        return getColumnParam("FkTableCatalogName");
    }

    default E setFkCatalogName(String val) {
        setColumnParam("FkTableCatalogName", val);
        return (E) this;
    }

    default Column<E, String, FkTableCatalogName> colFkCatalogName() {
        return new Column<E, String, FkTableCatalogName>() {

            @Override
            public String getColumnName() {
                return "FKTABLE_CAT";
            }

            @Override
            public String getFieldName() {
                return "FkTableCatalogName";
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
            public String getValue(FkTableCatalogName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "FkTableCatalogName");
                return entity.getFkCatalogName();
            }

            @Override
            public void setValue(FkTableCatalogName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "FkTableCatalogName");
                entity.setFkCatalogName(param);
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