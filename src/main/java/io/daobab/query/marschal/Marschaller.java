package io.daobab.query.marschal;

import io.daobab.error.NoEntitiesIntoTargetException;
import io.daobab.error.TargetMandatoryException;
import io.daobab.generator.DictRemoteKey;
import io.daobab.model.*;
import io.daobab.target.Target;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface Marschaller {

    static <E extends Entity> Map<String, Object> marschallColumnList(List<TableColumn> columnList) {
        Map<String, Object> rv = new HashMap<>();
        if (columnList == null) return rv;
        int counter = 0;
        for (TableColumn col : columnList) {
            rv.put("" + counter, marschallColumnToString(col.getColumn()));
            counter++;
        }
        return rv;
    }

    static MarschalledColumn marschallColumn(TableColumn column) {
        MarschalledColumn rv = new MarschalledColumn();
        rv.setColumnClass(column.getColumn().getFieldName());
        rv.setEntityClass(column.getColumn().getEntityClass().getSimpleName());
        return rv;
    }

    static Map<String, Object> marschallColumnToString(TableColumn column) {
        return marschallColumnToString(column.getColumn());
    }

    static Map<String, Object> marschallColumnToString(Column column) {
        Map<String, Object> rv = new HashMap<>();

        rv.put(DictRemoteKey.FIELD, column.getFieldName());
        rv.put(DictRemoteKey.ENTITY_NAME, column.getInstance().getEntityName());


        return rv;
    }

    static Column<?, ?, ?> fromRemote(Target target, Map<String, Object> map) {
        if (map == null || map.isEmpty()) return null;
        String entity = (String) map.get(DictRemoteKey.ENTITY_NAME);
        String field = (String) map.get(DictRemoteKey.FIELD);

        Entity pointedEntity = fromRemote(target, entity);
        if (pointedEntity==null||pointedEntity.columns()==null) return null;

        for (TableColumn entitycol : pointedEntity.columns()) {
            if (entitycol.getColumn().getFieldName().equals(field)) return entitycol.getColumn();
        }

        return null;
    }

    static Entity fromRemote(Target target, String entity) {
        if (entity == null || entity.isEmpty()) return null;
        if (target == null) throw new TargetMandatoryException();

        for (Entity targetentity : target.getTables()) {
            if (targetentity.getEntityName().equals(entity)) {
                return targetentity;
            }
        }
        return null;

    }

    static MarschalledEntity marschallEntity(Entity entity) {
        MarschalledEntity rv = new MarschalledEntity();
        rv.setEntityClass(entity.getClass().getSimpleName());
        return rv;
    }

    static <E extends Entity> List<TableColumn> unMarschallColumns(Map<String, Object> map, Target target) {
        if (target == null) throw new TargetMandatoryException();
        if (map == null) throw new TargetMandatoryException();
        if (target.getTables() == null || target.getTables().isEmpty()) throw new NoEntitiesIntoTargetException(target);

        int counter = 0;
        List<TableColumn> rv = new LinkedList<>();

        while (true) {
            Object o = map.get("" + counter);
            counter++;
            if (o == null) return rv;
            TableColumn col = unMarschallColumn((Map<String, Object>) o, target);
            if (col != null) {
                rv.add(col);
            }
        }

    }

    static <E extends Entity> TableColumn unMarschallColumn(Map<String, Object> rv, Target target) {
        if (target == null) throw new TargetMandatoryException();
        if (target.getTables() == null || target.getTables().isEmpty()) throw new NoEntitiesIntoTargetException(target);
        String fieldName = (String) rv.get(DictRemoteKey.FIELD);
        String entityName = (String) rv.get(DictRemoteKey.ENTITY_NAME);

        Entity ent = fromRemote(target, entityName);

        for (TableColumn ec : ent.columns()) {
            Column c = ec.getColumn();
            if (c.getFieldName().equals(fieldName)) {
                return ec;
            }
        }
        //TODO: throw
        return null;
    }


}
