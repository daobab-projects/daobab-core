package io.daobab.creation;

import io.daobab.model.Column;
import io.daobab.model.Field;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlateCreator {


    public static Plate ofTableColumnList(Collection<TableColumn> list) {
        Plate plate = new Plate();
        plate.fields = list.stream().map(TableColumn::getColumn).map(Field.class::cast).collect(Collectors.toList());
        return plate;
    }

    public static Plate ofTableColumnListAndRelatedArray(List<TableColumn> list, Object[] rowResults) {
        Plate plate = ofTableColumnList(list);
        for (int i = 0; i < list.size(); i++) {
            TableColumn c = list.get(i);
            plate.setValue(c, rowResults[i]);
        }
        return plate;
    }

    public static Plate ofColumnList(Collection<Column> list) {
        Plate plate = new Plate();
        plate.fields = list.stream().map(Field.class::cast).collect(Collectors.toList());
        return plate;
    }

    public static Plate ofFieldList(Collection<Field> list) {
        Plate plate = new Plate();
        plate.fields = new ArrayList<>(list);
        return plate;
    }

    public static Plate ofFieldMap(Map<Field, Object> map) {
        Plate plate = ofFieldList(map.keySet());
        for (Map.Entry<Field, Object> entry : map.entrySet()) {
            plate.setValue(entry.getKey(), entry.getValue());
        }
        return plate;
    }

    public static Plate ofColumnMap(Map<Column, Object> map) {
        Plate plate = ofColumnList(map.keySet());
        for (Map.Entry<Column, Object> entry : map.entrySet()) {
            plate.setValue(entry.getKey(), entry.getValue());
        }
        return plate;
    }

}
