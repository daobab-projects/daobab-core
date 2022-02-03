package io.daobab.statement.inner;

import io.daobab.model.Entity;
import io.daobab.target.database.query.DataBaseQueryField;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.result.FieldsProvider;

import java.util.List;
import java.util.Optional;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class InnerSelectManyCells<E extends Entity, F> implements QueryExpressionProvider, FieldsProvider<F> {

    private DataBaseQueryField<E, F> select;
    private List<F> bufferedResults = null;

    public InnerSelectManyCells(DataBaseQueryField<E, F> queryField) {
        setSelect(queryField);
    }

    public InnerSelectManyCells(List<F> results) {
        bufferedResults = results;
    }

    public DataBaseQueryField<E, F> getSelect() {
        return select;
    }

    public boolean isDatabaseQuery(){
        return getSelect()!=null;
    }

    private void setSelect(DataBaseQueryField<E, F> queryField) {
        this.select = queryField;
    }

    @Override
    public List<F> findMany() {
        if (bufferedResults != null) {
            return bufferedResults;
        }
        bufferedResults = getSelect().findMany();
        return bufferedResults;
    }

    @Override
    public Optional<F> findFirst() {
        List<F> many=findMany();
        return many.isEmpty()?Optional.empty():Optional.of(many.get(0));
    }

    @Override
    public long countAny() {
        return findMany().size();
    }

}
