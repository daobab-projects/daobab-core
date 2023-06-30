package io.daobab.model;

import io.daobab.converter.json.conversion.FieldJsonConversion;
import io.daobab.error.DaobabException;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlatPlateImpl extends HashMap<String, Object> implements FlatPlate {

    final Map<String, FieldJsonConversion> plateJsonConversion;

    public FlatPlateImpl(Map<String, FieldJsonConversion> plateJsonConversion) {
        this.plateJsonConversion = plateJsonConversion;
    }
//
//    @Override
//    public String getEntityName() {
//        return this.getClass().getSimpleName();
//    }

    @Override
    public List<TableColumn> columns() {
        throw new DaobabException("FlatPlate has no columns");
    }

    public void fromPlate(Plate plate) {
        plate.toFlatPlate(this);
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> entry : entrySet()) {
            String key = entry.getKey();
            sb.append(key).append(":");
            plateJsonConversion.get(key).toJson(sb, entry.getValue());
        }
        return sb.toString();
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return this.getClass();
    }

    @Override
    public <T extends Target & QueryHandler> void beforeInsert(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void beforeUpdate(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void beforeDelete(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterSelect(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterInsert(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterUpdate(T target) {

    }

    @Override
    public <T extends Target & QueryHandler> void afterDelete(T target) {

    }
}
