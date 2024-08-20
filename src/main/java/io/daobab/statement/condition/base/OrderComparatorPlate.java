package io.daobab.statement.condition.base;

import io.daobab.model.Plate;
import io.daobab.model.RelatedTo;
import io.daobab.parser.ParserNumber;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * OrderBy comparator for cached entities
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderComparatorPlate implements Comparator<Plate> {

    private List<OrderField> orderList;

    public OrderComparatorPlate(List<OrderField> order) {
        setOrderList(order);
    }


    @Override
    public int compare(Plate o1, Plate o2) {

        if (o1 == null && o2 == null) return 0;
        if (o1 != null && o2 == null) return 1;
        if (o1 == null) return -1;

        for (OrderField<?, ?, ?> fo : getOrderList()) {

            Object v1 = o1.getValue(((OrderField<?, ?, RelatedTo>) fo).getField());
            Object v2 = o2.getValue(((OrderField<?, ?, RelatedTo>) fo).getField());

            final int result;
            if (v1 instanceof Number nv1) {
                Number nv2 = (Number) v2;
                result = ParserNumber.toBigDecimal(nv1).compareTo(ParserNumber.toBigDecimal(nv2));
            } else if (v1 instanceof Date v1date) {
                Long nv1 = v1date.getTime();
                Long nv2 = ((Date) v2).getTime();
                result = ParserNumber.toBigDecimal(nv1).compareTo(ParserNumber.toBigDecimal(nv2));
            } else if (v1 instanceof String nv1) {
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

    public List<OrderField> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderField> orderList) {
        this.orderList = orderList;
    }

}
