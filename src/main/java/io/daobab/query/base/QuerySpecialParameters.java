package io.daobab.query.base;

import java.util.HashMap;
import java.util.Map;

/**
 * The SQL string being built together with the positional PreparedStatement parameters collected for it
 * (a map of 1-based index to value) and the running parameter counter.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public class QuerySpecialParameters {

    private int counter = 1;
    private StringBuilder query;
    private Map<Integer, Object> specialParameters = new HashMap<>();

    /**
     * The next free parameter index.
     */
    public int getCounter() {
        return counter;
    }

    /** Sets the next free parameter index. */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /** The SQL being built. */
    public StringBuilder getQuery() {
        return query;
    }

    /** Sets the SQL being built. */
    public void setQuery(StringBuilder query) {
        this.query = query;
    }

    /** The collected parameters, keyed by their 1-based position. */
    public Map<Integer, Object> getSpecialParameters() {
        return specialParameters;
    }

    /** Replaces the collected parameters. */
    public void setSpecialParameters(Map<Integer, Object> specialParameters) {
        this.specialParameters = specialParameters;
    }
}
