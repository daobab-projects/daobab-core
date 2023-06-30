package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.sql.Timestamp;
import java.util.Objects;

public interface LastUpdate<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: LAST_UPDATE,
     * db type: TIMESTAMP
     */
    default Timestamp getLastUpdate() {
        return getColumnParam("LastUpdate");
    }

    default E setLastUpdate(Timestamp val) {
        return setColumnParam("LastUpdate", val);
    }

    default Column<E, Timestamp, LastUpdate> colLastUpdate() {
        return new Column<E, Timestamp, LastUpdate>() {

            @Override
            public String getColumnName() {
                return "LAST_UPDATE";
            }

            @Override
            public String getFieldName() {
                return "LastUpdate";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Timestamp> getFieldClass() {
                return Timestamp.class;
            }

            @Override
            public Timestamp getValue(LastUpdate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "LastUpdate");
                return entity.getLastUpdate();
            }

            @Override
            public LastUpdate setValue(LastUpdate entity, Timestamp param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "LastUpdate");
                return (LastUpdate) entity.setLastUpdate(param);
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
