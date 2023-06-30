package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Film extends Table<Film> implements
        FilmId<Film>,
        Title<Film>,
        Description<Film>,
        ReleaseYear<Film>,
        LanguageId<Film>,
        OriginalLanguageId<Film>,
        RentalDuration<Film>,
        RentalRate<Film>,
        Length<Film>,
        ReplacementCost<Film>,
        Rating<Film>,
        SpecialFeatures<Film>,
        LastUpdate<Film>,

        PrimaryKey<Film, Integer, FilmId> {

    @Override
    public String getEntityName() {
        return "FILM";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colFilmId()).primaryKey(),
                new TableColumn(colTitle()).size(255).notNull(),
                new TableColumn(colDescription()).size(255),
                new TableColumn(colReleaseYear()),
                new TableColumn(colLanguageId()),
                new TableColumn(colOriginalLanguageId()),
                new TableColumn(colRentalDuration()),
                new TableColumn(colRentalRate()).size(10),
                new TableColumn(colLength()),
                new TableColumn(colReplacementCost()).size(10),
                new TableColumn(colRating()).size(5),
                new TableColumn(colSpecialFeatures()).size(54),
                new TableColumn(colLastUpdate())

        );
    }


    public Film() {
        super();
    }

    public Film(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Film, Integer, FilmId> colID() {
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
