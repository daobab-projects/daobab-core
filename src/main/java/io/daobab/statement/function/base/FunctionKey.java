package io.daobab.statement.function.base;

/**
 * A raw SQL token (a keyword or identifier) passed as a function argument and rendered verbatim, without being
 * quoted or bound as a parameter - e.g. the {@code YEAR} in {@code EXTRACT(YEAR FROM column)}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class FunctionKey {

    private final String key;

    /**
     * @param key the raw SQL token
     */
    public FunctionKey(String key) {
        this.key = key;
    }

    /**
     * The raw SQL token.
     */
    public String getKey() {
        return key;
    }

}
