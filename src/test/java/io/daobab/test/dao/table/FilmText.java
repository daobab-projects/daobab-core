package io.daobab.test.dao.table;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.test.dao.column.Description;
import io.daobab.test.dao.column.FilmId;
import io.daobab.test.dao.column.Title;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableInformation(name = "FILM_TEXT")
public class FilmText extends Table<FilmText> implements
        FilmId<FilmText>,
        Title<FilmText>,
        Description<FilmText>,

        PrimaryKey<FilmText, Integer, FilmId> {

    @Override
    public List<TableColumn> columns() {
        return DaobabCache.getTableColumns(this,
                () -> Arrays.asList(
                        new TableColumn(colFilmId()),
                        new TableColumn(colTitle()),
                        new TableColumn(colDescription())
                ));
    }


    public FilmText() {
        super();
    }

    public FilmText(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<FilmText, Integer, FilmId> colID() {
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
