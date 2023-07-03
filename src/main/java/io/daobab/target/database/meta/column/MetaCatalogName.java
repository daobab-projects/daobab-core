package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface MetaCatalogName<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getCatalogName() {
        return readParam("CatalogName");
    }

    default E setCatalogName(String val) {
        return storeParam("CatalogName", val);
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "CatalogName");
                return entity.getCatalogName();
            }

            @Override
            public MetaCatalogName setValue(MetaCatalogName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "CatalogName");
                return (MetaCatalogName) entity.setCatalogName(param);
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
