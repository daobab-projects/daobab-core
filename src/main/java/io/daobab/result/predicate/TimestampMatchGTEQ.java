package io.daobab.result.predicate;

import java.sql.Timestamp;
import java.util.TimeZone;

public class TimestampMatchGTEQ extends TimestampMatchEQ {


    public TimestampMatchGTEQ(Object valueToCompare) {
        super(valueToCompare);
    }


    public boolean test(Object valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        Timestamp dateValueFromEntity = toTimeZone((Timestamp) valueFromEntityField, TimeZone.getDefault());
        return valueToCompare.equals(dateValueFromEntity) || valueToCompare.before(dateValueFromEntity);
    }


}
