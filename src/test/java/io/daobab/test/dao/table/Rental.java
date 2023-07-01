package io.daobab.test.dao.table;

import io.daobab.model.*;
import io.daobab.test.dao.column.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@TableName(value = "RENTAL")
public class Rental extends Table<Rental> implements
        RentalId<Rental>,
        RentalDate<Rental>,
        InventoryId<Rental>,
        CustomerId<Rental>,
        ReturnDate<Rental>,
        StaffId<Rental>,
        LastUpdate<Rental>,

        PrimaryKey<Rental, BigDecimal, RentalId> {

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colRentalId()),
                new TableColumn(colRentalDate()),
                new TableColumn(colInventoryId()),
                new TableColumn(colCustomerId()),
                new TableColumn(colReturnDate()),
                new TableColumn(colStaffId()),
                new TableColumn(colLastUpdate())

        );
    }


    public Rental() {
        super();
    }

    public Rental(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Rental, BigDecimal, RentalId> colID() {
        return colRentalId();
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
