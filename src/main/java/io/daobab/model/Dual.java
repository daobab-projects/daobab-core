package io.daobab.model;

import java.util.Collections;
import java.util.List;

/**
 * The {@code DUAL} pseudo-table (a single {@link Dummy} column) - used as the {@code FROM} of column-less
 * queries, such as selecting a bare function value.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@TableInformation(name = "DUAL")
public class Dual extends Table<Dual> implements Dummy<Dual> {

    /**
     * The single dummy column.
     */
    @Override
    public List<TableColumn> columns() {
        return Collections.singletonList(new TableColumn(colDummy()).size(0));
    }

}
