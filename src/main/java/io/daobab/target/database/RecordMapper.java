package io.daobab.target.database;

import io.daobab.error.DaobabException;
import io.daobab.model.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.List;

/**
 * Maps a query result onto a Java record, assigning the query's selected columns to the record components
 * positionally - the first selected column to the first record component, the second to the second, and so on.
 * Shared by {@link DataBaseTargetLogic} and the remote client, so every target builds records the same way.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public final class RecordMapper {

    private RecordMapper() {
    }

    /**
     * Fails when the record's component count does not match the number of columns the query selects.
     *
     * @param recordClass     the record type
     * @param selectedColumns the number of columns the query selects
     * @throws DaobabException when the counts differ
     */
    public static void validateComponentCount(Class<? extends Record> recordClass, int selectedColumns) {
        int componentCount = recordClass.getRecordComponents().length;
        if (componentCount != selectedColumns) {
            throw new DaobabException("Record " + recordClass.getName() + " has " + componentCount
                    + " component(s), but the query selects " + selectedColumns
                    + " column(s). The record components must match the selected columns one to one.");
        }
    }

    /**
     * Builds a record from a plate row, reading each selected column value in component order.
     */
    public static <R extends Record> R fromPlate(Plate plate, List<TableColumn> fields, Class<R> recordClass) {
        RecordComponent[] components = recordClass.getRecordComponents();
        Object[] values = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            values[i] = plate.getValue(fields.get(i).getColumn());
        }
        return instantiate(recordClass, components, values);
    }

    /**
     * Builds a record from an entity, reading each selected column value in component order.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <R extends Record> R fromEntity(Entity entity, List<TableColumn> fields, Class<R> recordClass) {
        RecordComponent[] components = recordClass.getRecordComponents();
        Object[] values = new Object[components.length];
        for (int i = 0; i < components.length; i++) {
            Column column = fields.get(i).getColumn();
            values[i] = column.getValueOf((RelatedTo) entity);
        }
        return instantiate(recordClass, components, values);
    }

    /**
     * Invokes the record's canonical constructor with the column values gathered in component order.
     */
    private static <R extends Record> R instantiate(Class<R> recordClass, RecordComponent[] components, Object[] values) {
        Class<?>[] parameterTypes = new Class<?>[components.length];
        for (int i = 0; i < components.length; i++) {
            parameterTypes[i] = components[i].getType();
        }
        try {
            Constructor<R> constructor = recordClass.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            throw new DaobabException("Cannot instantiate record " + recordClass.getName()
                    + " from the query result: " + e.getMessage(), e);
        }
    }
}
