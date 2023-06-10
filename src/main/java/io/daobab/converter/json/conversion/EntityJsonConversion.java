package io.daobab.converter.json.conversion;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.Field;
import io.daobab.model.TableColumn;

import java.util.List;
import java.util.stream.Collectors;

public class EntityJsonConversion<E extends Entity> {

    private final List<FieldJsonConversion> fieldJsonConversions;
    private final List<Field> fields;



    public EntityJsonConversion(E entity) {
        this(entity,JsonConverterManager.INSTANCE);}

    public EntityJsonConversion(E entity,JsonConverterManager jsonConverterManager) {
        fields = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList());
        fieldJsonConversions = entity.columns().stream().map(TableColumn::getColumn).map(c -> new FieldJsonConversion(c, jsonConverterManager)).collect(Collectors.toList());
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public StringBuilder toJson(StringBuilder sb, E entity) {
        if (entity==null) return sb;
        int maxSizeMinus1 = fields.size() - 1;
        sb.append("{");
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            FieldJsonConversion fieldJsonConversion = fieldJsonConversions.get(i);
            fieldJsonConversion.toJson(sb, field.getValue((EntityRelation) entity));
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
            toJson(sb,entities.get(i));
            if (i != maxSizeMinus1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb;
    }
}
