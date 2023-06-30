package io.daobab.target.statistic.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Key<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getKey() {
        return getColumnParam("Key");
    }

    default E setKey(String val) {
        return setColumnParam("Key", val);
    }

    default Column<E, String, Key> colKey() {
        return new Column<E, String, Key>() {

            @Override
            public String getColumnName() {
                return "Key";
            }

            @Override
            public String getFieldName() {
                return "Key";
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
            public String getValue(Key entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Key");
                return entity.getKey();
            }

            @Override
            public Key setValue(Key entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Key");
                return (Key) entity.setKey(param);
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
