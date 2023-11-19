package io.daobab.test.dao.column;

import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.MapHandler;
import io.daobab.model.RelatedTo;

import java.util.Objects;

public interface FilmId<E extends Entity> extends RelatedTo<E>, MapHandler<E> {


    /**
     * db name: FILM_ID,
     * db type: SMALLINT
     */
    default Integer getFilmId() {
        return readParam("FilmId");
    }

    default E setFilmId(Integer val) {
        return storeParam("FilmId", val);
    }

    default Column<E, Integer, FilmId> colFilmId() {
        return new Column<E, Integer, FilmId>() {

            @Override
            public String getColumnName() {
                return "FILM_ID";
            }

            @Override
            public String getFieldName() {
                return "FilmId";
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
            public Integer getValue(FilmId entity) {
                if (entity == null) throw new AttemptToReadFromNullEntityException(entityClass(), "FilmId");
                return entity.getFilmId();
            }

            @Override
            public FilmId setValue(FilmId entity, Integer param) {
                if (entity == null) throw new AttemptToWriteIntoNullEntityException(entityClass(), "FilmId");
                return (FilmId) entity.setFilmId(param);
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
