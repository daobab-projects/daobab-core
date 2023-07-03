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
        SetFields s = new SetFields();
        s.setValue(field, related);
        return s;
    }

    public static <F, R extends RelatedTo> SetFields setInfoColumns(R related, TableColumn[] fields) {
        SetFields s = new SetFields();
        for (TableColumn field : fields) {
            s.setValue((Column<?, F, R>) field.getColumn(), related);
        }
        return s;
    }

    public static <F, R extends RelatedTo> SetFields setColumns(R related, Column<?, F, R>... fields) {
        SetFields s = new SetFields();
        for (Column<?, F, R> field : fields) {
            s.setValue(field, related);
        }
        return s;
    }

    public static <F, R extends RelatedTo> SetFields setColumn(Column<?, F, R> field, F value) {
        SetFields s = new SetFields();
        s.setValue(field, value);
        return s;
    }

    public static <E extends Entity, R extends RelatedTo> SetFields setColumns(E related, Column<E, ?, ?>... fields) {
        SetFields s = new SetFields();
        for (Column<E, ?, ?> c : fields) {
            Column<E, ?, R> cc = (Column<E, ?, R>) c;
            s.setValue(cc, (R) related);
        }
        return s;
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
