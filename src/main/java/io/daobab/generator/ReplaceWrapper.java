package io.daobab.generator;

/**
 * One template substitution used by the {@link Replacer}: a placeholder {@link #getKey() key} and its
 * {@link #getValue() replacement value}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ReplaceWrapper {

    private String key;
    private String value;

    /**
     * @param key   the placeholder
     * @param value its replacement
     */
    public ReplaceWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * The placeholder.
     */
    public String getKey() {
        return key;
    }

    /** Sets the placeholder. */
    public void setKey(String key) {
        this.key = key;
    }

    /** The replacement value. */
    public String getValue() {
        return value;
    }

    /** Sets the replacement value. */
    public void setValue(String value) {
        this.value = value;
    }
}
