package io.daobab.test.dao.table;

import io.daobab.model.*;
import io.daobab.test.dao.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableName("ADDRESS")
public class Address extends Table<Address> implements
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


    public Address() {
        super();
    }

    public Address(Map<String, Object> parameters) {
        super(parameters);
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
