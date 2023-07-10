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


    public static Plate fromTableColumnList(Collection<TableColumn> list) {
        Plate plate = new Plate();
        plate.fields = list.stream().map(TableColumn::getColumn).map(Field.class::cast).collect(Collectors.toList());
        return plate;
    }

    public static Plate fromTableColumnListAndRelatedArray(List<TableColumn> list, Object[] rowResults) {
        Plate plate = fromTableColumnList(list);
        for (int i = 0; i < list.size(); i++) {
            TableColumn c = list.get(i);
            plate.setValue(c, rowResults[i]);
        }
        return plate;
    }

    public static Plate fromColumnList(Collection<Column> list) {
        Plate plate = new Plate();
        plate.fields = list.stream().map(Field.class::cast).collect(Collectors.toList());
        return plate;
    }

    public static Plate fromFieldList(Collection<Field> list) {
        Plate plate = new Plate();
        plate.fields = new ArrayList<>(list);
        return plate;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Plate fromFieldMap(Map<Field, Object> map) {
        Plate plate = fromFieldList(map.keySet());
        for (Map.Entry<Field, Object> entry : map.entrySet()) {
            plate.setValue(entry.getKey(), entry.getValue());
        }
        return plate;
    }

    public static Plate fromColumnMap(Map<Column, Object> map) {
        Plate plate = fromColumnList(map.keySet());
        for (Map.Entry<Column, Object> entry : map.entrySet()) {
            plate.setValue(entry.getKey(), entry.getValue());
        }
        return plate;
    }

}
