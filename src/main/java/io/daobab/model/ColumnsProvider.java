package io.daobab.model;

import java.util.ArrayList;
import java.util.List;

public interface ColumnsProvider {

    List<TableColumn> columns();

    default List<TableColumn> relations() {
        return new ArrayList<>();
    }
}
