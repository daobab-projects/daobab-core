package io.daobab.result.predicate;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class DateMatchLT extends DateMatchEQ {


    public DateMatchLT(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        Date dateValueFromEntity = toTimeZone((Date) valueFromEntityField, TimeZone.getDefault());
        return valueToCompare.after(dateValueFromEntity);
    }
}
