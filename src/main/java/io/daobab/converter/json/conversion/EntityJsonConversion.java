package io.daobab.converter.json.conversion;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.creation.EntityCreator;
import io.daobab.error.DaobabException;
import io.daobab.model.Entity;
import io.daobab.model.Field;
import io.daobab.model.RelatedTo;
import io.daobab.model.TableColumn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityJsonConversion<E extends Entity> extends FromJsonContext {


    @SuppressWarnings("rawtypes")
    private final Map<String, FieldJsonConversion> fieldJsonConversions;

    @SuppressWarnings("rawtypes")
    private final List<Field> fields;


    public EntityJsonConversion(E entity) {
        this(entity, JsonConverterManager.INSTANCE);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityJsonConversion(E entity, JsonConverterManager jsonConverterManager) {
        fields = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList());
        fieldJsonConversions = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toMap(Field::getFieldName, c -> new FieldJsonConversion(c, jsonConverterManager)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public StringBuilder toJson(StringBuilder sb, E entity) {
        if (entity == null) return sb;
        int maxSizeMinus1 = fields.size() - 1;
        sb.append("{");
        for (int i = 0; i < fieldJsonConversions.size(); i++) {
            Field field = fields.get(i);
            FieldJsonConversion fieldJsonConversion = fieldJsonConversions.get(field.getFieldName());
            fieldJsonConversion.toJson(sb, field.getValue((RelatedTo) entity));
            if (i != maxSizeMinus1) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb;
    }

    public StringBuilder toJson(StringBuilder sb, List<E> entities) {
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

    public E fromJsonMap(Class<E> entityClass, Map<String, String> mapString) {
        Map<String, Object> map = new HashMap<>();
        for (FieldJsonConversion<?> fieldJsonConversion : fieldJsonConversions.values()) {
            String fieldName = fieldJsonConversion.fieldName;
            map.put(fieldName, fieldJsonConversion.fromJson(mapString.get(fieldName)));
        }
        return EntityCreator.createEntity(entityClass, map);
    }

    public E fromJson(Class<E> entityClass, String sb) {
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

        return fromJsonMap(entityClass, hashMap);
    }


}
