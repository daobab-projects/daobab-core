package io.daobab.test.dao.table;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.test.dao.column.CityId;
import io.daobab.test.dao.column.CountryId;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableInformation(name = "CITY")
public class City extends Table<City> implements
        CityId<City>,
        io.daobab.test.dao.column.City<City>,
        CountryId<City>,
        LastUpdate<City>,
        io.daobab.test.dao.column.Country<City>,

        PrimaryKey<City, Integer, CityId> {

    @Override
    public List<TableColumn> columns() {
        return DaobabCache.getTableColumns(this,
                () -> Arrays.asList(
                        new TableColumn(colCityId()),
                        new TableColumn(colCity()),
                        new TableColumn(colCountryId()),
                        new TableColumn(colLastUpdate())
                ));
    }


    public City() {
        super();
    }

    public City(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<City, Integer, CityId> colID() {
        return colCityId();
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
