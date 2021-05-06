package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.CityId;
import io.daobab.test.dao.column.CountryId;
import io.daobab.test.dao.column.LastUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class City extends Table implements
        CityId<City>,
        io.daobab.test.dao.column.City<City>,
        CountryId<City>,
        LastUpdate<City>,
        io.daobab.test.dao.column.Country<City>,

        PrimaryKey<City, Integer, CityId> {


    @Override
    public String getEntityName() {
        return "CITY";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colCityId()),
                new TableColumn(colCity()),
                new TableColumn(colCountryId()),
                new TableColumn(colLastUpdate())

        );
    }

    @Override
    public City clone() {
        return EntityDuplicator.cloneEntity(this);
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