package io.daobab.statement.condition;

/**
 * A comparison operator of a where/having condition; its {@link #toString()} is the SQL fragment rendered
 * between the column and the value.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public enum Operator {


    /**
     * Equality ({@code =}).
     */
    EQ(" = "),
    /** Inequality ({@code <>}). */
    NOT_EQ(" <> "),
    /** Greater than ({@code >}). */
    GT(" > "),
    /** Greater than or equal ({@code >=}). */
    GTEQ(" >= "),
    /** Less than ({@code <}). */
    LT(" < "),
    /** Less than or equal ({@code <=}). */
    LTEQ(" <= "),
    /** Membership ({@code IN}): the value must be one of the following. */
    IN(" in "),
    /** Non-membership ({@code NOT IN}): the value must not be one of the following. */
    NOT_IN(" not in "),
    /** Not null ({@code IS NOT NULL}). */
    NOT_NULL(" is not NULL "),
    /** Null ({@code IS NULL}). */
    IS_NULL(" is NULL "),

    /** Pattern match ({@code LIKE}). */
    LIKE(" LIKE "),

    /** Negated pattern match ({@code NOT LIKE}). */
    NOT_LIKE(" NOT LIKE ");


    private final String text;

    Operator(final String text) {
        this.text = text;
    }

    /**
     * The SQL fragment for this operator (e.g. {@code " = "}).
     */
    @Override
    public String toString() {
        return text;
    }


    /**
     * Whether the operator compares against a collection of values ({@link #IN} or {@link #NOT_IN}).
     */
    public boolean isRelationCollectionBased() {
        return Operator.IN.equals(this)
                || Operator.NOT_IN.equals(this);
    }
}
