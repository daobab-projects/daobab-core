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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Order {

    private Map<String, Object> orderMap = new HashMap<>();
    private int counter = 1;

    public static Order ASC(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.asc(col);
    }

    public static Order DESC(Column<?, ?, ?> col) {
        Order o = new Order();
        return o.desc(col);
    }

    @SuppressWarnings("java:S1845")
    public Order asc(Column<?, ?, ?> col) {
        getOrderMap().put(OrderDirection.ORDERASC + getCounter(), col);
        setCounter(getCounter() + 1);
        return this;
    }

    @SuppressWarnings("java:S1845")
    public Order desc(Column<?, ?, ?> col) {
        getOrderMap().put(OrderDirection.ORDERDESC + getCounter(), col);
        setCounter(getCounter() + 1);
        return this;
    }

    @SuppressWarnings("java:S1845")
    public Order asc(String identifier) {
        getOrderMap().put(OrderDirection.ORDERASC + getCounter(), identifier);
        setCounter(getCounter() + 1);
        return this;
    }

    @SuppressWarnings("java:S1845")
    public Order desc(String identifier) {
        getOrderMap().put(OrderDirection.ORDERDESC + getCounter(), identifier);
        setCounter(getCounter() + 1);
        return this;
    }

    public int getCounter() {
        return counter;
    }

    private void setCounter(int counter) {
        this.counter = counter;
    }

    public String getOrderKindForPointer(int pointer) {
        Object asc = getOrderMap().get(OrderDirection.ORDERASC + pointer);
        Object desc = getOrderMap().get(OrderDirection.ORDERDESC + pointer);
        if (asc == null && desc != null) return OrderDirection.ORDERDESC;
        if (asc != null && desc == null) return OrderDirection.ORDERASC;
        return null;
    }

    public Object getObjectForPointer(int pointer) {
        Object asc = getOrderMap().get(OrderDirection.ORDERASC + pointer);
        Object desc = getOrderMap().get(OrderDirection.ORDERDESC + pointer);
        if (asc == null && desc != null) return desc;
        if (asc != null && desc == null) return asc;
        return null;
    }

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

    public Map<String, Object> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<String, Object> orderMap) {
        this.orderMap = orderMap;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> rv = new HashMap<>();
        for (Map.Entry<String, Object> entry : orderMap.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Entity) {
                rv.put(entry.getKey(), Marshaller.marshalEntity((Entity) val));
            } else if (val instanceof TableColumn) {
                rv.put(entry.getKey(), Marshaller.marshallColumnToString((TableColumn) val));
            } else {
                rv.put(entry.getKey(), val);
            }
        }
        return rv;
    }

    public void fromRemote() {

    }
}
