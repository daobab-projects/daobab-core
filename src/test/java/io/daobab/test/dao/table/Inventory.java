package io.daobab.test.dao.table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.model.Table;
import io.daobab.model.TableColumn;
import io.daobab.test.dao.column.FilmId;
import io.daobab.test.dao.column.InventoryId;
import io.daobab.test.dao.column.LastUpdate;
import io.daobab.test.dao.column.StoreId;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class Inventory extends Table<Inventory> implements
        InventoryId<Inventory>,
        FilmId<Inventory>,
        StoreId<Inventory>,
        LastUpdate<Inventory>,

        PrimaryKey<Inventory, BigDecimal, InventoryId> {

    @Override
    public String getEntityName() {
        return "INVENTORY";
    }

    @Override
    public List<TableColumn> columns() {
        return Arrays.asList(
                new TableColumn(colInventoryId()),
                new TableColumn(colFilmId()),
                new TableColumn(colStoreId()),
                new TableColumn(colLastUpdate())

        );
    }


    public Inventory() {
        super();
    }

    public Inventory(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Column<Inventory, BigDecimal, InventoryId> colID() {
        return colInventoryId();
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
