package io.daobab.model;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;

import java.util.Objects;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface Dummy<E extends Entity> extends EntityRelationMap<E>, EntityMap {

    /**
     * db name: DUMMY,
     * db type: VARCHAR
     */
    default String getDummy() {
        return getColumnParam("Dummy");
    }

    @SuppressWarnings("unchecked")
    default E setDummy(String val) {
        setColumnParam("Dummy", val);
        return (E) this;
    }

    @SuppressWarnings("rawtypes")
    default Column<E, String, Dummy> colDummy() {
        return new Column<E, String, Dummy>() {

            @Override
            public String getColumnName() {
                return "DUMMY";
            }

            @Override
            public String getFieldName() {
                return "Dummy";
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
            public String getValue(Dummy entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Dummy");
                return entity.getDummy();
            }

            @Override
            public void setValue(Dummy entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Dummy");
                entity.setDummy(param);
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
