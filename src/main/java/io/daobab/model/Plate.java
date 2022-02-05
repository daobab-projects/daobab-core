package io.daobab.model;

import io.daobab.converter.JsonMapHierarchicalHandler;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.error.ColumnMandatory;
import io.daobab.error.DaobabException;
import io.daobab.error.NullParameter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class Plate extends HashMap<String, Map<String, Object>> implements JsonMapHierarchicalHandler, ColumnsProvider {

    private transient List<TableColumn> fields;

    public Plate() {
    }

    public Plate(List<TableColumn> fields, Object[] rowResults) {
        if (fields == null || fields.isEmpty()) {
            throw new ColumnMandatory();
        }
        this.fields = fields;
        for (int i = 0; i < fields.size(); i++) {
            TableColumn c = fields.get(i);
            setValue(c, rowResults[i]);
        }
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public Plate(Entity entity) {
        this.fields = entity.columns();
        for (TableColumn tableColumn : fields) {
            setValue(tableColumn, tableColumn.getColumn().getValue((EntityRelation) entity));
        }
    }
    @SuppressWarnings("unchecked")
    public Plate(Plate plate) {
        this.fields = plate.columns();
        for (TableColumn tableColumn : fields) {
            setValue(tableColumn, plate.getValue(tableColumn.getColumn()));
        }
    }

    public Plate(List<TableColumn> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new ColumnMandatory();
        }
        this.fields = fields;
        fields.forEach(c -> setValue(c, null));
    }

    public List<TableColumn> columns() {
        return fields;
    }

    @SuppressWarnings("unchecked")
    public <F> F getValue(Column<?, F, ?> df) {
        if (df == null) return null;
        Map<String, Object> entityMap = get(df.getEntityName());
        if (entityMap == null) return null;
        return (F) entityMap.get(df.getFieldName());
    }

    @SuppressWarnings("unchecked")
    public <F> F getValueOrElse(Column<?, F, ?> df, F defaultValue) {
        if (df == null) return defaultValue;
        Map<String, Object> entityMap = get(df.getEntityName());
        if (entityMap == null) return defaultValue;
        F rv = (F) entityMap.get(df.getFieldName());
        if (rv == null) {
            return defaultValue;
        }
        return rv;
    }

    public <F> F getValueOrElse(Column<?, F, ?> df, Class<F> clazz, F defaultValue) {
        if (df == null) return defaultValue;
        Map<String, Object> entityMap = get(df.getEntityName());
        if (entityMap == null) return defaultValue;
        F rv = clazz.cast(entityMap.get(df.getFieldName()));
        if (rv == null) {
            return defaultValue;
        }
        return rv;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public <F> F getValueIgnoreEntity(Column<?, F, ?> df) {
        if (df == null) return null;
        List<Column> columnsToSet = getColumnIgnoreEntity(df);
        if (columnsToSet.isEmpty()) {
            throw new DaobabException("Plate doesn't contains column " + df.getFieldName());
        } else if (columnsToSet.size() > 1) {
            throw new DaobabException("Plate contains more than one column " + df.getFieldName());
        }
        return (F) getValue(columnsToSet.get(0));
    }

    @SuppressWarnings("rawtypes")
    private List<Column> getColumnIgnoreEntity(Column col) {
        if (col == null) return Collections.emptyList();
        List<Column> rv = new ArrayList<>();
        this.forEach((key, value) -> {
            if (value.containsKey(col.getFieldName())) {
                rv.add(col);
            }
        });
        return rv;
    }

    @SuppressWarnings("unchecked")
    public <F> F getValue(String fieldName) {
        if (fieldName == null) return null;
        for (String entitykey : this.keySet()) {
            Map<String, Object> entityMap = get(entitykey);
            if (entityMap.containsKey(fieldName)) {
                return (F) entityMap.get(fieldName);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <F> F getValueOrElse(String fieldName, F defaultValue) {
        if (fieldName == null) return defaultValue;
        for (String entitykey : this.keySet()) {
            Map<String, Object> entityMap = get(entitykey);
            if (entityMap.containsKey(fieldName)) {
                return (F) entityMap.get(fieldName);
            }
        }
        return defaultValue;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public <E extends Entity> E getEntity(Class<E> entityClass) {
        if (entityClass == null) throw new NullParameter("entityClass");
        E rv = null;
        try {
            rv = entityClass.getDeclaredConstructor().newInstance();

            for (TableColumn column : rv.columns()) {
                column.getColumn().setValue((EntityRelation) rv, getValue(column.getColumn()));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return rv;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E getEntity(E entity) {
        if (entity == null) throw new NullParameter("entity");
        return (E) getEntity(entity.getClass());
    }

    public <F> void setValue(TableColumn c, F val) {
        Map<String, Object> entityMap;

        if (!this.containsKey(c.getColumn().getEntityName())) {
            entityMap = new HashMap<>();
            put(c.getColumn().getEntityName(), entityMap);
        } else {
            entityMap = get(c.getColumn().getEntityName());
        }

        entityMap.put(c.getColumn().getFieldName(), val);
    }

    @SuppressWarnings("rawtypes")
    public <F> void setValue(Column c, F val) {
        Map<String, Object> entityMap;

        if (!this.containsKey(c.getEntityName())) {
            entityMap = new HashMap<>();
            put(c.getEntityName(), entityMap);
        } else {
            entityMap = get(c.getEntityName());
        }

        entityMap.put(c.getFieldName(), val);
    }

    public FlatPlate toFlat() {
        return toFlatPlate(new FlatPlate());
    }

    @SuppressWarnings("unchecked")
    public <E extends EntityMap> E toEntity(Class<E> targetTypeClass, List<TableColumn> columns) {
        E entity = null;

        try {
            entity = targetTypeClass.getDeclaredConstructor().newInstance();

            for (TableColumn col : columns) {
                if (!col.getColumn().getEntityClass().equals(targetTypeClass)) {
                    throw new DaobabException("Invalid class");
                }
                entity.setColumnParam(col.getColumn().getFieldName(), getValue(col.getColumn()));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            //TODO: exception
        }

        return entity;
    }

    public <M extends FlatPlate> M toFlatPlate(M flatProjection) {
        if (flatProjection == null) throw new AttemptToWriteIntoNullEntityException();
        values().stream().filter(Objects::nonNull).forEach(flatProjection::putAll);
        return flatProjection;
    }

    public void joinPlate(Plate otherPlate){
        for (Entry<String,Map<String,Object>> otherPlateEntry:otherPlate.entrySet()){
            if (!this.containsKey(otherPlateEntry.getKey())){
                put(otherPlateEntry.getKey(),otherPlateEntry.getValue());
                continue;
            }

            Map<String,Object> otherPlateColumns=otherPlateEntry.getValue();
            Map<String,Object> thisPlateColumns=get(otherPlateEntry.getKey());

            for (Entry<String,Object> otherPlateColumn:otherPlateColumns.entrySet()){
                thisPlateColumns.putIfAbsent(otherPlateColumn.getKey(), otherPlateColumn.getValue());
            }
        }
    }


}
