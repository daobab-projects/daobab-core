package io.daobab.converter.duplicator;

import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.target.statistic.column.RelatedEntity;

import java.util.concurrent.TimeUnit;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class Duplicator<F> {



    public abstract F duplicate(F obj);


//    @SuppressWarnings("rawtypes")
//    public <E extends Entity> void toJson(StringBuilder sb, Field<E, F, RelatedEntity> field, RelatedEntity entity) {
//        F value = field.getValue(entity);
//        String fieldName = field.getFieldName();
//        sb.append(fieldName).append(":");//
//        if (value == null){
//            sb.append("null");
//        }else{
//            toJson(sb,value);
//        }
//    }




}
