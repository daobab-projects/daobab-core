package io.daobab.target.database.converter.standard;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.converter.type.TypeConverterPKBasedList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StandardTypeConverterPrimaryKeyEntityList<F, E extends Entity & PrimaryKey<E, F, ?>> implements TypeConverterPKBasedList<F, E> {

    private final DatabaseTypeConverter<F, E> pkTypeConverter;
    private final E table;
    private final DataBaseTarget dataBaseTarget;

    public StandardTypeConverterPrimaryKeyEntityList(DataBaseTarget dataBaseTarget, DatabaseTypeConverter<F, E> pkTypeConverter, E table) {
        this.pkTypeConverter = pkTypeConverter;
        this.table = table;
        this.dataBaseTarget = dataBaseTarget;
    }

    @Override
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return pkTypeConverter.readFromResultSet(rs, columnIndex);
    }

    @Override
    public List<E> readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        F pkValue = pkTypeConverter.readFromResultSet(rs, columnIndex);
        return convertReadingTarget(pkValue);
    }


    @Override
    public List<E> convertReadingTarget(F from) {
        return getDataBaseTarget().select(getTable()).whereEqual(getTable().colID(), from).findMany();
    }

    @Override
    public String convertWritingTarget(List<E> to) {
        return null;//pkTypeConverter.convertWritingTarget(to);
    }


    public E getTable() {
        return table;
    }

    public DataBaseTarget getDataBaseTarget() {
        return dataBaseTarget;
    }
}
