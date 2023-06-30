package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.CategoryId;
import io.daobab.test.dao.column.FilmId;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class FilmCategory extends Table<FilmCategory> implements
        FilmId<FilmCategory>,
        CategoryId<FilmCategory>,
        LastUpdate<FilmCategory>,

        PrimaryKey<FilmCategory, Integer, FilmId> {

    @Override
    public String getEntityName() {
        return "FILM_CATEGORY";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colFilmId()),
                new TableColumn(colCategoryId()),
                new TableColumn(colLastUpdate())

        );
    }


    public FilmCategory() {
        super();
    }

    public FilmCategory(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<FilmCategory, Integer, FilmId> colID() {
        return colFilmId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PrimaryKey other = (PrimaryKey) obj;
        return Objects.equals(getId(), other.getId());
    }


}
