package io.daobab.test.dao.table;

import io.daobab.model.*;
import io.daobab.test.dao.column.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@TableInformation(name = "STAFF")
public class Staff extends Table<Staff> implements
        StaffId<Staff>,
        FirstName<Staff>,
        LastName<Staff>,
        AddressId<Staff>,
        Picture<Staff>,
        Email<Staff>,
        StoreId<Staff>,
        Active<Staff>,
        Username<Staff>,
        Password<Staff>,
        LastUpdate<Staff>,
        OptimisticConcurrencyForPrimaryKey<Staff, Timestamp, LastUpdate>,
        PrimaryKey<Staff, Integer, StaffId> {

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colStaffId()),
                new TableColumn(colFirstName()),
                new TableColumn(colLastName()),
                new TableColumn(colAddressId()),
                new TableColumn(colPicture()),
                new TableColumn(colEmail()),
                new TableColumn(colStoreId()),
                new TableColumn(colActive()),
                new TableColumn(colUsername()),
                new TableColumn(colPassword()),
                new TableColumn(colLastUpdate())

        );
    }


    public Staff() {
        super();
    }

    public Staff(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Staff, Integer, StaffId> colID() {
        return colStaffId();
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


    @Override
    public Column<Staff, Timestamp, LastUpdate> getOCCColumn() {
        return colLastUpdate();
    }
}
