package io.daobab.statement.condition.base;

import io.daobab.model.EntityRelation;
import io.daobab.model.Plate;
import io.daobab.parser.ParserNumber;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

/**
 * OrderBy comparator for cached entities
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class OrderComparatorPlate implements Comparator<Plate> {

    private LinkedList<OrderField> orderList;

    public OrderComparatorPlate(LinkedList<OrderField> order) {
        setOrderList(order);
    }

    @Override
    public int compare(Plate o1, Plate o2) {

        if (o1 == null && o2 == null) return 0;
        if (o1 != null && o2 == null) return 1;
        if (o1 == null && o2 != null) return -1;

        for (OrderField<?, ?, ?> fo : getOrderList()) {

            Object v1 = ((OrderField<?, ?, EntityRelation>) fo).getField().getValue((EntityRelation) o1);
            Object v2 = ((OrderField<?, ?, EntityRelation>) fo).getField().getValue((EntityRelation) o2);

            int result = 0;
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

    public LinkedList<OrderField> getOrderList() {
        return orderList;
    }

    public void setOrderList(LinkedList<OrderField> orderList) {
        this.orderList = orderList;
    }

}
