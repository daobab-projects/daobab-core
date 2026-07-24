package io.daobab.model;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;

import java.util.Objects;

/**
 * Mixes in the {@code DUMMY} (VARCHAR) column - the single scratch column carried by pseudo-tables such as
 * {@link Dual}.
 *
 * @param <E> the entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface Dummy<E extends Entity> extends RelatedTo<E>, MapHandler<E>, Entity {

    /**
     * The dummy value.
     */
    default String getDummy() {
        return readParam("Dummy");
    }

    /** Sets the dummy value, returning a new entity. */
    default E setDummy(String val) {
        return storeParam("Dummy", val);
    }

    /** The {@code DUMMY} column handle. */
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
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Dummy");
                return entity.getDummy();
            }

            @Override
            public Dummy setValue(Dummy entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Dummy");
                return (Dummy) entity.setDummy(param);
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
