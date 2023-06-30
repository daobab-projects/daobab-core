package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface MetaIndexType<E extends Entity> extends EntityRelationMap<E> {

    default String getIndexType() {
        return getColumnParam("IndexType");
    }

    default E setIndexType(String val) {
        return setColumnParam("IndexType", val);
    }

    default Column<E, String, MetaIndexType> colIndexType() {
        return new Column<E, String, MetaIndexType>() {

            @Override
            public String getColumnName() {
                return "INDEX_TYPE";
            }

            @Override
            public String getFieldName() {
                return "IndexType";
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
            public String getValue(MetaIndexType entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "IndexType");
                return entity.getIndexType();
            }

            @Override
            public MetaIndexType setValue(MetaIndexType entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "IndexType");
                return (MetaIndexType) entity.setIndexType(param);
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
