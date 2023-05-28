package io.daobab.test.dao.table;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.test.dao.column.ActorId;
import io.daobab.test.dao.column.FilmId;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableInformation(name = "FILM_ACTOR")
public class FilmActor extends Table<FilmActor> implements
        ActorId<FilmActor>,
        FilmId<FilmActor>,
        LastUpdate<FilmActor>,

        PrimaryKey<FilmActor, Integer, FilmId> {

    @Override
    public List<TableColumn> columns() {
        return DaobabCache.getTableColumns(this,
                () -> Arrays.asList(
                        new TableColumn(colActorId()),
                        new TableColumn(colFilmId()),
                        new TableColumn(colLastUpdate())
                ));
    }


    public FilmActor() {
        super();
    }

    public FilmActor(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<FilmActor, Integer, FilmId> colID() {
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
