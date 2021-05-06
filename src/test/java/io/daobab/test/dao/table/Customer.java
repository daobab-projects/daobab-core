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
public class Customer extends Table implements
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
    public String getEntityName() {
        return "CUSTOMER";
    }

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

    @Override
    public Customer clone() {
        return EntityDuplicator.cloneEntity(this);
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