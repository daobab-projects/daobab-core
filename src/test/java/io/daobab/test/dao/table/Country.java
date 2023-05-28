package io.daobab.test.dao.table;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.test.dao.column.CountryId;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableInformation(name = "COUNTRY")
public class Country extends Table<Country> implements
        CountryId<Country>,
        io.daobab.test.dao.column.Country<Country>,
        LastUpdate<Country>,

        PrimaryKey<Country, Integer, CountryId> {

    @Override
    public List<TableColumn> columns() {
        return DaobabCache.getTableColumns(this,
                () -> Arrays.asList(
                        new TableColumn(colCountryId()),
                        new TableColumn(colCountry()),
                        new TableColumn(colLastUpdate())
                ));
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
