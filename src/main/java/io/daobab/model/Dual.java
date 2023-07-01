package io.daobab.model;

import java.util.Collections;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@TableName(value = "DUAL")
public class Dual extends Table<Dual> implements Dummy<Dual> {

    @Override
    public List<TableColumn> columns() {
        return Collections.singletonList(new TableColumn(colDummy()).size(0));
    }

}
