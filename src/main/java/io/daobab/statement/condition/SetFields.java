package io.daobab.statement.condition;

import io.daobab.error.AttemptToSetNullValueInWrongWay;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.model.TableColumn;

import java.util.Date;
import java.util.HashMap;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class SetFields {

    private static final String FIELD = "field";
    private static final String VALUE = "value";
    private final HashMap<String, Object> hash = new HashMap<>();
    private int counter = 1;

    public static <F, R extends RelatedTo> SetFields setColumn(Column<?, F, R> field, R related) {
        SetFields setFields = new SetFields();
        setFields.setValue(field, related);
        return setFields;
    }

    public static <F, R extends RelatedTo> SetFields setInfoColumns(R related, TableColumn[] fields) {
        SetFields setFields = new SetFields();
        for (TableColumn field : fields) {
            setFields.setValue((Column<?, F, R>) field.getColumn(), related);
        }
        return setFields;
    }

    public static <F, R extends RelatedTo> SetFields setValues(R related, Column<?, F, R>... fields) {
        SetFields setFields = new SetFields();
        for (Column<?, F, R> field : fields) {
            setFields.setValue(field, related);
        }
        return setFields;
    }

    public static <F, R extends RelatedTo> SetFields setColumn(SetFields setFields, Column<?, F, R> field, F value) {
        setFields.setValue(field, value);
        return setFields;
    }

    public static <E extends Entity, R extends RelatedTo> SetFields setValuesArray(E related, Column<E, ?, ?>... fields) {
        SetFields setFields = new SetFields();
        for (Column<E, ?, ?> c : fields) {
            Column<E, ?, R> cc = (Column<E, ?, R>) c;
            setFields.setValue(cc, (R) related);
        }
        return setFields;
    }

    public int size() {
        return counter - 1;
    }

    public SetFields setCurrentDate(Column<?, Date, ?> field) {
        hash.put(FIELD + getCounter(), field);
        hash.put(VALUE + getCounter(), new Date());
        setCounter(getCounter() + 1);
        return this;
    }

    public <E extends Entity> SetFields setNull(Column<E, ?, ?> relation) {
        hash.put(FIELD + getCounter(), relation);
        setCounter(getCounter() + 1);
        return this;
    }

    public <F, R extends RelatedTo> SetFields setValue(Column<?, F, R> relation, R related) {
        if (related == null) throw new AttemptToSetNullValueInWrongWay(relation);
        hash.put(FIELD + getCounter(), relation);
        Object val = relation.getValueOf(related);
        if (val != null) hash.put(VALUE + getCounter(), val);

        setCounter(getCounter() + 1);
        return this;
    }

    public <F, R extends RelatedTo> SetFields setValue(Column<?, F, R> relation, F val) {
        hash.put(FIELD + getCounter(), relation);
        if (val != null) hash.put(VALUE + getCounter(), val);
        setCounter(getCounter() + 1);
        return this;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Column<?, ?, ?> getFieldForPointer(int pointer) {
        return (Column<?, ?, ?>) hash.get(FIELD + pointer);
    }

    public Object getValueForPointer(int pointer) {
        return hash.get(VALUE + pointer);
    }

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
