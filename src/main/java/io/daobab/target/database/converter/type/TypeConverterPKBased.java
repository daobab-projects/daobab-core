package io.daobab.target.database.converter.type;


import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeConverterPKBased<E extends Entity & PrimaryKey<?, F, ?>, F> extends DatabaseTypeConverter<E, F> {

    default F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }


}
