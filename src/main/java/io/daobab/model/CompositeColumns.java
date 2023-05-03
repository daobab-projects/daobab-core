package io.daobab.model;


import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class CompositeColumns<K extends Composite> extends ArrayList<TableColumn> {

    public CompositeColumns(TableColumn... columns) {
        if (columns == null) return;
        addAll(Arrays.asList(columns));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Where getWhere(Composite val) {
        WhereAnd whereAnd = new WhereAnd();
        for (TableColumn tableColumn : this) {
            Column column = tableColumn.getColumn();
            whereAnd.equal(column, column.getValueOf((EntityRelation) val));
        }
        return whereAnd;
    }

}
