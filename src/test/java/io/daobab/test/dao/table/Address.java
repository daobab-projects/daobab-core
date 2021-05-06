package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Address extends Table implements
        AddressId<Address>,
        io.daobab.test.dao.column.Address<Address>,
        Address2<Address>,
        District<Address>,
        CityId<Address>,
        PostalCode<Address>,
        Phone<Address>,
        LastUpdate<Address>,

        PrimaryKey<Address, Integer, AddressId> {

    @Override
    public String getEntityName() {
        return "ADDRESS";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colAddressId()),
                new TableColumn(colAddress()),
                new TableColumn(colAddress2()),
                new TableColumn(colDistrict()),
                new TableColumn(colCityId()),
                new TableColumn(colPostalCode()),
                new TableColumn(colPhone()),
                new TableColumn(colLastUpdate())

        );
    }

    @Override
    public Address clone() {
        return EntityDuplicator.cloneEntity(this);
    }

    @Override
    public Column<Address, Integer, AddressId> colID() {
        return colAddressId();
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