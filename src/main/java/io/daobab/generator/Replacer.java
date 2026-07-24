package io.daobab.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A minimal template engine: it collects {@code key -> value} substitutions ({@link #add}) and applies them all
 * to a template string ({@link #replaceAll(String)}). The keys are the
 * {@link io.daobab.generator.template.GenKeys} placeholders embedded in the code templates.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Replacer {
    private final List<ReplaceWrapper> repl = new ArrayList<>();

    /**
     * Adds a placeholder-to-value substitution, returning this replacer for chaining.
     */
    public Replacer add(String key, String value) {
        repl.add(new ReplaceWrapper(key, value));
        return this;
    }

    /** Removes all the registered substitutions. */
    public void clear() {
        repl.clear();
    }

    private String replaceAll(String sb, String find, String replace) {
        return sb.replaceAll(Pattern.quote(find), Matcher.quoteReplacement(replace));
    }

    /** Applies every registered substitution to the template and returns the result. */
    public String replaceAll(String temp) {
        for (ReplaceWrapper rw : repl) {
            try {
                temp = replaceAll(temp, rw.getKey(), rw.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return temp;
    }
}
