package io.daobab.statement.inner;

import io.daobab.model.Entity;
import io.daobab.target.database.query.DataBaseQueryEntity;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.target.buffer.single.Entities;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class InnerSelectManyEntities<E extends Entity> implements QueryExpressionProvider {

    private Query<E,?,?> select;

    private Entities<E> cachedResults = null;

    public InnerSelectManyEntities(DataBaseQueryEntity<E> sel) {
        setSelect(sel);
    }

    public InnerSelectManyEntities(Entities<E> res) {
        cachedResults = res;
    }

    public Query<E,?,?> getSelect() {
        return select;
    }

    private void setSelect(DataBaseQueryEntity<E> select) {
        this.select = select;
    }

    @Override
    public boolean isResultCached() {
        return cachedResults != null;
    }

}
