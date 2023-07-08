package io.daobab.converter.json.conversion;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.error.DaobabException;
import io.daobab.model.Field;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;
import io.daobab.target.buffer.single.Plates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlateJsonConversion {


    private static final int NOTHING = 0;
    private static final int KEY_OPENED = 1;
    private static final int KEY_CLOSED = 2;
    private static final int KEY_VAL_SEPARATOR = 3;
    private static final int VAL_OPENED = 4;
    private static final int VAL_CLOSED = 5;


    @SuppressWarnings("rawtypes")
    private final Map<String, FieldJsonConversion> fieldJsonConversions;

    @SuppressWarnings("rawtypes")
    private final List<Field> fields;

    public PlateJsonConversion(List<TableColumn> fields1) {
        this(fields1, JsonConverterManager.INSTANCE);
    }

    public PlateJsonConversion(Plate plate) {
        this(plate.columns(), JsonConverterManager.INSTANCE);
    }

    public PlateJsonConversion(Plate plate, JsonConverterManager jsonConverterManager) {
        this(plate.columns(), jsonConverterManager);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PlateJsonConversion(List<TableColumn> fields1, JsonConverterManager jsonConverterManager) {
        fields = fields1.stream().map(TableColumn::getColumn).collect(Collectors.toList());
        fieldJsonConversions = fields1.stream().map(TableColumn::getColumn).collect(Collectors.toMap(Field::getFieldName, c -> new FieldJsonConversion(c, jsonConverterManager)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public StringBuilder toJson(StringBuilder sb, Plate entity) {
        if (entity == null) return sb;
        int maxSizeMinus1 = fields.size() - 1;
        sb.append("{");
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            FieldJsonConversion fieldJsonConversion = fieldJsonConversions.get(i);
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
            jsonConverters.put(fields.get(i).getFieldName(), fieldJsonConversions.get(i));
        }
        return jsonConverters;
    }


    @SuppressWarnings("rawtypes")
    public Plate fromJsonMap(Map<String, String> mapString) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, FieldJsonConversion> fieldJsonConversion : fieldJsonConversions.entrySet()) {
            String fieldName = fieldJsonConversion.getKey();
            map.put(fieldName, fieldJsonConversion.getValue().fromJson(mapString.get(fieldName)));
        }
        return null;//EntityCreator.createEntity(map);
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
                } else if (state == VAL_OPENED && znak == '\"') {
                    state = VAL_CLOSED;
                }

                if (state == KEY_VAL_SEPARATOR || state == VAL_CLOSED && (znak == ',')) {
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

    private void putKeys(StringBuilder key, StringBuilder value, Map<String, String> hashMap) {
        String keyString = key.toString();
        if (keyString.isEmpty()) {
            throw new DaobabException("Problem during json conversion. Cannot find a key");
        }
        String valueString = value.toString();
        hashMap.put(keyString, valueString.equals("null") ? null : valueString);
    }

}
