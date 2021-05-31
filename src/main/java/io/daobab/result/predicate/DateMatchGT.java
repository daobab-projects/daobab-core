package io.daobab.result.predicate;

import java.util.Date;
import java.util.TimeZone;

public class DateMatchGT extends DateMatchEQ {

    public DateMatchGT(Object valueToCompare) {
        super(valueToCompare);
    }

    @Override
    public boolean test(Object valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        Date dateValueFromEntity = toTimeZone((Date) valueFromEntityField, TimeZone.getDefault());
        return valueToCompare.before(dateValueFromEntity);
    }


}
