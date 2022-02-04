package io.daobab.query.base;

import io.daobab.statement.condition.Limit;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryLimit<Q extends Query> {

    void setTempLimit(Limit limit);

    default Q setLimit(Limit limit) {
        setTempLimit(limit);
        return (Q) this;
    }

    default Q limitBy(Limit limit) {
        this.setLimit(limit);
        return (Q) this;
    }

    default Q limitBy(int limit) {
        this.setLimit(new Limit(limit));
        return (Q) this;
    }

    default Q limitBy(int from, int limit) {
        this.setLimit(new Limit(from, limit));
        return (Q) this;
    }

    default Q page(int pageId, int pageSize) {
        this.setTempPage(pageId, pageSize);
        return (Q) this;
    }

    default void setTempPage(int pageNo, int elementsOnPage) {
        Limit lim = new Limit(elementsOnPage * pageNo, elementsOnPage);
        setTempLimit(lim);
    }
}
