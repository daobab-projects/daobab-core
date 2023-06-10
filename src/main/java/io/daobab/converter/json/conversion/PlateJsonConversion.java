package io.daobab.converter.json.conversion;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.error.DaobabException;
import io.daobab.model.*;
import io.daobab.target.buffer.single.Plates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlateJsonConversion {

    private final List<FieldJsonConversion> fieldJsonConversions;
    private final List<Field> fields;

    public PlateJsonConversion(List<TableColumn> fields1) {
        this(fields1,JsonConverterManager.INSTANCE);
    }

    public PlateJsonConversion(Plate plate) {
        this(plate.columns(),JsonConverterManager.INSTANCE);
    }

    public PlateJsonConversion(Plate plate,JsonConverterManager jsonConverterManager) {
        this(plate.columns(),jsonConverterManager);
    }

    public PlateJsonConversion(List<TableColumn> fields1,JsonConverterManager jsonConverterManager) {
        fields = fields1.stream().map(TableColumn::getColumn).collect(Collectors.toList());
        fieldJsonConversions = fields1.stream().map(TableColumn::getColumn).map(c -> new FieldJsonConversion(c, jsonConverterManager)).collect(Collectors.toList());
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    public StringBuilder toJson(StringBuilder sb, Plate entity) {
        if (entity==null) return sb;
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
            toJson(sb,entities.get(i));
            if (i != maxSizeMinus1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb;
    }

    public Map<String,FieldJsonConversion> toFlatJsonConversion(){
        Map<String,FieldJsonConversion> jsonConverters=new HashMap<>();
        if (fieldJsonConversions.size()<fields.size()){
            throw new DaobabException("Json converters doesn't match with fields number");
        }
        for (int i = 0; i < fields.size(); i++) {
            jsonConverters.put(fields.get(i).getFieldName(), fieldJsonConversions.get(i));
        }
        return jsonConverters;
    }
}
