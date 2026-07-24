package io.daobab.model;

import io.daobab.converter.json.JsonConverterManager;
import io.daobab.converter.json.JsonProvider;
import io.daobab.creation.EntityBuilder;
import io.daobab.creation.EntityCreator;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryColumn;
import io.daobab.error.MandatoryEntity;
import io.daobab.statement.function.type.ColumnFunction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A "plate": one row of a multi-entity query result, held as a map from entity class name to that entity's
 * {@code fieldName -> value} map - so the same column name can coexist across joined entities. It exposes typed
 * value accessors ({@link #getValue(Field)}), can rebuild whole entities from its cells ({@link #getEntity}), and
 * can be flattened to a {@link FlatPlate} or serialized to JSON.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Plate extends HashMap<String, Map<String, Object>> implements JsonProvider, ColumnsProvider {

    /**
     * The fields this plate holds.
     */
    public transient List<Field> fields;

    /** An empty plate. */
    public Plate() {
    }

    /** A plate holding the given entity's column values. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Plate(Entity entity) {
        this.fields = entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList());
        for (Field tableColumn : fields) {
            setValue(tableColumn, tableColumn.getValue((RelatedTo) entity));
        }
    }

    /** A deep copy of another plate (value by value). */
    public Plate(Plate plate) {
        this.fields = plate.fields();
        for (Field<?, ?, ?> tableColumn : fields) {
            setValue(tableColumn, plate.getValue(tableColumn));
        }
    }

    /** A copy of another plate: a shallow {@code putAll} when {@code quickCopy}, otherwise value by value. */
    public Plate(Plate plate, boolean quickCopy) {
        this.fields = plate.fields();
        if (quickCopy) {
            putAll(plate);
        } else {
            for (Field<?, ?, ?> tableColumn : fields) {
                setValue(tableColumn, plate.getValue(tableColumn));
            }
        }
    }

    /** An empty plate shaped for the given fields (their values initialized to {@code null}). */
    public Plate(Collection<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new MandatoryColumn();
        }
        this.fields = new ArrayList<>(fields);
        fields.forEach(c -> setValue(c, null));
    }

    /** The fields this plate holds. */
    public List<Field> fields() {
        return fields;
    }

    /** The fields that are {@link TableColumn}s. */
    @Override
    public List<TableColumn> columns() {
        return fields.stream()
                .filter(TableColumn.class::isInstance)
                .map(TableColumn.class::cast).collect(Collectors.toList());
    }

    /** The value of the field, wrapped in an {@link Optional}. */
    public <F> Optional<F> getValueOptional(Field<?, F, ?> df) {
        return Optional.ofNullable(getValue(df));
    }

    /** The value of the field (matched by entity and field name), or {@code null}. */
    @SuppressWarnings("unchecked")
    public <F> F getValue(Field<?, F, ?> df) {
        if (df == null) return null;
        Map<String, Object> entityMap = get(df.entityClass().getName());
        if (entityMap == null) return null;
        return (F) entityMap.get(df.getFieldName());
    }

    /** The value of a function column (by its alias when set, otherwise its field name), or {@code null}. */
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

    /** The value of the field, or {@code defaultValue} when absent or {@code null}. */
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

    /** The value of the field cast to {@code clazz}, or {@code defaultValue} when absent or {@code null}. */
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

    /**
     * The value of the column by its field name across all entities, ignoring the column's own entity.
     *
     * @throws DaobabException when the plate holds no such column, or more than one
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
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

    /** The first value found under the given field name (across all entities), or {@code null}. */
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

    /** The first value found under the given field name, or {@code defaultValue} when absent. */
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

    /** Rebuilds a whole entity of the given class from this plate's cells. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity> E getEntity(Class<E> entityClass) {
        if (entityClass == null) throw new MandatoryEntity();

        EntityBuilder<E> builder = EntityCreator.builder(entityClass);

        for (TableColumn column : builder.getEntityColumns()) {
            builder.addRelatedValue(column.getColumn(), (RelatedTo) getValue(column.getColumn()));
        }
        return builder.build();
    }

    /** Rebuilds a whole entity of the given instance's class from this plate's cells. */
    @SuppressWarnings("unchecked")
    public <E extends Entity> E getEntity(E entity) {
        if (entity == null) throw new MandatoryEntity();
        return (E) getEntity(entity.getClass());
    }

    /** Sets the value of the column of a {@link TableColumn}. */
    public <F> void setValue(TableColumn tableColumn, F val) {
        if (tableColumn == null) return;
        Column<?, ?, ?> column = tableColumn.getColumn();
        setValue(column, val);
    }

    /** Sets the value of a field (under its entity name and field name). */
    @SuppressWarnings("rawtypes")
    public <F> void setValue(Field column, F val) {
        String entityName = column.entityClass().getName();
        computeIfAbsent(entityName, x -> new HashMap<>())
                .put(column.getFieldName(), val);
    }

    /** This plate flattened into a {@link FlatPlate}. */
    public FlatPlate toFlat() {
        return toFlatPlate(new FlatPlateImpl(JsonConverterManager.INSTANCE.getPlateJsonConverter(this).toFlatJsonConversion()));
    }

    /** Builds an entity of the given class from the given columns' values. */
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

    /** Copies all of this plate's non-null entity maps into {@code flatPlate} and returns it. */
    public <M extends FlatPlate> M toFlatPlate(M flatPlate) {
        if (flatPlate == null) throw new AttemptToWriteIntoNullEntityException();
        values().stream().filter(Objects::nonNull).forEach(flatPlate::putAll);
        return flatPlate;
    }

    /** Merges another plate into this one, keeping this plate's values on a clash. */
    public void joinPlate(Plate otherPlate) {
        for (Entry<String, Map<String, Object>> otherPlateEntry : otherPlate.entrySet()) {
            if (!this.containsKey(otherPlateEntry.getKey())) {
                put(otherPlateEntry.getKey(), otherPlateEntry.getValue());
                continue;
            }

            Map<String, Object> otherPlateColumns = otherPlateEntry.getValue();
            Map<String, Object> thisPlateColumns = get(otherPlateEntry.getKey());

            for (Entry<String, Object> otherPlateColumn : otherPlateColumns.entrySet()) {
                thisPlateColumns.putIfAbsent(otherPlateColumn.getKey(), otherPlateColumn.getValue());
            }
        }
    }

    /** Restricts this plate to the entries present in {@code mask}, dropping the rest. */
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

    /** This plate rendered as JSON ({@code []} when empty). */
    @Override
    public String toJson() {

        if (isEmpty()) {
            return "[]";
        }

        return JsonConverterManager.INSTANCE.getPlateJsonConverter(this)
                .toJson(new StringBuilder(), this).toString();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plate plate)) return false;
        if (size() != plate.size()) return false;
        if (!Objects.equals(keySet(), plate.keySet())) return false;
        return Objects.equals(entrySet(), plate.entrySet());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fields);
    }

}
