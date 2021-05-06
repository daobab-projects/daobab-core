package io.daobab.target.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface KeySeq<E extends EntityMap> extends EntityRelationMap<E> {

    default String getKeySeq() {
        return getColumnParam("KeySeq");
    }

    default E setKeySeq(String val) {
        setColumnParam("KeySeq", val);
        return (E) this;
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "KeySeq");
                return entity.getKeySeq();
            }

            @Override
            public void setValue(KeySeq entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "KeySeq");
                entity.setKeySeq(param);
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