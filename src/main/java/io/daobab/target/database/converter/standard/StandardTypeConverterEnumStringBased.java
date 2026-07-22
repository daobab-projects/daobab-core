package io.daobab.target.database.converter.standard;

import io.daobab.error.EnumCannotBeFound;
import io.daobab.target.database.converter.enums.StringBasedEnum;
import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Standard converter for enum columns stored by a custom string code, where the enum implements
 * {@link StringBasedEnum}: the database {@code String} is matched against the constants' {@code getValue()} on
 * reading and written back as that quoted code (rather than by the enum name).
 *
 * @param <E> the enum type, carrying a string code
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("rawtypes")
public class StandardTypeConverterEnumStringBased<E extends Enum & StringBasedEnum> extends TypeConverterStringBased<E> {

    private final Class<E> enumClass;

    /**
     * @param enumClass the enum type whose constants back the column
     */
    public StandardTypeConverterEnumStringBased(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Reads the stored code and resolves it to the enum constant, or {@code null} when null/empty.
     */
    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        String value = readFromResultSet(rs, columnIndex);
        if (value == null || value.isEmpty()) return null;
        return convertReadingTarget(value);
    }

    /**
     * Resolves the string code to the constant whose {@code getValue()} matches.
     *
     * @throws EnumCannotBeFound if no constant carries the code
     */
    @Override
    public E convertReadingTarget(String from) {
        if (from == null) return null;
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> from.equals(e.getValue())).findFirst().orElseThrow(EnumCannotBeFound::new);
    }

    /** Renders the constant's string code as a quoted SQL literal, or {@code null}. */
    @SuppressWarnings("unchecked")
    @Override
    public String convertWritingTarget(Enum to) {
        return to == null ? null : "'" + ((E) to).getValue() + "'";
    }

    /** Binds the constant's string code, or {@code null}. */
    @SuppressWarnings("unchecked")
    @Override
    public Object convertWritingParameter(Enum to) {
        return to == null ? null : ((E) to).getValue();
    }
}
