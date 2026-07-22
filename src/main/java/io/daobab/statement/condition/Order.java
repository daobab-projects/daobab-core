package io.daobab.statement.condition;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;
import io.daobab.query.marschal.Marshaller;
import io.daobab.statement.condition.base.OrderDirection;
import io.daobab.statement.condition.base.OrderField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code ORDER BY} clause: an ordered list of columns (or identifiers), each ascending or descending.
 * The ordering is stored positionally in a map, keyed by direction and pointer, up to {@link #getCounter()}.
 * Built fluently: {@code new Order().asc(colA).desc(colB)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Order {

    private Map<String, Object> orderMap = new HashMap<>();
    private int counter = 1;

    /**
     * An ordering ascending by the column.
     */
    public static Order ASC(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.asc(col);
    }

    /** An ordering descending by the column. */
    public static Order DESC(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.desc(col);
    }

    /** Appends an ascending ordering by the column. */
    @SuppressWarnings("java:S1845")
    public Order asc(Column<?, ?, ?> col) {
        getOrderMap().put(OrderDirection.ORDERASC + getCounter(), col);
        setCounter(getCounter() + 1);
        return this;
    }

    /** Appends a descending ordering by the column. */
    @SuppressWarnings("java:S1845")
    public Order desc(Column<?, ?, ?> col) {
        getOrderMap().put(OrderDirection.ORDERDESC + getCounter(), col);
        setCounter(getCounter() + 1);
        return this;
    }

    /** Appends an ascending ordering by the named column/identifier. */
    @SuppressWarnings("java:S1845")
    public Order asc(String identifier) {
        getOrderMap().put(OrderDirection.ORDERASC + getCounter(), identifier);
        setCounter(getCounter() + 1);
        return this;
    }

    /** Appends a descending ordering by the named column/identifier. */
    @SuppressWarnings("java:S1845")
    public Order desc(String identifier) {
        getOrderMap().put(OrderDirection.ORDERDESC + getCounter(), identifier);
        setCounter(getCounter() + 1);
        return this;
    }

    /** The next free pointer; the orderings occupy the pointers {@code 1 .. counter - 1}. */
    public int getCounter() {
        return counter;
    }

    private void setCounter(int counter) {
        this.counter = counter;
    }

    /** The direction ({@code ORDERASC}/{@code ORDERDESC}) at the given pointer, or {@code null}. */
    public String getOrderKindForPointer(int pointer) {
        Object asc = getOrderMap().get(OrderDirection.ORDERASC + pointer);
        Object desc = getOrderMap().get(OrderDirection.ORDERDESC + pointer);
        if (asc == null && desc != null) return OrderDirection.ORDERDESC;
        if (asc != null && desc == null) return OrderDirection.ORDERASC;
        return null;
    }

    /** The ordered column (or identifier) at the given pointer, or {@code null}. */
    public Object getObjectForPointer(int pointer) {
        Object asc = getOrderMap().get(OrderDirection.ORDERASC + pointer);
        Object desc = getOrderMap().get(OrderDirection.ORDERDESC + pointer);
        if (asc == null && desc != null) return desc;
        if (asc != null && desc == null) return asc;
        return null;
    }

    /**
     * The ordering as a list of {@link OrderField}s. Only the {@link Column}-based entries are included;
     * identifier-based ones are skipped, so an in-memory buffer ordering may drop string-based arguments.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    //Warning. If query is related to in-memory buffers, this order may skip string based arguments.
    public List<OrderField> toOrderFieldList() {
        List<OrderField> rv = new ArrayList<>();
        for (int i = 1; i < getCounter(); i++) {
            Object field = getObjectForPointer(i);
            if (!(field instanceof Column)) {
                continue;
            }
            String orderkind = getOrderKindForPointer(i);
            rv.add(new OrderField<>(((Column) field), orderkind));
        }
        return rv;
    }

    /** The raw ordering map. */
    public Map<String, Object> getOrderMap() {
        return orderMap;
    }

    /** Replaces the raw ordering map. */
    public void setOrderMap(Map<String, Object> orderMap) {
        this.orderMap = orderMap;
    }

    /** Serializes the ordering to a remote-friendly map (entities and columns marshalled). */
    public Map<String, Object> toMap() {
        Map<String, Object> rv = new HashMap<>();
        for (Map.Entry<String, Object> entry : orderMap.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Entity valEntity) {
                rv.put(entry.getKey(), Marshaller.marshalEntity(valEntity));
            } else if (val instanceof TableColumn tableColumn) {
                rv.put(entry.getKey(), Marshaller.marshallColumnToString(tableColumn));
            } else {
                rv.put(entry.getKey(), val);
            }
        }
        return rv;
    }


    /** Rebuilds the ordering from its remote representation (not implemented yet). */
    public void fromRemote() {

    }
}
