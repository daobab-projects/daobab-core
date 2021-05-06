package io.daobab.statement.join;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public enum JoinType {

    INNER(" inner join"),
    OUTER(" outer join"),
    LEFT_JOIN(" left join"),
    RIGHT_JOIN(" right join");


    private final String text;

    JoinType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }


}
