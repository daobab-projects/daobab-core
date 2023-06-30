package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface PostalCode<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: POSTAL_CODE,
     * db type: VARCHAR
     */
    default String getPostalCode() {
        return getColumnParam("PostalCode");
    }

    default E setPostalCode(String val) {
        return setColumnParam("PostalCode", val);
    }

    default Column<E, String, PostalCode> colPostalCode() {
        return new Column<E, String, PostalCode>() {

            @Override
            public String getColumnName() {
                return "POSTAL_CODE";
            }

            @Override
            public String getFieldName() {
                return "PostalCode";
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
            public String getValue(PostalCode entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "PostalCode");
                return entity.getPostalCode();
            }

            @Override
            public PostalCode setValue(PostalCode entity, String param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "PostalCode");
                return (PostalCode) entity.setPostalCode(param);
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
