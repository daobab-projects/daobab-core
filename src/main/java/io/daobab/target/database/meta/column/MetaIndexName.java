package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface MetaIndexName<E extends EntityMap> extends EntityRelationMap<E> {

    default String getIndexName() {
        return getColumnParam("IndexName");
    }

    default E setIndexName(String val) {
        setColumnParam("IndexName", val);
        return (E) this;
    }

    default Column<E, String, MetaIndexName> colIndexName() {
        return new Column<E, String, MetaIndexName>() {

            @Override
            public String getColumnName() {
                return "INDEX_NAME";
            }

            @Override
            public String getFieldName() {
                return "IndexName";
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
            public String getValue(MetaIndexName entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "IndexName");
                return entity.getIndexName();
            }

            @Override
            public void setValue(MetaIndexName entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "IndexName");
                entity.setIndexName(param);
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
