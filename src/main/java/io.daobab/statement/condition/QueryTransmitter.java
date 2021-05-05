package io.daobab.statement.condition;

import io.daobab.model.Column;

import java.util.HashMap;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class QueryTransmitter {

    private final HashMap<String, Object> whereMap = new HashMap<>();
    private final HashMap<String, Object> havingMap = new HashMap<>();
    private final HashMap<String, Column<?, ?, ?>> orderMap = new HashMap<>();


}
