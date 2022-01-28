package io.daobab.statement.condition;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public enum Operator {


    /**
     * EQ
     * Variables must be the same
     */
    EQ(" = "),
    /**
     * NOT EQ
     * Variables must be differend
     */
    NOT_EQ(" <> "),
    /**
     * GREATER THAN
     */
    GT(" > "),
    /**
     * GREATER OR EQ THAN
     */
    GTEQ(" >= "),
    /**
     * LESS THAN
     */
    LT(" < "),
    /**
     * LESS OR EQ THAN
     */
    LTEQ(" <= "),
    /**
     * IN
     * Valiable must be one of following
     */
    IN(" in "),
    /**
     * NOT IN
     * Variable can't be on of the following
     */
    NOT_IN(" not in "),
    /**
     * NOT NULL
     */
    NOT_NULL(" is not NULL "),
    /**
     * NULL
     */
    IS_NULL(" is NULL "),

    /**
     * LIKE
     */
    LIKE(" LIKE "),

    /**
     * NOT LIKE
     */
    NOT_LIKE(" NOT LIKE ");


    private final String text;

    Operator(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }


    public boolean isRelationCollectionBased() {
        return Operator.IN.equals(this)
                || Operator.NOT_IN.equals(this)
                ;
    }
}
