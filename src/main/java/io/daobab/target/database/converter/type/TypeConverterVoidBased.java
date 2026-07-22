package io.daobab.target.database.converter.type;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base converter for pseudo columns that have no database value of their own (e.g. computed or relation
 * columns). Nothing is read from the result set; subclasses only provide the writing side for the type
 * {@code T}.
 *
 * @param <T> the Daobab column type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class TypeConverterVoidBased<T> implements DatabaseTypeConverter<Void, T> {

    /**
     * Reads nothing: this converter has no backing column. Always returns {@code null}.
     */
    public Void readFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }


}
