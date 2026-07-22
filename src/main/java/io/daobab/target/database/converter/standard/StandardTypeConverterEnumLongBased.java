package io.daobab.target.database.converter.standard;

import io.daobab.error.EnumCannotBeFound;
import io.daobab.target.database.converter.enums.LongBasedEnum;
import io.daobab.target.database.converter.type.TypeConverterLongBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Standard converter for enum columns stored by a long code, where the enum implements {@link LongBasedEnum}:
 * the database {@code Long} is matched against the constants' {@code getValue()} on reading and written back as
 * that code.
 *
 * @param <E> the enum type, carrying a long code
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("rawtypes")
public class StandardTypeConverterEnumLongBased<E extends Enum & LongBasedEnum> extends TypeConverterLongBased<E> {

    private final Class<E> enumClass;

    /**
     * @param enumClass the enum type whose constants back the column
     */
    public StandardTypeConverterEnumLongBased(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Reads the stored code and resolves it to the enum constant, or {@code null}.
     */
    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        Long value = readFromResultSet(rs, columnIndex);
        if (value == null) return null;
        return convertReadingTarget(value);
    }

    /**
     * Resolves the long code to the constant whose {@code getValue()} matches.
     *
     * @throws EnumCannotBeFound if no constant carries the code
     */
    @Override
    public E convertReadingTarget(Long from) {
        if (from == null) return null;
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> from.equals(e.getValue())).findFirst().orElseThrow(EnumCannotBeFound::new);
    }

    /** Renders the constant's long code as text, or {@code null}. */
    @SuppressWarnings("unchecked")
    @Override
    public String convertWritingTarget(Enum to) {
        return to == null ? null : String.valueOf(((E) to).getValue());
    }

    /** Binds the constant's long code, or {@code null}. */
    @SuppressWarnings("unchecked")
    @Override
    public Object convertWritingParameter(Enum to) {
        return to == null ? null : ((E) to).getValue();
    }
}
