package io.daobab.target.database.converter.type;


import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class TypeConverterPKBasedList<F, E extends Entity & PrimaryKey<?, F, ?>> implements DatabaseTypeConverter<F, List<E>> {

    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    public abstract E getTable();

    public abstract DataBaseTarget getDataBaseTarget();


}
