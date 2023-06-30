package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.daobab.clone.EntityDuplicator;
import io.daobab.model.*;
import io.daobab.test.dao.column.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
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
    public String getEntityName() {
        return "STAFF";
    }

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

    @Override
    public Staff clone() {
        return EntityDuplicator.cloneEntity(this);
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
