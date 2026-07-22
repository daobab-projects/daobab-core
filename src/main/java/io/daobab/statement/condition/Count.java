package io.daobab.statement.condition;

import io.daobab.model.Column;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@code COUNT(...)} aggregate: {@link #any()} for {@code COUNT(*)}, {@link #field(Column)} for
 * {@code COUNT(column)} and {@link #fieldDistinct(Column)} for {@code COUNT(DISTINCT column)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Count {

    protected static final String KEY = "key";
    protected static final String DISTINCT = "distinct";
    private final Map<String, Object> map = new HashMap<>();
    private int counter = 1;

    /**
     * A {@code COUNT(*)} over the rows.
     */
    public static Count any() {
        return new Count();
    }

    /** A {@code COUNT(column)}. */
    public static Count field(Column<?, ?, ?> col) {
        Count c = new Count();
        c.map.put(KEY + c.getCounter(), col);
        c.setCounter(c.getCounter() + 1);
        return c;
    }

    /** A {@code COUNT(column)}, the column addressed by its identifier/alias. */
    public static Count field(String col) {
        Count c = new Count();
        c.map.put(KEY + c.getCounter(), col);
        c.setCounter(c.getCounter() + 1);
        return c;
    }

    /** A {@code COUNT(DISTINCT column)}. */
    public static Count fieldDistinct(Column<?, ?, ?> col) {
        Count c = new Count();
        c.map.put(KEY + c.getCounter(), col);
        c.map.put(DISTINCT + c.getCounter(), col);
        c.setCounter(c.getCounter() + 1);
        return c;
    }

    /** The next free pointer. */
    public int getCounter() {
        return counter;
    }

    /** Sets the next free pointer. */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /** Whether this is a bare {@code COUNT(*)} (no column given). */
    public boolean countEntities() {
        return getCounter() == 1 && getObjectForPointer(1) == null;
    }

    /** The counted column (or its identifier) at the given pointer. */
    public Object getObjectForPointer(int pointer) {
        return map.get(KEY + pointer);
    }

    /** Whether the count at the given pointer is {@code DISTINCT}. */
    public boolean isDistinctForPointer(int pointer) {
        return map.get(DISTINCT + pointer) != null;
    }

    /** The raw count map. */
    public Map<String, Object> getCountMap() {
        return map;
    }

}
