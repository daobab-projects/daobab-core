package io.daobab.model;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.converter.json.JsonProvider;
import io.daobab.creation.EntityBuilder;
import io.daobab.creation.EntityCreator;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryColumn;
import io.daobab.error.NullParameter;
import io.daobab.statement.function.type.ColumnFunction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Plate extends HashMap<String, Map<String, Object>> implements JsonProvider, ColumnsProvider {

    public transient List<Field> fields;

    public Plate() {
    }

//    public Plate(List<TableColumn> fields, Object[] rowResults) {
//        if (fields == null || fields.isEmpty()) {
//            throw new MandatoryColumn();
//        }
//        this.fields = fields.stream().map(TableColumn::getColumn).collect(Collectors.toList());
//        for (int i = 0; i < fields.size(); i++) {
//            TableColumn c = fields.get(i);
//            setValue(c, rowResults[i]);
//        }
//    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public Plate(Entity entity) {
        this.fields = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList());
        for (Field tableColumn : fields) {
            setValue(tableColumn, tableColumn.getValue((RelatedTo) entity));
        }
    }


    @SuppressWarnings("unchecked")
    public Plate(Plate plate) {
        this.fields = plate.fields();
        for (Field tableColumn : fields) {
            setValue(tableColumn, plate.getValue(tableColumn));
        }
    }

    @SuppressWarnings("unchecked")
    public Plate(Plate plate, boolean quickCopy) {
        this.fields = plate.fields();
        if (quickCopy) {
            putAll(plate);
        } else {
            for (Field tableColumn : fields) {
                setValue(tableColumn, plate.getValue(tableColumn));
            }
        }
    }

    public Plate(Collection<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new MandatoryColumn();
        }
        this.fields = new ArrayList<>(fields);
        fields.forEach(c -> setValue(c, null));
    }

    public List<Field> fields() {
        return fields;
    }

    @Override
    public List<TableColumn> columns() {
        return fields.stream()
                .filter(f -> f instanceof TableColumn)
                .map(TableColumn.class::cast).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <F> F getValue(Field<?, F, ?> df) {
        if (df == null) return null;
        Map<String, Object> entityMap = get(df.entityClass().getName());
        if (entityMap == null) return null;
        return (F) entityMap.get(df.getFieldName());
    }

    @SuppressWarnings("unchecked")
    public <F> F getFunctionValue(ColumnFunction<?, F, ?, ?> df) {
        if (df == null) return null;
        Map<String, Object> entityMap = get(df.entityClass().getName());
        if (entityMap == null) return null;
        if (df.identifier != null) {
            return (F) entityMap.get(df.identifier);
        } else {
            return (F) entityMap.get(df.getFieldName());
        }
    }

    @SuppressWarnings("unchecked")
    public <F> F getValueOrElse(Field<?, F, ?> df, F defaultValue) {
        if (df == null) return defaultValue;
        Map<String, Object> entityMap = get(df.entityClass().getName());
        if (entityMap == null) return defaultValue;
        F rv = (F) entityMap.get(df.getFieldName());
        if (rv == null) {
            return defaultValue;
        }
        return rv;
    }

    public <F> F getValueOrElse(Field<?, F, ?> df, Class<F> clazz, F defaultValue) {
        if (df == null) return defaultValue;
        Map<String, Object> entityMap = get(df.entityClass().getName());
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

        EntityBuilder<E> builder = EntityCreator.builder(entityClass);

        for (TableColumn column : builder.getEntityColumns()) {
            builder.addRelatedValue(column.getColumn(), (RelatedTo) getValue(column.getColumn()));
        }

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E getEntity(E entity) {
        if (entity == null) throw new NullParameter("entity");
        return (E) getEntity(entity.getClass());
    }

    public <F> void setValue(TableColumn tableColumn, F val) {
        Map<String, Object> entityMap;
        if (tableColumn == null) return;

        String entityName = tableColumn.getColumn().entityClass().getName();

        if (!this.containsKey(entityName)) {
            entityMap = new HashMap<>();
            put(entityName, entityMap);
        } else {
            entityMap = get(entityName);
        }

        entityMap.put(tableColumn.getColumn().getFieldName(), val);
    }

    @SuppressWarnings("rawtypes")
    public <F> void setValue(Field column, F val) {
        Map<String, Object> entityMap;

        if (!this.containsKey(column.entityClass().getName())) {
            entityMap = new HashMap<>();
            put(column.entityClass().getName(), entityMap);
        } else {
            entityMap = get(column.entityClass().getName());
        }

        entityMap.put(column.getFieldName(), val);
    }

    public FlatPlate toFlat() {
        return toFlatPlate(new FlatPlateImpl(JsonConverterManager.INSTANCE.getPlateJsonConverter(this).toFlatJsonConversion()));
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E toEntity(Class<E> entityClass, List<TableColumn> columns) {

        EntityBuilder<E> builder = EntityCreator.builder(entityClass);

        for (TableColumn column : columns) {
            builder.add(column.getColumn(), getValue(column.getColumn()));
        }

        return builder.build();


//        E entity;
//
//        try {
//            entity = targetTypeClass.getDeclaredConstructor().newInstance();
//
//            for (TableColumn col : columns) {
//                if (!col.getColumn().getEntityClass().equals(targetTypeClass)) {
//                    throw new DaobabException("Invalid class");
//                }
//                entity = (E) col.getColumn().setValue((RelatedTo) entity, getValue(col.getColumn()));
////                entity.setColumnParam(col.getColumn().getFieldName(), getValue(col.getColumn()));
//            }
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            throw new DaobabException("Cannot create an Entity from a Plate",e);
//        }
//
//        return entity;
    }

    public <M extends FlatPlate> M toFlatPlate(M flatPlate) {
        if (flatPlate == null) throw new AttemptToWriteIntoNullEntityException();
        values().stream().filter(Objects::nonNull).forEach(flatPlate::putAll);
        return flatPlate;
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

    @Override
    public String toJson() {

        if (isEmpty()) {
            return "[]";
        }

        return JsonConverterManager.INSTANCE.getPlateJsonConverter(this)
                .toJson(new StringBuilder(), this).toString();

    }

}
