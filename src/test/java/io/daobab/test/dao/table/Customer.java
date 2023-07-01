package io.daobab.test.dao.table;

import io.daobab.model.*;
import io.daobab.test.dao.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableName(value = "CUSTOMER")
public class Customer extends Table<Customer> implements
        CustomerId<Customer>,
        StoreId<Customer>,
        FirstName<Customer>,
        LastName<Customer>,
        Email<Customer>,
        AddressId<Customer>,
        Active<Customer>,
        CreateDate<Customer>,
        LastUpdate<Customer>,

        PrimaryKey<Customer, Integer, CustomerId> {

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colCustomerId()),
                new TableColumn(colStoreId()),
                new TableColumn(colFirstName()),
                new TableColumn(colLastName()),
                new TableColumn(colEmail()),
                new TableColumn(colAddressId()),
                new TableColumn(colActive()),
                new TableColumn(colCreateDate()),
                new TableColumn(colLastUpdate())

        );
    }


    public Customer() {
        super();
    }

    public Customer(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Customer, Integer, CustomerId> colID() {
        return colCustomerId();
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
