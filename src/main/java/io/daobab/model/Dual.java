package io.daobab.model;

import java.util.Collections;
import java.util.List;

public class Dual extends Table implements Dummy<Dual> {

    @Override
    public String getEntityName() {
        return "DUAL";
    }

    @Override
    public List<TableColumn> columns() {
        return Collections.singletonList(new TableColumn(colDummy()).size(0));
    }

}
