package io.daobab.statement.join;

/**
 * The kind of a SQL join; its {@link #toString()} is the SQL keyword rendered before the joined table.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public enum JoinType {

    /**
     * {@code INNER JOIN}.
     */
    INNER(" inner join"),
    /** {@code OUTER JOIN}. */
    OUTER(" outer join"),
    /** {@code LEFT JOIN}. */
    LEFT_JOIN(" left join"),
    /** {@code RIGHT JOIN}. */
    RIGHT_JOIN(" right join");


    private final String text;

    JoinType(final String text) {
        this.text = text;
    }

    /** The SQL keyword for this join type (e.g. {@code " inner join"}). */
    @Override
    public String toString() {
        return text;
    }


}
