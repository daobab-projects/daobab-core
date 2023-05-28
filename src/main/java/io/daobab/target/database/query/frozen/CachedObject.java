package io.daobab.target.database.query.frozen;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public final class CachedObject {

    private final String sqlQuery;
    private final LocalDateTime validTo;
    private final Object content;

    public CachedObject(String sqlQuery, TemporalAmount period, Object content) {
        this.sqlQuery = sqlQuery;
        this.validTo = LocalDateTime.now().plus(period);
        this.content = content;
    }

    String getSqlQuery() {
        return sqlQuery;
    }

    public boolean needRefresh() {
        return validTo.isBefore(LocalDateTime.now());
    }

    @SuppressWarnings("unchecked")
    <X> X getContent() {
        return (X) content;
    }


}
