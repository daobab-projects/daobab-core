package io.daobab.target.database.converter.standard;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.converter.type.TypeConverterPKBased;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardTypeConverterPrimaryKeyEntity<F, E extends Entity & PrimaryKey<E, F, ?>> implements TypeConverterPKBased<F, E> {

    private final DatabaseTypeConverter<F, E> pkTypeConverter;
    private final E table;
    private final DataBaseTarget dataBaseTarget;

    public StandardTypeConverterPrimaryKeyEntity(DataBaseTarget dataBaseTarget, DatabaseTypeConverter<F, E> pkTypeConverter, E table) {
        this.pkTypeConverter = pkTypeConverter;
        this.table = table;
        this.dataBaseTarget = dataBaseTarget;
    }

    public StandardTypeConverterPrimaryKeyEntityList toMany() {
        return new StandardTypeConverterPrimaryKeyEntityList(dataBaseTarget, pkTypeConverter, table);
    }

    @Override
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return pkTypeConverter.readFromResultSet(rs, columnIndex);
    }

    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        F pkValue = pkTypeConverter.readFromResultSet(rs, columnIndex);
        return convertReadingTarget(pkValue);
    }


    @Override
    public E convertReadingTarget(F from) {
        return getDataBaseTarget().select(getTable()).whereEqual(getTable().colID(), from).findOne();
    }

    @Override
    public String convertWritingTarget(E to) {
        return pkTypeConverter.convertWritingTarget(to);
    }


    public E getTable() {
        return table;
    }

    public DataBaseTarget getDataBaseTarget() {
        return dataBaseTarget;
    }
}
