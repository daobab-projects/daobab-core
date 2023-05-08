package io.daobab.target.database.converter.standard;

import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.TypeConverterVoidBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterVoid implements TypeConverterVoidBased<Void> {

    private final DataBaseTarget target;

    public StandardTypeConverterVoid(DataBaseTarget target) {
        this.target = target;
    }

    @Override
    public Void readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Void convertReadingTarget(Void from) {
        return from;
    }

    @Override
    public String convertWritingTarget(Void to) {
        return null;
    }
}
