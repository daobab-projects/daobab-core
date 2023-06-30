package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.CountryId;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Country extends Table<Country> implements
        CountryId<Country>,
        io.daobab.test.dao.column.Country<Country>,
        LastUpdate<Country>,

        PrimaryKey<Country, Integer, CountryId> {

    @Override
    public String getEntityName() {
        return "COUNTRY";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colCountryId()),
                new TableColumn(colCountry()),
                new TableColumn(colLastUpdate())

        );
    }


    public Country() {
        super();
    }

    public Country(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Country, Integer, CountryId> colID() {
        return colCountryId();
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
