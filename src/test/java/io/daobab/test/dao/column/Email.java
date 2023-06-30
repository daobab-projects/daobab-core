package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Email<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: EMAIL,
     * db type: VARCHAR
     */
    default String getEmail() {
        return getColumnParam("Email");
    }

    default E setEmail(String val) {
        return setColumnParam("Email", val);
    }

    default Column<E, String, Email> colEmail() {
        return new Column<E, String, Email>() {

            @Override
            public String getColumnName() {
                return "EMAIL";
            }

            @Override
            public String getFieldName() {
                return "Email";
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
            public String getValue(Email entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "Email");
                return entity.getEmail();
            }

            @Override
            public Email setValue(Email entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "Email");
                return (Email) entity.setEmail(param);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return getEntityClass().getName() + "." + getFieldName();
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
