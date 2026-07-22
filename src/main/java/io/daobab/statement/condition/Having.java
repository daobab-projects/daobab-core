package io.daobab.statement.condition;

import io.daobab.model.ColumnHaving;
import io.daobab.statement.where.base.Where;

import static io.daobab.statement.condition.Operator.*;

/**
 * The {@code HAVING} clause: a {@link Where} group (joined by {@code AND}) of conditions over the grouped
 * result, typically aggregate functions. On top of the inherited column/value operators it adds overloads that
 * address a column by its identifier/alias (the {@code String} variants).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class Having extends Where<Having> {

    /**
     * Creates an empty having clause.
     */
    public Having() {
        super();
    }

    /** Creates a having clause seeded with the given sub-clause. */
    public Having(Where<?> where) {
        super();
        having(where);
    }


    /** Adds a nested sub-clause to this having clause. */
    public Having having(Where<?> val) {
        temp(val);
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return the {@code AND} operator
     */
    @Override
    public String getRelationBetweenExpressions() {
        return AND;
    }


    /** Adds a {@code column = value} condition, the column addressed by its identifier/alias. */
    @SuppressWarnings("java:S1221")
    public final Having equal(String column, Object val) {
        tempHaving(new ColumnHaving<>(column), EQ, val);
        return this;
    }

    /** Adds a {@code column > value} condition, the column addressed by its identifier/alias. */
    public final Having greater(String column, Object val) {
        tempHaving(new ColumnHaving<>(column), GT, val);
        return this;
    }

    /** Adds a {@code column >= value} condition, the column addressed by its identifier/alias. */
    public final Having greaterOrEqual(String column, Object val) {
        tempHaving(new ColumnHaving<>(column), GTEQ, val);
        return this;
    }

    /** Adds a {@code column < value} condition, the column addressed by its identifier/alias. */
    public final Having less(String column, Object val) {
        tempHaving(new ColumnHaving<>(column), LT, val);
        return this;
    }

    /** Adds a {@code column <= value} condition, the column addressed by its identifier/alias. */
    public final Having lessOrEqual(String column, Object val) {
        tempHaving(new ColumnHaving<>(column), LTEQ, val);
        return this;
    }

    /** Adds a {@code column <> value} condition, the column addressed by its identifier/alias. */
    public final Having notEqual(String column, Object val) {
        tempHaving(new ColumnHaving<>(column), NOT_EQ, val);
        return this;
    }


}
