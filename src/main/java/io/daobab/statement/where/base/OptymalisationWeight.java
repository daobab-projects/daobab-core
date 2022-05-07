package io.daobab.statement.where.base;

import io.daobab.model.Column;
import io.daobab.model.PrimaryKey;
import io.daobab.statement.condition.Operator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface OptymalisationWeight {

    long PK = 1;
    long FK = 200;
    long INDEXED_NUMBER = 220;
    long NUMBER = 230;
    long STRING = 250;
    long DEFAULT = 260;

    long EQUAL = 1;
    long GT = 10;
    long LT = 20;
    long IN = 30;
    long LIKE = 60;
    long NOT_EQUAL = 50;
    long DEFAULT_OPERATOR = 40;


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
