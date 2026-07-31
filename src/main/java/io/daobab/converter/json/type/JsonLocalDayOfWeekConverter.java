package io.daobab.converter.json.type;

import io.daobab.converter.json.JsonConverter;

import java.time.DayOfWeek;

/**
 * JSON converter for {@link java.time.DayOfWeek}: written as its quoted numeric value 1-7 (Monday=1)
 * and parsed with {@link java.time.DayOfWeek#of(int)} (numeric, not the enum name).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JsonLocalDayOfWeekConverter extends JsonConverter<DayOfWeek> {


    @Override
    public void toJson(StringBuilder sb, DayOfWeek obj) {
        sb.append(QUOTE).append(obj.getValue());
        sb.append(QUOTE);
    }

    @Override
    public DayOfWeek fromJson(String json) {
        return DayOfWeek.of(Integer.parseInt(json));
    }
}
