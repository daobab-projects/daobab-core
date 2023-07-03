package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface Length<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: LENGTH,
     * db type: SMALLINT
     */
    default Integer getLength() {
        return readParam("Length");
    }

    default E setLength(Integer val) {
        return storeParam("Length", val);
    }

    default Column<E, Integer, Length> colLength() {
        return new Column<E, Integer, Length>() {

            @Override
            public String getColumnName() {
                return "LENGTH";
            }

            @Override
            public String getFieldName() {
                return "Length";
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
            public Integer getValue(Length entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "Length");
                return entity.getLength();
            }

            @Override
            public Length setValue(Length entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "Length");
                return (Length) entity.setLength(param);
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
