package io.daobab.model;

import io.daobab.converter.JsonHandler;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.error.ColumnMandatory;
import io.daobab.error.DaobabException;
import io.daobab.error.NullParameter;
import io.daobab.statement.function.type.ColumnFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class Plate extends HashMap<String, Map<String, Object>> implements JsonHandler, ColumnsProvider {

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

    @SuppressWarnings("unchecked")
    public Plate(Plate plate, boolean quickCopy) {
        this.fields = plate.columns();
        if (quickCopy) {
            putAll(plate);
        } else {
            for (TableColumn tableColumn : fields) {
                setValue(tableColumn, plate.getValue(tableColumn.getColumn()));
            }
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
    public <F> F getFunctionValue(ColumnFunction<?, F, ?, ?> df) {
        if (df == null) return null;
        Map<String, Object> entityMap = get(df.getEntityName());
        if (entityMap == null) return null;
        if (df.identifier != null) {
            return (F) entityMap.get(df.identifier);
        } else {
            return (F) entityMap.get(df.getFieldName());
        }
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
        for (String entityKey : this.keySet()) {
            Map<String, Object> entityMap = get(entityKey);
            if (entityMap.containsKey(fieldName)) {
                return (F) entityMap.get(fieldName);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <F> F getValueOrElse(String fieldName, F defaultValue) {
        if (fieldName == null) return defaultValue;
        for (String entityKey : this.keySet()) {
            Map<String, Object> entityMap = get(entityKey);
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

    public <F> void setValue(TableColumn tableColumn, F val) {
        Map<String, Object> entityMap;
        if (tableColumn == null) return;

        if (!this.containsKey(tableColumn.getColumn().getEntityName())) {
            entityMap = new HashMap<>();
            put(tableColumn.getColumn().getEntityName(), entityMap);
        } else {
            entityMap = get(tableColumn.getColumn().getEntityName());
        }

        entityMap.put(tableColumn.getColumn().getFieldName(), val);
    }

    @SuppressWarnings("rawtypes")
    public <F> void setValue(Column column, F val) {
        Map<String, Object> entityMap;

        if (!this.containsKey(column.getEntityName())) {
            entityMap = new HashMap<>();
            put(column.getEntityName(), entityMap);
        } else {
            entityMap = get(column.getEntityName());
        }

        entityMap.put(column.getFieldName(), val);
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

            Map<String, Object> otherPlateColumns = otherPlateEntry.getValue();
            Map<String, Object> thisPlateColumns = get(otherPlateEntry.getKey());

            for (Entry<String, Object> otherPlateColumn : otherPlateColumns.entrySet()) {
                thisPlateColumns.putIfAbsent(otherPlateColumn.getKey(), otherPlateColumn.getValue());
            }
        }
    }

    public void maskPlate(Plate mask) {
        for (Entry<String, Map<String, Object>> otherPlateEntry : mask.entrySet()) {
            if (!this.containsKey(otherPlateEntry.getKey())) {
                remove(otherPlateEntry.getKey());
                continue;
            }

            Map<String, Object> maskColumns = otherPlateEntry.getValue();
            Map<String, Object> thisPlateColumns = get(otherPlateEntry.getKey());

            for (Entry<String, Object> maskColumn : maskColumns.entrySet()) {
                if (!thisPlateColumns.containsKey(maskColumn.getKey())) {
                    thisPlateColumns.remove(otherPlateEntry.getKey());
                }
            }
        }
    }

}
