package io.daobab.statement.inner;

import io.daobab.model.Entity;
import io.daobab.query.QueryField;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.result.FieldsProvider;

import java.util.List;
import java.util.Optional;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class InnerSelectManyCells<E extends Entity, F> implements QueryExpressionProvider, FieldsProvider<F> {

    private QueryField<E, F> select;

    private List<F> cachedResults = null;


    public InnerSelectManyCells(QueryField<E, F> sel) {
        setSelect(sel);
    }

    public InnerSelectManyCells(List<F> res) {
        cachedResults = res;
    }

    public QueryField<E, F> getSelect() {
        return select;
    }

    private void setSelect(QueryField<E, F> select) {
        this.select = select;
    }



    @Override
    public List<F> findMany() {
        if (cachedResults != null) {
            return cachedResults;
        }
        List<F> field = getSelect().findMany();
        if (field != null) cachedResults = field;

        return field;
    }

    @Override
    public Optional<F> findFirst() {
        List<F> many=findMany();
        return many.isEmpty()?Optional.empty():Optional.of(many.get(0));
    }



    @Override
    public long countAny() {
        return getSelect().countAny();
    }

    @Override
    public boolean isResultCached() {
        return cachedResults != null;
    }


}
