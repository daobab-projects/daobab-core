package io.daobab.target.database.converter.standard;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.converter.type.TypeConverterPKBased;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Standard converter that resolves a foreign key value into the referenced entity: the key is read with the
 * underlying primary-key converter and the entity is fetched from the target ({@code select ... where id = ?}).
 * Writing delegates back to the primary-key converter, so only the key value is emitted.
 *
 * @param <F> the primary key value type
 * @param <E> the referenced entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterPrimaryKeyEntity<F, E extends Entity & PrimaryKey<E, F, ?>> extends TypeConverterPKBased<F, E> {

    private final DatabaseTypeConverter<F, E> pkTypeConverter;
    private final E table;
    private final DataBaseTarget dataBaseTarget;

    /**
     * @param dataBaseTarget  the target used to fetch the referenced entity
     * @param pkTypeConverter the converter for the key value itself (reading and writing)
     * @param table           the referenced entity
     */
    public StandardTypeConverterPrimaryKeyEntity(DataBaseTarget dataBaseTarget, DatabaseTypeConverter<F, E> pkTypeConverter, E table) {
        this.pkTypeConverter = pkTypeConverter;
        this.table = table;
        this.dataBaseTarget = dataBaseTarget;
    }

    /**
     * The one-to-many counterpart of this converter, resolving the key into a list of entities.
     *
     * @return a list-valued converter over the same key, entity and target
     */
    public StandardTypeConverterPrimaryKeyEntityList toMany() {
        return new StandardTypeConverterPrimaryKeyEntityList(dataBaseTarget, pkTypeConverter, table);
    }

    /**
     * Reads the raw key value with the underlying primary-key converter.
     */
    @Override
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return pkTypeConverter.readFromResultSet(rs, columnIndex);
    }

    /** Reads the key value and resolves it into the referenced entity. */
    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        F pkValue = pkTypeConverter.readFromResultSet(rs, columnIndex);
        return convertReadingTarget(pkValue);
    }


    /** Fetches the referenced entity by its id ({@code select ... where id = from}). */
    @Override
    public E convertReadingTarget(F from) {
        return getDataBaseTarget().select(getTable()).whereEqual(getTable().colID(), from).findOne();
    }

    /** Writes only the key value, delegating to the underlying primary-key converter. */
    @Override
    public String convertWritingTarget(E to) {
        return pkTypeConverter.convertWritingTarget(to);
    }

    /** Binds only the key value, delegating to the underlying primary-key converter. */
    @Override
    public Object convertWritingParameter(E to) {
        return pkTypeConverter.convertWritingParameter(to);
    }


    /** {@inheritDoc} */
    public E getTable() {
        return table;
    }

    /** {@inheritDoc} */
    public DataBaseTarget getDataBaseTarget() {
        return dataBaseTarget;
    }
}
