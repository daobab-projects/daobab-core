package io.daobab.target.database.converter.standard;

import io.daobab.error.EnumCannotBeFound;
import io.daobab.target.database.converter.enums.StringBasedEnum;
import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class StandardTypeConverterEnumStringBased<E extends Enum & StringBasedEnum> extends TypeConverterStringBased<E> {

    private final Class<E> enumClass;

    public StandardTypeConverterEnumStringBased(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        String value = readFromResultSet(rs, columnIndex);
        if (value == null || value.isEmpty()) return null;
        return convertReadingTarget(value);
    }

    @Override
    public E convertReadingTarget(String from) {
        if (from == null) return null;
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> from.equals(e.getValue())).findFirst().orElseThrow(EnumCannotBeFound::new);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convertWritingTarget(Enum to) {
        return to == null ? null : "'" + ((E) to).getValue() + "'";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object convertWritingParameter(Enum to) {
        return to == null ? null : ((E) to).getValue();
    }
}
