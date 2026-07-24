package io.daobab.model;

import io.daobab.converter.json.conversion.FieldJsonConversion;
import io.daobab.error.DaobabException;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The default {@link FlatPlate}: a {@code HashMap} that also carries the per-field JSON conversions, so it can
 * render itself to JSON. It has no columns and its lifecycle hooks are no-ops.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class FlatPlateImpl extends HashMap<String, Object> implements FlatPlate {

    final Map<String, FieldJsonConversion> plateJsonConversion;

    /**
     * @param plateJsonConversion the per-field JSON conversions, keyed by field name
     */
    public FlatPlateImpl(Map<String, FieldJsonConversion> plateJsonConversion) {
        this.plateJsonConversion = plateJsonConversion;
    }
//
//    @Override
//    public String getEntityName() {
//        return this.getClass().getSimpleName();
//    }

    /**
     * Not supported: a flat plate has no columns.
     */
    @Override
    public List<TableColumn> columns() {
        throw new DaobabException("FlatPlate has no columns");
    }

    /** Fills this flat plate from the given plate. */
    public void fromPlate(Plate plate) {
        plate.toFlatPlate(this);
    }

    /** This flat plate rendered as JSON. */
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

    /** {@inheritDoc} */
    @Override
    public Class<? extends Entity> entityClass() {
        return this.getClass();
    }

    /** {@inheritDoc} No-op. */
    @Override
    public <T extends Target & QueryHandler> void beforeInsert(T target) {

    }

    /** {@inheritDoc} No-op. */
    @Override
    public <T extends Target & QueryHandler> void beforeUpdate(T target) {

    }

    /** {@inheritDoc} No-op. */
    @Override
    public <T extends Target & QueryHandler> void beforeDelete(T target) {

    }

    /** {@inheritDoc} No-op. */
    @Override
    public <T extends Target & QueryHandler> void afterSelect(T target) {

    }

    /** {@inheritDoc} No-op. */
    @Override
    public <T extends Target & QueryHandler> void afterInsert(T target) {

    }

    /** {@inheritDoc} No-op. */
    @Override
    public <T extends Target & QueryHandler> void afterUpdate(T target) {

    }

    /** {@inheritDoc} No-op. */
    @Override
    public <T extends Target & QueryHandler> void afterDelete(T target) {

    }
}
