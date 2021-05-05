package io.daobab.statement.condition;

import io.daobab.model.Column;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class Count {

    protected static final String KEY = "key";
    protected static final String DISTINCT = "distinct";
    Map<String, Object> map = new HashMap<>();
    private int counter = 1;


    public static final Count any() {
       return new Count();
    }

    public static final Count field(Column<?, ?, ?> col) {
        Count c = new Count();
        c.map.put(KEY + c.getCounter(), col);
        c.setCounter(c.getCounter() + 1);
        return c;
    }

    public static final Count fieldDistinct(Column<?, ?, ?> col) {
        Count c = new Count();
        c.map.put(KEY + c.getCounter(), col);
        c.map.put(DISTINCT + c.getCounter(), col);
        c.setCounter(c.getCounter() + 1);
        return c;
    }


    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean countEntities() {
        return getCounter() == 1 && getFieldForPointer(1) == null;
    }

    public Column getFieldForPointer(int pointer) {
        return (Column) map.get(KEY + pointer);
    }

    public boolean isDistinctForPointer(int pointer) {
        return map.get(DISTINCT + pointer) != null;
    }


    public Map<String, Object> getCountMap() {
        return map;
    }

}
