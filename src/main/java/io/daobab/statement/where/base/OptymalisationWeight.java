package io.daobab.statement.where.base;

import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.statement.condition.Operator;

/**
 * Selectivity weights used to order the conditions of a where clause so the most selective ones are evaluated
 * first (see {@link WhereBase#optimize()}). A condition's weight is the product of its column weight and its
 * operator weight - the <b>lower</b> the product, the earlier the condition is applied. A primary-key equality
 * is therefore the cheapest possible condition.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface OptymalisationWeight {

    /**
     * Column weight: a primary key (the most selective).
     */
    long PK = 1;
    /** Column weight: a foreign key. */
    long FK = 200;
    /** Column weight: an indexed numeric column. */
    long INDEXED_NUMBER = 220;
    /** Column weight: a numeric column. */
    long NUMBER = 230;
    /** Column weight: a string column. */
    long STRING = 250;
    /** Column weight: any other column (the least selective). */
    long DEFAULT = 260;

    /** Operator weight: equality (the most selective). */
    long EQUAL = 1;
    /** Operator weight: greater-than (and greater-or-equal). */
    long GT = 10;
    /** Operator weight: less-than (and less-or-equal). */
    long LT = 20;
    /** Operator weight: {@code IN}. */
    long IN = 30;
    /** Operator weight: {@code LIKE} (the least selective). */
    long LIKE = 60;
    /** Operator weight: inequality. */
    long NOT_EQUAL = 50;
    /** Operator weight: any other operator. */
    long DEFAULT_OPERATOR = 40;


    /**
     * The selectivity weight of a column: {@link #PK} for a primary key, {@link #NUMBER}/{@link #STRING} by the
     * field type, {@link #DEFAULT} otherwise (also for a {@code null} column).
     *
     * @param column the condition column
     * @return the column weight
     */
    static long getColumnWeight(Column<?, ?, ?> column) {
        if (column == null) return DEFAULT;

        if (column.getInstance() instanceof PrimaryKey && ((PrimaryKey) column.getInstance()).colID().equals(column)) {
            return PK;
        }

        Class<?> c = column.getFieldClass();

        if (Number.class.isAssignableFrom(c)) {
            return NUMBER;
        }

        if (String.class.equals(c)) {
            return STRING;
        }

        return DEFAULT;
    }


    /**
     * The selectivity weight of an operator: equality is the cheapest, {@code LIKE} the most expensive;
     * a {@code null} operator falls back to {@link #DEFAULT_OPERATOR}.
     *
     * @param operator the condition operator
     * @return the operator weight
     */
    static long getOperatorWeight(Operator operator) {
        if (operator == null) return DEFAULT_OPERATOR;
        switch (operator) {
            case EQ:
                return EQUAL;
            case GT:
                return GT;
            case GTEQ:
                return GT;
            case LT:
                return LT;
            case LTEQ:
                return LT;
            case LIKE:
                return LIKE;
            case IN:
                return IN;
            default:
                return DEFAULT_OPERATOR;
        }
    }
}
