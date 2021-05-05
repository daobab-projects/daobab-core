package io.daobab.target.statistic;

import io.daobab.query.base.Query;
import io.daobab.result.Entities;
import io.daobab.target.statistic.table.StatisticRecord;

public interface StatisticCollector {

    void send(Query<?, ?> query);

    void received(Query<?, ?> query, Integer result);

    void error(Query<?, ?> query, Throwable result);

    Entities<StatisticRecord> getTarget();

    String generateIdentifier();

    int getBufferSize();

    void setBufferSize(int bufferSize);

    boolean ignoreSuccessful(boolean ignore);

    void ignoreExecutionTimeBelow(long miliseconds);
}
