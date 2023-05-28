package io.daobab.target.database.query.base;

import io.daobab.query.base.Query;
import io.daobab.query.base.QueryLimit;
import io.daobab.statement.condition.Limit;
import io.daobab.target.database.query.frozen.DaoParam;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface DataBaseQueryLimit<Q extends Query> extends QueryLimit<Q> {

    default Q limitBy(DaoParam param) {
        this.setLimit(new Limit(param));
        return (Q) this;
    }

}
