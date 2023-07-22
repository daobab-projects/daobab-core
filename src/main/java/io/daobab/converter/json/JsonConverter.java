package io.daobab.converter.json;

import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.target.statistic.column.RelatedEntity;

import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class JsonConverter<F> implements JsonType<F> {

    protected static final String PAUSE = "-";
    protected static final String COLON = ":";
    protected static final String QUOTE = "\"";

    @SuppressWarnings("rawtypes")
    public <E extends Entity> void toJson(StringBuilder sb, Field<E, F, RelatedEntity> field, RelatedEntity entity) {
        F value = field.getValue(entity);
        String fieldName = field.getFieldName();
        sb.append(fieldName).append(":");//
        if (value == null){
            sb.append("null");
        }else{
            toJson(sb,value);
        }
    }

    protected void appendDate(StringBuilder sb,int year, int month,int day){
        sb.append(year);
        sb.append(PAUSE);
        if (month<10){
            sb.append("0");
        }
        sb.append(month);
        sb.append(PAUSE);
        if (day<10){
            sb.append("0");
        }
        sb.append(day);
    }

    protected void appendTime(StringBuilder sb,int hour, int minute,int second,long nano){
        if (hour<10){
            sb.append("0");
        }
        sb.append(hour);
        sb.append(COLON);
        if (minute<10){
            sb.append("0");
        }
        sb.append(minute);
        sb.append(COLON);
        if (second<10){
            sb.append("0");
        }
        sb.append(second);
        sb.append(".");

        long milliseconds= TimeUnit.NANOSECONDS.toMillis(nano);

        if (milliseconds<100){
            if (milliseconds < 10) {
                sb.append("00");
            } else {
                sb.append("0");
            }
        }
        sb.append(milliseconds);
    }

    protected void appendTimeZone(StringBuilder sb, ZoneOffset offset) {
        sb.append(offset.toString());
    }

}
