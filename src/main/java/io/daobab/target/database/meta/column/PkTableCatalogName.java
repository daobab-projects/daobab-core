package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface PkTableCatalogName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getPkCatalogName() {
        return getColumnParam("PkTableCatalogName");
    }

    default E setPkCatalogName(String val) {
        setColumnParam("PkTableCatalogName", val);
        return (E) this;
    }

    default Column<E, String, PkTableCatalogName> colPkCatalogName() {
        return new Column<E, String, PkTableCatalogName>() {

            @Override
            public String getColumnName() {
                return "PKTABLE_CAT";
            }

            @Override
            public String getFieldName() {
                return "PkTableCatalogName";
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
            public String getValue(PkTableCatalogName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PkTableCatalogName");
                return entity.getPkCatalogName();
            }

            @Override
            public void setValue(PkTableCatalogName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PkTableCatalogName");
                entity.setPkCatalogName(param);
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
