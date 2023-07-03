package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface KeySeq<E extends Entity> extends RelatedTo<E>, MapHandler<E> {

    default String getKeySeq() {
        return readParam("KeySeq");
    }

    default E setKeySeq(String val) {
        return storeParam("KeySeq", val);
    }

    default Column<E, String, KeySeq> colKeySeq() {
        return new Column<E, String, KeySeq>() {

            @Override
            public String getColumnName() {
                return "KEY_SEQ";
            }

            @Override
            public String getFieldName() {
                return "KeySeq";
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
            public String getValue(KeySeq entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "KeySeq");
                return entity.getKeySeq();
            }

            @Override
            public KeySeq setValue(KeySeq entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "KeySeq");
                return (KeySeq) entity.setKeySeq(param);
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
