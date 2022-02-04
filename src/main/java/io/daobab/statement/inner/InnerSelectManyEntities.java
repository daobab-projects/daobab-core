package io.daobab.statement.inner;

import io.daobab.model.Entity;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.query.DataBaseQueryBase;
import io.daobab.target.database.query.DataBaseQueryEntity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class InnerSelectManyEntities<E extends Entity> implements QueryExpressionProvider {

    private DataBaseQueryBase<E, ?> select;

    private Entities<E> cachedResults = null;

    public InnerSelectManyEntities(DataBaseQueryEntity<E> sel) {
        setSelect(sel);
    }

    public InnerSelectManyEntities(Entities<E> res) {
        cachedResults = res;
    }

    public DataBaseQueryBase<E, ?> getSelect() {
        return select;
    }

    private void setSelect(DataBaseQueryEntity<E> select) {
        this.select = select;
    }

}
