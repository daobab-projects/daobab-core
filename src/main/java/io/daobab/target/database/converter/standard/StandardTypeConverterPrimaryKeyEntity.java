package io.daobab.target.database.converter.standard;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.converter.type.TypeConverterPKBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterPrimaryKeyEntity<E extends Entity & PrimaryKey<E, F, ?>, F> implements TypeConverterPKBased<E, F> {

    private final DatabaseTypeConverter<F, E> pkTypeConverter;
    private final E table;
    private final DataBaseTarget dataBaseTarget;

    public StandardTypeConverterPrimaryKeyEntity(DataBaseTarget dataBaseTarget, DatabaseTypeConverter<F, E> pkTypeConverter, E table) {
        this.pkTypeConverter = pkTypeConverter;
        this.table = table;
        this.dataBaseTarget = dataBaseTarget;
    }

    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        F pkValue = pkTypeConverter.readAndConvert(rs, columnIndex);
        return convertReadingTarget(pkValue);
    }

    @Override
    public E convertReadingTarget(F from) {
        return dataBaseTarget.select(table).whereEqual(table.colID(), from).findOne();
    }

    @Override
    public String convertWritingTarget(E to) {
        return pkTypeConverter.convertWritingTarget(to.getId());
    }
}
