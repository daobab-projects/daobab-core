package io.daobab.target.database.converter.standard;

import io.daobab.error.EnumCannotBeFound;
import io.daobab.target.database.converter.enums.IntBasedEnum;
import io.daobab.target.database.converter.type.TypeConverterIntegerBased;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class StandardTypeConverterEnumIntBased<E extends Enum & IntBasedEnum> extends TypeConverterIntegerBased<E> {

    private final Class<E> enumClass;

    public StandardTypeConverterEnumIntBased(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E readAndConvert(ResultSet rs, int columnIndex) throws SQLException {
        Integer value = readFromResultSet(rs, columnIndex);
        if (value == null) return null;
        return convertReadingTarget(value);
    }

    @Override
    public E convertReadingTarget(Integer from) {
        if (from == null) return null;
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> from.equals(e.getValue())).findFirst().orElseThrow(EnumCannotBeFound::new);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convertWritingTarget(Enum to) {
        return to == null ? null : String.valueOf(((E) to).getValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object convertWritingParameter(Enum to) {
        return to == null ? null : ((E) to).getValue();
    }
}
