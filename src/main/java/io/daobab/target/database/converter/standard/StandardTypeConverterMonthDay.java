package io.daobab.target.database.converter.standard;

import io.daobab.target.database.converter.type.TypeConverterStringBased;

import java.time.MonthDay;

/**
 * Standard converter for {@link MonthDay} columns stored as strings (e.g. {@code --07-22}): parsed on reading,
 * rendered back to its text on writing.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StandardTypeConverterMonthDay extends TypeConverterStringBased<MonthDay> {

    /**
     * Parses the stored string into a {@link MonthDay}, or {@code null}.
     */
    @Override
    public MonthDay convertReadingTarget(String from) {
        return from == null ? null : MonthDay.parse(from);
    }

    /** Renders the {@link MonthDay} as a quoted SQL literal, or {@code null}. */
    @Override
    public String convertWritingTarget(MonthDay to) {
        return StandardTypeConverterString.valueStringToSQL(to == null ? null : to.toString());
    }

    /** Binds the {@link MonthDay} as its string form, or {@code null}. */
    @Override
    public Object convertWritingParameter(MonthDay to) {
        return to == null ? null : to.toString();
    }
}
