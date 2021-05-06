package io.daobab.query.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class QuerySpecialParameters {

    private int counter = 1;
    private StringBuilder query;
    private Map<Integer, Object> specialParameters = new HashMap<>();

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public StringBuilder getQuery() {
        return query;
    }

    public void setQuery(StringBuilder query) {
        this.query = query;
    }

    public Map<Integer, Object> getSpecialParameters() {
        return specialParameters;
    }

    public void setSpecialParameters(Map<Integer, Object> specialParameters) {
        this.specialParameters = specialParameters;
    }
}
