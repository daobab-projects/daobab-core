package io.daobab.converter;

import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.target.statistic.column.RelatedEntity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface JsonConverter<F> {

    String toJson(F obj);

    F fromJson(String json);

    default <E extends Entity> void toJson(StringBuilder sb, Field<E, F, RelatedEntity> field, RelatedEntity entity) {
        F value = field.getValue(entity);
        String fieldName = field.getFieldName();
        sb.append(fieldName).append(":").append(value == null ? "null" : toJson(value));
    }


}
