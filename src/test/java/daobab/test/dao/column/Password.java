package daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.EntityMap;
import io.daobab.model.EntityRelationMap;

import java.util.Objects;

public interface Password<E extends EntityMap> extends EntityRelationMap<E> {


    /**
     * db name: PASSWORD,
     * db type: VARCHAR
     */
    default String getPassword() {
        return getColumnParam("Password");
    }

    default E setPassword(String val) {
        setColumnParam("Password", val);
        return (E) this;
    }

    default Column<E, String, Password> colPassword() {
        return new Column<E, String, Password>() {

            @Override
            public String getColumnName() {
                return "PASSWORD";
            }

            @Override
            public String getFieldName() {
                return "Password";
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
            public String getValue(Password entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Password");
                return entity.getPassword();
            }

            @Override
            public void setValue(Password entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Password");
                entity.setPassword(param);
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