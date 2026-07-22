package io.daobab.statement.condition;

import io.daobab.error.AttemptToSetNullValueInWrongWay;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.model.TableColumn;

import java.util.Date;
import java.util.HashMap;


/**
 * A set of {@code column = value} assignments for an {@code UPDATE} or an {@code INSERT}, stored positionally
 * (field/value pairs) up to {@link #getCounter()}. Assignments are added fluently through the {@code setValue}
 * / {@code setNull} / {@code setCurrentDate} methods or built with the static factories.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class SetFields {

    private static final String FIELD = "field";
    private static final String VALUE = "value";
    private final HashMap<String, Object> hash = new HashMap<>();
    private int counter = 1;

    /**
     * A set with a single assignment: the column set to its value in the related entity.
     */
    public static <F, R extends RelatedTo> SetFields setColumn(Column<?, F, R> field, R related) {
        SetFields setFields = new SetFields();
        setFields.setValue(field, related);
        return setFields;
    }

    /** A set assigning each of the given columns its value in the related entity. */
    public static <F, R extends RelatedTo> SetFields setInfoColumns(R related, TableColumn[] fields) {
        SetFields setFields = new SetFields();
        for (TableColumn field : fields) {
            setFields.setValue((Column<?, F, R>) field.getColumn(), related);
        }
        return setFields;
    }

    /** A set assigning each of the given columns its value in the related entity. */
    public static <F, R extends RelatedTo> SetFields setValues(R related, Column<?, F, R>... fields) {
        SetFields setFields = new SetFields();
        for (Column<?, F, R> field : fields) {
            setFields.setValue(field, related);
        }
        return setFields;
    }

    /** Adds a {@code field = value} assignment to the given set and returns it. */
    public static <F, R extends RelatedTo> SetFields setColumn(SetFields setFields, Column<?, F, R> field, F value) {
        setFields.setValue(field, value);
        return setFields;
    }

    /** A set assigning each of the given columns its value in the related entity. */
    public static <E extends Entity, R extends RelatedTo> SetFields setValuesArray(E related, Column<E, ?, ?>... fields) {
        SetFields setFields = new SetFields();
        for (Column<E, ?, ?> c : fields) {
            Column<E, ?, R> cc = (Column<E, ?, R>) c;
            setFields.setValue(cc, (R) related);
        }
        return setFields;
    }

    /** The number of assignments. */
    public int size() {
        return counter - 1;
    }

    /** Adds a {@code field = now} assignment (the current date/time). */
    public SetFields setCurrentDate(Column<?, Date, ?> field) {
        hash.put(FIELD + getCounter(), field);
        hash.put(VALUE + getCounter(), new Date());
        setCounter(getCounter() + 1);
        return this;
    }

    /** Adds a {@code field = NULL} assignment. */
    public <E extends Entity> SetFields setNull(Column<E, ?, ?> relation) {
        hash.put(FIELD + getCounter(), relation);
        setCounter(getCounter() + 1);
        return this;
    }

    /**
     * Adds a {@code field = value} assignment, the value read from the related entity.
     *
     * @throws AttemptToSetNullValueInWrongWay when {@code related} is {@code null} (use {@link #setNull})
     */
    public <F, R extends RelatedTo> SetFields setValue(Column<?, F, R> relation, R related) {
        if (related == null) throw new AttemptToSetNullValueInWrongWay(relation);
        hash.put(FIELD + getCounter(), relation);
        Object val = relation.getValueOf(related);
        if (val != null) hash.put(VALUE + getCounter(), val);

        setCounter(getCounter() + 1);
        return this;
    }

    /** Adds a {@code field = value} assignment with a plain value ({@code null} stores no value). */
    public <F, R extends RelatedTo> SetFields setValue(Column<?, F, R> relation, F val) {
        hash.put(FIELD + getCounter(), relation);
        if (val != null) hash.put(VALUE + getCounter(), val);
        setCounter(getCounter() + 1);
        return this;
    }

    /** The next free pointer; the assignments occupy the pointers {@code 1 .. counter - 1}. */
    public int getCounter() {
        return counter;
    }

    /** Sets the next free pointer. */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /** The assigned column at the given pointer. */
    public Column<?, ?, ?> getFieldForPointer(int pointer) {
        return (Column<?, ?, ?>) hash.get(FIELD + pointer);
    }

    /** The assigned value at the given pointer (absent for a {@code NULL} assignment). */
    public Object getValueForPointer(int pointer) {
        return hash.get(VALUE + pointer);
    }

    /** The value assigned to the given column (matched by entity and field name), or {@code null}. */
    public Object getValueForColumn(Column<?, ?, ?> col) {
        for (int i = 0; i < counter; i++) {
            Column<?, ?, ?> column = (Column<?, ?, ?>) hash.get(FIELD + i);
            if (col.entityClass().equals(column.entityClass()) && col.getFieldName().equals(column.getFieldName())) {
                return hash.get(VALUE + i);
            }
        }
        return null;
    }


}
