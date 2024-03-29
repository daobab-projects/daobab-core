package io.daobab.target.statistic;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.query.base.Query;
import io.daobab.query.base.StatisticQuery;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.statistic.table.StatisticRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface StatisticCollector extends ILoggerBean {

    default <T> List<T> wrapList(StatisticCollectorProvider statisticCollectorProvider, StatisticQuery query, Supplier<List<T>> supplier) {

        if (!statisticCollectorProvider.isStatisticCollectingEnabled()) {
            return supplier.get();
        }

        send(query);
        try {
            List<T> collection = supplier.get();
            received(query, collection.size());
            return collection;
        } catch (Exception e) {
            getLog().error("error:", e);
            error(query, e);
            return new ArrayList<>();
        }

    }

    void send(StatisticQuery query);

    void received(StatisticQuery query, Integer result);

    void error(StatisticQuery query, Throwable result);

    String sendProcedure(String procedureName);

    void receivedProcedure(String procedureName, String identifier, String query, Integer result);

    void errorProcedure(String procedureName, String identifier, String query, Throwable result);

    Entities<StatisticRecord> getTarget();

    String generateIdentifier();

    int getBufferSize();

    void setBufferSize(int bufferSize);

    boolean ignoreSuccessful(boolean ignore);

    void ignoreExecutionTimeBelow(long miliseconds);
}
