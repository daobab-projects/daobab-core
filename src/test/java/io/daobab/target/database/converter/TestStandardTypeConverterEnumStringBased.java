package io.daobab.target.database.converter;

import io.daobab.error.EnumCannotBeFound;
import io.daobab.target.database.converter.enums.StringBasedEnum;
import io.daobab.target.database.converter.standard.StandardTypeConverterEnumStringBased;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the String-value based enum converter: an enum is mapped to and from its custom
 * {@link StringBasedEnum#getValue()} (not its {@code name()}).
 */
class TestStandardTypeConverterEnumStringBased {

    private final StandardTypeConverterEnumStringBased<Priority> converter =
            new StandardTypeConverterEnumStringBased<>(Priority.class);

    @Test
    void readsByCustomValueNotByName() {
        assertEquals(Priority.LOW, converter.convertReadingTarget("L"));
        assertEquals(Priority.HIGH, converter.convertReadingTarget("H"));
    }

    @Test
    void writesTheCustomValue() {
        //inline literal is quoted, the bound parameter is the raw value
        assertEquals("'H'", converter.convertWritingTarget(Priority.HIGH));
        assertEquals("H", converter.convertWritingParameter(Priority.HIGH));
    }

    @Test
    void nullHandling() {
        assertNull(converter.convertReadingTarget(null));
        assertNull(converter.convertWritingTarget(null));
        assertNull(converter.convertWritingParameter(null));
    }

    @Test
    void unknownValueThrows() {
        //the enum name is not a valid stored value
        assertThrows(EnumCannotBeFound.class, () -> converter.convertReadingTarget("LOW"));
        assertThrows(EnumCannotBeFound.class, () -> converter.convertReadingTarget("X"));
    }

    private enum Priority implements StringBasedEnum {
        LOW("L"),
        HIGH("H");

        private final String value;

        Priority(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}
