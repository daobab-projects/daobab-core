package io.daobab.result.predicate;

import java.util.regex.Pattern;

public class MatchLike implements WherePredicate<Object> {

    private final Object valueToCompare;
    private Pattern pattern;

    public MatchLike(Object valueToCompare) {
        this.valueToCompare = valueToCompare;
        if (valueToCompare != null) {
            String regex = quotemeta(valueToCompare.toString());
            regex = regex.replace("_", ".").replace("%", ".*?");
            pattern = Pattern.compile(regex,
                    Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        }
    }

    public boolean test(Object valueFromEntityField) {
        if (valueToCompare == null || valueFromEntityField == null) return false;
        return pattern.matcher(valueFromEntityField.toString()).matches();
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
