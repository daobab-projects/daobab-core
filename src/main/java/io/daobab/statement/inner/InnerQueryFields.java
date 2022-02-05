package io.daobab.statement.inner;

import io.daobab.model.Entity;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.result.FieldsProvider;
import io.daobab.target.database.query.DataBaseQueryField;

import java.util.List;
import java.util.Optional;

/**
 * Contains inner query or fields collection.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class InnerQueryFields<E extends Entity, F> implements QueryExpressionProvider<E>, FieldsProvider<F> {

    private DataBaseQueryField<E, F> innerQuery;
    private List<F> bufferedResults = null;

    public InnerQueryFields(DataBaseQueryField<E, F> queryField) {
        setInnerQuery(queryField);
    }

    public InnerQueryFields(List<F> results) {
        bufferedResults = results;
    }

    public DataBaseQueryField<E, F> getInnerQuery() {
        return innerQuery;
    }

    private void setInnerQuery(DataBaseQueryField<E, F> queryField) {
        this.innerQuery = queryField;
    }

    public boolean isDatabaseQuery() {
        return getInnerQuery() != null;
    }

    @Override
    public List<F> findMany() {
        if (bufferedResults != null) {
            return bufferedResults;
        }
        bufferedResults = getInnerQuery().findMany();
        return bufferedResults;
    }

    @Override
    public Optional<F> findFirst() {
        List<F> many = findMany();
        return many.isEmpty() ? Optional.empty() : Optional.of(many.get(0));
    }

    @Override
    public long countAny() {
        return findMany().size();
    }

}
