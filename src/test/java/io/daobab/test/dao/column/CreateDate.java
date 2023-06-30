package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.sql.Timestamp;
import java.util.Objects;

public interface CreateDate<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: CREATE_DATE,
     * db type: TIMESTAMP
     */
    default Timestamp getCreateDate() {
        return getColumnParam("CreateDate");
    }

    default E setCreateDate(Timestamp val) {
        return setColumnParam("CreateDate", val);
    }

    default Column<E, Timestamp, CreateDate> colCreateDate() {
        return new Column<E, Timestamp, CreateDate>() {

            @Override
            public String getColumnName() {
                return "CREATE_DATE";
            }

            @Override
            public String getFieldName() {
                return "CreateDate";
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
            public Timestamp getValue(CreateDate entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "CreateDate");
                return entity.getCreateDate();
            }

            @Override
            public CreateDate setValue(CreateDate entity, Timestamp param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "CreateDate");
                return (CreateDate) entity.setCreateDate(param);
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
