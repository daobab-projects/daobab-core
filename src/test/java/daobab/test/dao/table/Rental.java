package daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.daobab.clone.EntityDuplicator;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Rental extends Table implements
        RentalId<Rental>,
        RentalDate<Rental>,
        InventoryId<Rental>,
        CustomerId<Rental>,
        ReturnDate<Rental>,
        StaffId<Rental>,
        LastUpdate<Rental>,

        PrimaryKey<Rental, BigDecimal, RentalId> {

    @Override
    public String getEntityName() {
        return "RENTAL";
    }

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

    @Override
    public Rental clone() {
        return EntityDuplicator.cloneEntity(this);
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