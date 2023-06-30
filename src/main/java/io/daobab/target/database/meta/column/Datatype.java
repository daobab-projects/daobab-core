package io.daobab.target.database.meta.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;
import io.daobab.target.database.connection.JdbcType;

import java.util.Objects;

public interface Datatype<E extends EntityMap> extends EntityRelationMap<E> {

    default JdbcType getDatatype() {
        return getColumnParam("Datatype");
    }

    default E setDatatype(JdbcType val) {
        return setColumnParam("Datatype", val);
    }

    default Column<E, JdbcType, Datatype> colDatatype() {
        return new Column<E, JdbcType, Datatype>() {

            @Override
            public String getColumnName() {
                return "DATATYPE";
            }

            @Override
            public String getFieldName() {
                return "Datatype";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<JdbcType> getFieldClass() {
                return JdbcType.class;
            }

            @Override
            public JdbcType getValue(Datatype entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Datatype");
                return entity.getDatatype();
            }

            @Override
            public Datatype setValue(Datatype entity, JdbcType param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Datatype");
                return (Datatype) entity.setDatatype(param);
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
