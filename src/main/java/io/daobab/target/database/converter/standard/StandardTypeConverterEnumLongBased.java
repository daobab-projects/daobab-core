package io.daobab.target.database.converter.standard;

import io.daobab.error.EnumCannotBeFound;
import io.daobab.target.database.converter.enums.LongBasedEnum;
import io.daobab.target.database.converter.type.TypeConverterLongBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class StandardTypeConverterEnumLongBased<E extends Enum & LongBasedEnum> extends TypeConverterLongBased<E> {

    private final Class<E> enumClass;

    public StandardTypeConverterEnumLongBased(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        Long value = readFromResultSet(rs, columnIndex);
        if (value == null) return null;
        return convertReadingTarget(value);
    }

    @Override
    public E convertReadingTarget(Long from) {
        if (from == null) return null;
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> from.equals(e.getValue())).findFirst().orElseThrow(EnumCannotBeFound::new);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convertWritingTarget(Enum to) {
        return to == null ? null : String.valueOf(((E) to).getValue());
    }
}
