package io.daobab.generator;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Replacer {
    private List<ReplaceWrapper> repl = new LinkedList<>();

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
                System.out.printf("Error for " + rw.getKey() + " and " + rw.getValue());
                e.printStackTrace();
            }
        }
        return temp;
    }
}
