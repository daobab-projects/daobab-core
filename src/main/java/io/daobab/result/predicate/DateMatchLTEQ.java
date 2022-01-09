package io.daobab.result.predicate;

import java.util.Date;
import java.util.TimeZone;

public class DateMatchLTEQ extends DateMatchEQ {

    public DateMatchLTEQ(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        Date dateValueFromEntity = toTimeZone((Date) valueFromEntityField, TimeZone.getDefault());
        return valueToCompare.equals(dateValueFromEntity) || valueToCompare.after(dateValueFromEntity);
    }


}
