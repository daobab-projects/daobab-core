package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Payment extends Table<Payment> implements
        PaymentId<Payment>,
        CustomerId<Payment>,
        StaffId<Payment>,
        RentalId<Payment>,
        Amount<Payment>,
        PaymentDate<Payment>,
        LastUpdate<Payment>,

        PrimaryKey<Payment, Integer, PaymentId> {

    @Override
    public String getEntityName() {
        return "PAYMENT";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colPaymentId()),
                new TableColumn(colCustomerId()),
                new TableColumn(colStaffId()),
                new TableColumn(colRentalId()),
                new TableColumn(colAmount()),
                new TableColumn(colPaymentDate()),
                new TableColumn(colLastUpdate())

        );
    }


    public Payment() {
        super();
    }

    public Payment(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Payment, Integer, PaymentId> colID() {
        return colPaymentId();
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
