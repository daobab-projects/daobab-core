package io.daobab.converter.json.conversion;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.creation.PlateCreator;
import io.daobab.error.DaobabException;
import io.daobab.model.Field;
import io.daobab.model.Plate;
import io.daobab.target.buffer.single.Plates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlateJsonConversion extends FromJsonContext {


    @SuppressWarnings("rawtypes")
    private final Map<String, FieldJsonConversion> fieldJsonConversions;

    @SuppressWarnings("rawtypes")
    private final List<Field> fields;

    public PlateJsonConversion(List<Field> fields1) {
        this(fields1, JsonConverterManager.INSTANCE);
    }

    public PlateJsonConversion(Plate plate) {
        this(plate.fields(), JsonConverterManager.INSTANCE);
    }

    public PlateJsonConversion(Plate plate, JsonConverterManager jsonConverterManager) {
        this(plate.fields(), jsonConverterManager);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PlateJsonConversion(List<Field> fields, JsonConverterManager jsonConverterManager) {
        this.fields = fields;
        fieldJsonConversions = fields.stream().collect(Collectors.toMap(Field::getFieldName, c -> new FieldJsonConversion(c, jsonConverterManager)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public StringBuilder toJson(StringBuilder sb, Plate entity) {
        if (entity == null) return sb;
        int maxSizeMinus1 = fields.size() - 1;
        sb.append("{");
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            FieldJsonConversion fieldJsonConversion = fieldJsonConversions.get(field.getFieldName());
            fieldJsonConversion.toJson(sb, entity.getValue(field));
            if (i != maxSizeMinus1) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb;
    }

    public StringBuilder toJson(StringBuilder sb, Plates entities) {
        int maxSizeMinus1 = entities.size() - 1;
        sb.append("[");
        for (int i = 0; i < entities.size(); i++) {
            toJson(sb, entities.get(i));
            if (i != maxSizeMinus1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb;
    }

    @SuppressWarnings("rawtypes")
    public Map<String, FieldJsonConversion> toFlatJsonConversion() {
        Map<String, FieldJsonConversion> jsonConverters = new HashMap<>();
        if (fieldJsonConversions.size() < fields.size()) {
            throw new DaobabException("Json converters doesn't match with fields number");
        }
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i).getFieldName();
            jsonConverters.put(fieldName, fieldJsonConversions.get(fieldName));
        }
        return jsonConverters;
    }


    @SuppressWarnings("rawtypes")
    public Plate fromJsonMap(Map<String, String> mapString) {
        Map<Field, Object> map = new HashMap<>();
        for (Map.Entry<String, FieldJsonConversion> fieldJsonConversion : fieldJsonConversions.entrySet()) {
            String fieldName = fieldJsonConversion.getKey();
            Field field = fieldJsonConversion.getValue().getTargetField();
            map.put(field, fieldJsonConversion.getValue().fromJson(mapString.get(fieldName)));
        }
        return PlateCreator.ofFieldMap(map);
    }


    @SuppressWarnings({"java:S3776", "java:S135"})
    public Plate fromJson(String sb) {
        Map<String, String> hashMap = new HashMap<>();
        if (!sb.startsWith("{") || !sb.endsWith("}")) {
            throw new DaobabException("Cannot convert an json array");
        }
        sb = sb.substring(sb.indexOf("{") + 1, sb.lastIndexOf("}"));

        int len = sb.length();
        int i;

        int state = NOTHING;

        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();


        for (i = 0; i < len; i++) {
            char znak = sb.charAt(i);
            if (state == NOTHING && (znak == '\"')) {
                state = KEY_OPENED;
                continue;
            }

            if (state == KEY_OPENED && (znak == '\"')) {
                state = KEY_CLOSED;
                continue;
            }

            if (state == KEY_CLOSED && (znak == ':')) {
                state = KEY_VAL_SEPARATOR;
                continue;
            }

            if (state == KEY_OPENED) {
                key.append(znak);
            }

            if (state == KEY_VAL_SEPARATOR || state == VAL_OPENED || state == VAL_CLOSED) {
                if (state == KEY_VAL_SEPARATOR && znak == '\"') {
                    state = VAL_OPENED;
                    continue;
                } else if (state == VAL_OPENED && znak == '\"') {
                    state = VAL_CLOSED;
                    continue;
                }

                if ((state == KEY_VAL_SEPARATOR || state == VAL_CLOSED) && (znak == ',')) {
                    state = NOTHING;

                    putKeys(key, value, hashMap);
                    key = new StringBuilder();
                    value = new StringBuilder();
                    continue;
                }

                value.append(znak);
            }
        }
        //the last pair
        if (!key.toString().isEmpty()) {
            putKeys(key, value, hashMap);
        }

        return fromJsonMap(hashMap);
    }


}
