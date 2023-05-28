package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.time.LocalDateTime;
import java.util.Objects;

public interface ReleaseYear<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: RELEASE_YEAR,
     * db type: DATE
     */
    default LocalDateTime getReleaseYear() {
        return readParam("ReleaseYear");
    }

    default E setReleaseYear(LocalDateTime val) {
        return storeParam("ReleaseYear", val);
    }

    default Column<E, LocalDateTime, ReleaseYear> colReleaseYear() {
        return new Column<E, LocalDateTime, ReleaseYear>() {

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
            public Class<LocalDateTime> getFieldClass() {
                return LocalDateTime.class;
            }

            @Override
            public LocalDateTime getValue(ReleaseYear entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "ReleaseYear");
                return entity.getReleaseYear();
            }

            @Override
            public ReleaseYear setValue(ReleaseYear entity, LocalDateTime param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "ReleaseYear");
                return (ReleaseYear) entity.setReleaseYear(param);
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
