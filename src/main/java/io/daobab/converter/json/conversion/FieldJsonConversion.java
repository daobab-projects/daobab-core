package io.daobab.converter.json.conversion;

import io.daobab.converter.JsonConverter;
import io.daobab.converter.json.JsonConverterManager;
import io.daobab.error.DaobabException;
import io.daobab.model.Field;

import java.util.List;

public class FieldJsonConversion<F> {

    final Field<?, ?, ?> targetField;

    final String fieldName;
    final JsonConverter<F> jsonConverter;

    public FieldJsonConversion(Field<?, F, ?> field) {
        this(field, JsonConverterManager.INSTANCE);
    }

    @SuppressWarnings("unchecked")
    public FieldJsonConversion(Field<?, F, ?> field, JsonConverterManager manager) {
        targetField = field;
        fieldName = "\"" + field.getFieldName() + "\"";
        jsonConverter = (JsonConverter<F>) manager.getConverter(field).orElseThrow(() -> new DaobabException("Cannot find Json converter for %s", field.getFieldClass()));
    }

    public StringBuilder toJson(StringBuilder sb, F value) {
        sb.append(fieldName).append(":");
        if (value == null) {
            sb.append("null");
        } else {
            jsonConverter.toJson(sb, value);
        }
        return sb;
    }

    public StringBuilder toJson(StringBuilder sb, List<F> values) {
        sb.append(fieldName).append(" : [");
        toJsonNoName(sb, values);
        sb.append("]");
        return sb;
    }

    private void toJsonNoName(StringBuilder sb, F value) {
        jsonConverter.toJson(sb, value);
    }

    private void toJsonNoName(StringBuilder sb, List<F> values) {
        int maxSizeMinus1 = values.size() - 1;
        for (int i = 0; i < values.size(); i++) {
            toJsonNoName(sb, values.get(i));
            if (i != maxSizeMinus1) {
                sb.append(",");
            }
        }
    }
}
