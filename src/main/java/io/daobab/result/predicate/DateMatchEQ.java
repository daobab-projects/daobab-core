package io.daobab.result.predicate;

import java.util.Date;
import java.util.TimeZone;

public class DateMatchEQ implements WherePredicate<Object> {

    protected final Date valueToCompare;

    public DateMatchEQ(Object valueToCompare) {
        this.valueToCompare = (Date) valueToCompare;
    }


    public boolean test(Object valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        Date dateValueFromEntity = toTimeZone((Date) valueFromEntityField, TimeZone.getDefault());
        return valueToCompare.equals(dateValueFromEntity);
    }

    protected Date toTimeZone(Date date, TimeZone timeZone) {
        if (date == null) return null;
        if (timeZone == null) return date;
        return new Date(date.getTime() - timeZone.getOffset(date.getTime()));
    }

}
