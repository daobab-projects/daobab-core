package io.daobab.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Replacer {
    private final List<ReplaceWrapper> repl = new ArrayList<>();

    public Replacer add(String key, String value) {
        repl.add(new ReplaceWrapper(key, value));
        return this;
    }

    public void clear() {
        repl.clear();
    }

    private String replaceAll(String sb, String find, String replace) {
        return sb.replaceAll(Pattern.quote(find), Matcher.quoteReplacement(replace));
    }

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
