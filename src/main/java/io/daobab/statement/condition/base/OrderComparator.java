package io.daobab.statement.condition.base;

import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.parser.ParserNumber;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * OrderBy comparator for cached entities
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class OrderComparator<E extends Entity> implements Comparator<E> {

    private List<OrderField<E, ?, ?>> orderList;

    public OrderComparator(List<OrderField<E, ?, ?>> order) {
        setOrderList(order);
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    @Override
    public int compare(E o1, E o2) {

        if (o1 == null && o2 == null) return 0;
        if (o1 != null && o2 == null) return 1;
        if (o1 == null) return -1;

        for (OrderField<E, ?, ?> fo : getOrderList()) {

            Object v1 = ((OrderField<E, ?, EntityRelation>) fo).getField().getValue((EntityRelation) o1);
            Object v2 = ((OrderField<E, ?, EntityRelation>) fo).getField().getValue((EntityRelation) o2);

            int result;
            if (v1 instanceof Number) {
                Number nv1 = (Number) v1;
                Number nv2 = (Number) v2;
                result = ParserNumber.toBigDecimal(nv1).compareTo(ParserNumber.toBigDecimal(nv2));
            } else if (v1 instanceof Date) {
                Long nv1 = ((Date) v1).getTime();
                Long nv2 = ((Date) v2).getTime();
                result = ParserNumber.toBigDecimal(nv1).compareTo(ParserNumber.toBigDecimal(nv2));
            } else if (v1 instanceof String) {
                String nv1 = (String) v1;
                String nv2 = (String) v2;
                result = nv2.toUpperCase().compareTo(nv1.toUpperCase());
            } else {
                result = v2.toString().toUpperCase().compareTo(v1.toString().toUpperCase());
            }

            if (result != 0) {
                if (OrderDirection.ORDERASC.equals(fo.getDirection())) {
                    return result * -1;
                }
                return result;
            }
        }
        return 0;
    }

    public List<OrderField<E, ?, ?>> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderField<E, ?, ?>> orderList) {
        this.orderList = orderList;
    }

}
