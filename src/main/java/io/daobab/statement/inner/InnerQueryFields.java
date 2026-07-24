package io.daobab.statement.inner;

import io.daobab.model.Entity;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.result.FieldsProvider;
import io.daobab.target.database.query.DataBaseQueryField;

import java.util.List;
import java.util.Optional;

/**
 * The materialization of a single-field subquery: either a live {@link DataBaseQueryField} (rendered as SQL and
 * executed lazily) or a pre-computed list of values (a buffered result). Its results are cached on first access.
 *
 * @param <E> the entity of the inner query
 * @param <F> the type of the projected field
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class InnerQueryFields<E extends Entity, F> implements QueryExpressionProvider<E>, FieldsProvider<F> {

    private DataBaseQueryField<E, F> innerQuery;
    private List<F> bufferedResults = null;

    /**
     * Backs the expression with a live field subquery.
     */
    public InnerQueryFields(DataBaseQueryField<E, F> queryField) {
        setInnerQuery(queryField);
    }

    /** Backs the expression with a pre-computed list of values. */
    public InnerQueryFields(List<F> results) {
        bufferedResults = results;
    }

    /** The backing field subquery, or {@code null} for a buffered result. */
    public DataBaseQueryField<E, F> getInnerQuery() {
        return innerQuery;
    }

    private void setInnerQuery(DataBaseQueryField<E, F> queryField) {
        this.innerQuery = queryField;
    }

    /** Whether this is a live database subquery (rather than a buffered list). */
    public boolean isDatabaseQuery() {
        return getInnerQuery() != null;
    }

    /** The values of the subquery, executing and caching them on first call. */
    @Override
    public List<F> findMany() {
        if (bufferedResults != null) {
            return bufferedResults;
        }
        bufferedResults = getInnerQuery().findMany();
        return bufferedResults;
    }

    /** The first value of the subquery, or {@link Optional#empty()} when there is none. */
    @Override
    public Optional<F> findFirst() {
        List<F> many = findMany();
        return many.isEmpty() ? Optional.empty() : Optional.of(many.get(0));
    }


}
