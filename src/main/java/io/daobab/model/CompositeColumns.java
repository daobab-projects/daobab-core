package io.daobab.model;


import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The ordered columns forming a composite key. Being a {@code List<TableColumn>}, it holds the key's columns and
 * can render the {@code WHERE} matching a given composite value ({@link #getWhere(Composite)}).
 *
 * @param <K> the composite key type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class CompositeColumns<K extends Composite> extends ArrayList<TableColumn> {

    /**
     * @param columns the columns forming the composite key
     */
    public CompositeColumns(TableColumn... columns) {
        if (columns == null) return;
        addAll(Arrays.asList(columns));
    }

    /**
     * The {@code AND} where clause matching every key column against {@code val}'s values.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Where getWhere(Composite val) {
        WhereAnd whereAnd = new WhereAnd();
        for (TableColumn tableColumn : this) {
            Column column = tableColumn.getColumn();
            whereAnd.equal(column, column.getValueOf((RelatedTo) val));
        }
        return whereAnd;
    }

}
