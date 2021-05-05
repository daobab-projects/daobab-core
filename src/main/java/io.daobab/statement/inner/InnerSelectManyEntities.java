package io.daobab.statement.inner;

import io.daobab.model.Entity;
import io.daobab.query.QueryEntity;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.result.Entities;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class InnerSelectManyEntities<E extends Entity> implements QueryExpressionProvider {

    private Query<E,?> select;

    private Entities<E> cachedResults = null;

    public InnerSelectManyEntities(QueryEntity<E> sel) {
        setSelect(sel);
    }

    public InnerSelectManyEntities(Entities<E> res) {
        cachedResults = res;
    }

    public Query<E,?> getSelect() {
        return select;
    }

    private void setSelect(QueryEntity<E> select) {
        this.select = select;
    }

    @Override
    public boolean isResultCached() {
        return cachedResults != null;
    }

}
