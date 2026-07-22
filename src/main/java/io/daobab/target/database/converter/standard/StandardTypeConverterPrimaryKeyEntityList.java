package io.daobab.target.database.converter.standard;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.converter.type.TypeConverterPKBasedList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Standard converter that resolves a key value into the list of referenced entities: the key is read with the
 * underlying primary-key converter and every matching entity is fetched from the target
 * ({@code select ... where id = ?}). The one-to-many counterpart of {@link StandardTypeConverterPrimaryKeyEntity}.
 *
 * @param <F> the key value type
 * @param <E> the referenced entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterPrimaryKeyEntityList<F, E extends Entity & PrimaryKey<E, F, ?>> extends TypeConverterPKBasedList<F, E> {

    private final DatabaseTypeConverter<F, E> pkTypeConverter;
    private final E table;
    private final DataBaseTarget dataBaseTarget;

    /**
     * @param dataBaseTarget  the target used to fetch the referenced entities
     * @param pkTypeConverter the converter for the key value itself
     * @param table           the referenced entity
     */
    public StandardTypeConverterPrimaryKeyEntityList(DataBaseTarget dataBaseTarget, DatabaseTypeConverter<F, E> pkTypeConverter, E table) {
        this.pkTypeConverter = pkTypeConverter;
        this.table = table;
        this.dataBaseTarget = dataBaseTarget;
    }

    /**
     * Reads the raw key value with the underlying primary-key converter.
     */
    @Override
    public F readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return pkTypeConverter.readFromResultSet(rs, columnIndex);
    }

    /** Reads the key value and resolves it into the list of referenced entities. */
    @Override
    public List<E> readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        F pkValue = pkTypeConverter.readFromResultSet(rs, columnIndex);
        return convertReadingTarget(pkValue);
    }


    /** Fetches every referenced entity matching the key ({@code select ... where id = from}). */
    @Override
    public List<E> convertReadingTarget(F from) {
        return getDataBaseTarget().select(getTable()).whereEqual(getTable().colID(), from).findMany();
    }

    /** Not writable: an entity list is a read-only projection, so this returns {@code null}. */
    @Override
    public String convertWritingTarget(List<E> to) {
        return null;//pkTypeConverter.convertWritingTarget(to);
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
