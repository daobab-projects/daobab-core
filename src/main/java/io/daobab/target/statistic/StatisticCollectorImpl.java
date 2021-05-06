package io.daobab.target.statistic;

import io.daobab.error.DaobabException;
import io.daobab.parser.ParserGeneral;
import io.daobab.query.base.Query;
import io.daobab.result.Entities;
import io.daobab.result.EntityList;
import io.daobab.target.statistic.table.StatisticRecord;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


public class StatisticCollectorImpl extends LinkedHashMap<String, StatisticRecord> implements StatisticCollector, ParserGeneral {


    private int bufferSize = 500;
    private String lineSep = System.getProperty("line.separator");

    private boolean ignoreSuccessful = false;
    private long ignoreBelowMilliseconds = 0;


    @Override
    public void send(Query<?, ?> query) {
        if (ignoreSuccessful) {
            return;
        }
        StatisticRecord record = new StatisticRecord(query.getIdentifier());
        if (query.getIdentifier() == null) {
            query.setIdentifier(generateIdentifier());
        }
        record.setId(query.getIdentifier())
                .setRequestDate(toCurrentTimestamp())
                .setRelatedEntity(query.getEntityName())
                .setRequestType(query.getQueryType());

        put(query.getIdentifier(), record);
    }

    @Override
    public void received(Query<?, ?> query, Integer result) {
        if (ignoreSuccessful) {
            return;
        }
        if (query.getIdentifier() == null) {
            query.setIdentifier(generateIdentifier());
        }
        StatisticRecord record = get(query.getIdentifier());
        if (record == null) {
            record = new StatisticRecord(query.getIdentifier());
            put(query.getIdentifier(), record);
        }

        record.setResponseDate(toCurrentTimestamp());
        if (query.getSentQuery() != null) {

            record.setSqlQuery(query.getSentQuery().replaceAll(lineSep, ""));
        }
        long executionTime = record.getResponseDate().getTime() - record.getRequestDate().getTime();
        if (executionTime < ignoreBelowMilliseconds) {
            remove(query.getIdentifier());
        }
        record.setExecutionTime(executionTime);
    }

    @Override
    public void error(Query<?, ?> query, Throwable result) {
        boolean daobabException = result instanceof DaobabException;
        if (daobabException) {
            DaobabException daobabException1 = (DaobabException) result;
            String couse = daobabException1.getStatusDesc();
        }
    }


    public Entities<StatisticRecord> getTarget() {
        EntityList rv = new EntityList<>(new ArrayList<>(values()), StatisticRecord.class);
        rv.enableStatisticCollecting(false);
        return rv;
    }


    public String generateIdentifier() {
        return UUID.randomUUID().toString();
    }


    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public boolean ignoreSuccessful(boolean ignore) {
        return ignoreSuccessful;
    }

    @Override
    public void ignoreExecutionTimeBelow(long milliseconds) {
        ignoreBelowMilliseconds = milliseconds > 0 ? milliseconds : 0;
    }


    @Override
    protected boolean removeEldestEntry(Map.Entry<String, StatisticRecord> eldest) {
        return size() > bufferSize;
    }

}
