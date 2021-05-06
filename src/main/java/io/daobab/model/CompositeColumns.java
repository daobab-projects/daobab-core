package io.daobab.model;


import java.util.ArrayList;
import java.util.Arrays;

public class CompositeColumns<K extends Composite> extends ArrayList<TableColumn> {

    public CompositeColumns(TableColumn... columns) {
        if (columns == null) return;
        addAll(Arrays.asList(columns));
    }

}
