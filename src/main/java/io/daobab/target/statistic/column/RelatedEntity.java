package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface RelatedEntity<E extends EntityMap> extends EntityRelationMap<E> {

    default String getRelatedEntity() {
        return getColumnParam("RelatedEntity");
    }

    default E setRelatedEntity(String val) {
        setColumnParam("RelatedEntity", val);
        return (E) this;
    }

    default Column<E, String, RelatedEntity> colRelatedEntity() {
        return new Column<E, String, RelatedEntity>() {

            @Override
            public String getColumnName() {
                return "RELATED_ENTITY";
            }

            @Override
            public String getFieldName() {
                return "RelatedEntity";
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
            public String getValue(RelatedEntity entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "RelatedEntity");
                return entity.getRelatedEntity();
            }

            @Override
            public void setValue(RelatedEntity entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "RelatedEntity");
                entity.setRelatedEntity(param);
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