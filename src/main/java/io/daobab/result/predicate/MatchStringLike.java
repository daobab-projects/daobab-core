package io.daobab.result.predicate;

import java.util.regex.Pattern;

public class MatchStringLike implements WherePredicate<String> {

    protected final String valueToCompare;
    protected Pattern pattern;

    public MatchStringLike(String valueToCompare) {
        this.valueToCompare = valueToCompare;
        if (valueToCompare != null) {
            String regex = quotemeta(valueToCompare);
            regex = regex.replace("_", ".").replace("%", ".*?");
            pattern = Pattern.compile(regex,
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        }
    }

    public boolean test(String valueFromEntityField) {
        if (valueToCompare == null || valueFromEntityField == null) return false;
        return pattern.matcher(valueFromEntityField).matches();
    }

    private String quotemeta(String s) {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null");
        }

        int len = s.length();
        if (len == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if ("[](){}.*+?$^|#\\".indexOf(c) != -1) {
                sb.append("\\");
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
