package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.AddressId;
import io.daobab.test.dao.column.LastUpdate;
import io.daobab.test.dao.column.ManagerStaffId;
import io.daobab.test.dao.column.StoreId;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Store extends Table<Store> implements
        StoreId<Store>,
        ManagerStaffId<Store>,
        AddressId<Store>,
        LastUpdate<Store>,

        PrimaryKey<Store, Integer, StoreId> {


    public Store() {
        super();
    }

    public Store(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public String getEntityName() {
        return "STORE";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colStoreId()),
                new TableColumn(colManagerStaffId()),
                new TableColumn(colAddressId()),
                new TableColumn(colLastUpdate())

        );
    }

    @Override
    public Column<Store, Integer, StoreId> colID() {
        return colStoreId();
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
