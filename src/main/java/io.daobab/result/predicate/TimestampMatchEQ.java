package io.daobab.result.predicate;

import java.sql.Timestamp;
import java.util.TimeZone;

public class TimestampMatchEQ implements WherePredicate<Object> {

    protected final Timestamp valueToCompare;

    public TimestampMatchEQ(Object valueToCompare) {
        this.valueToCompare = (Timestamp) valueToCompare;
    }


    public boolean test(Object valueFromEntityField) {
        if (valueFromEntityField == null) {
            return false;
        }
        Timestamp dateValueFromEntity = toTimeZone((Timestamp) valueFromEntityField, TimeZone.getDefault());
        return valueToCompare.equals(dateValueFromEntity);
    }

    protected Timestamp toTimeZone(Timestamp timestamp, TimeZone timeZone) {
        if (timestamp == null) return null;
        return new Timestamp(timestamp.getTime() - timeZone.getOffset(timestamp.getTime()));
    }

}
