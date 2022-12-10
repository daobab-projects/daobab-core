package io.daobab.target.database.converter.type;


import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterPKBasedList<F, E extends Entity & PrimaryKey<?, F, ?>> extends DatabaseTypeConverterList<F, E> {

    default F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    E getTable();

    DataBaseTarget getDataBaseTarget();


}
