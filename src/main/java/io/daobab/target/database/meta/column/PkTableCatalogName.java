package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface PkTableCatalogName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getPkCatalogName() {
        return getColumnParam("PkTableCatalogName");
    }

    default E setPkCatalogName(String val) {
        return setColumnParam("PkTableCatalogName", val);
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
                if (entity == null)
                    throw new AttemptToReadFromNullEntityException(getEntityClass(), "PkTableCatalogName");
                return entity.getPkCatalogName();
            }

            @Override
            public PkTableCatalogName setValue(PkTableCatalogName entity, String param) {
                if (entity == null)
                    throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PkTableCatalogName");
                return (PkTableCatalogName) entity.setPkCatalogName(param);
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
