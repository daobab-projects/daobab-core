package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.sql.Date;
import java.util.Objects;

public interface ReleaseYear<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: RELEASE_YEAR,
     * db type: DATE
     */
    default Date getReleaseYear() {
        return getColumnParam("ReleaseYear");
    }

    default E setReleaseYear(Date val) {
        return setColumnParam("ReleaseYear", val);
    }

    default Column<E, Date, ReleaseYear> colReleaseYear() {
        return new Column<E, Date, ReleaseYear>() {

            @Override
            public String getColumnName() {
                return "RELEASE_YEAR";
            }

            @Override
            public String getFieldName() {
                return "ReleaseYear";
            }

            @Override
            public E getInstance() {
                return getEntity();
            }

            @Override
            public Class<Date> getFieldClass() {
                return Date.class;
            }

            @Override
            public Date getValue(ReleaseYear entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(getEntityClass(), "ReleaseYear");
                return entity.getReleaseYear();
            }

            @Override
            public ReleaseYear setValue(ReleaseYear entity, Date param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(), "ReleaseYear");
                return (ReleaseYear) entity.setReleaseYear(param);
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
