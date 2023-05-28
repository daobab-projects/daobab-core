package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface ActorId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: ACTOR_ID,
     * db type: SMALLINT
     */
    default Integer getActorId() {
        return readParam("ActorId");
    }

    default E setActorId(Integer val) {
        return storeParam("ActorId", val);
    }

    default Column<E, Integer, ActorId> colActorId() {
        return new Column<E, Integer, ActorId>() {

            @Override
            public String getColumnName() {
                return "ACTOR_ID";
            }

            @Override
            public String getFieldName() {
                return "ActorId";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Integer> getFieldClass() {
                return Integer.class;
            }

            @Override
            public Integer getValue(ActorId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "ActorId");
                return entity.getActorId();
            }

            @Override
            public ActorId setValue(ActorId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "ActorId");
                return (ActorId) entity.setActorId(param);
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
