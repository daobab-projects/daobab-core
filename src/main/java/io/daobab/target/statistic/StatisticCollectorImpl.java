package io.daobab.target.statistic;

import io.daobab.error.DaobabException;
import io.daobab.parser.ParserGeneral;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryType;
import io.daobab.query.base.StatisticQuery;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.statistic.dictionary.CallStatus;
import io.daobab.target.statistic.table.StatisticRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class StatisticCollectorImpl extends LinkedHashMap<String, StatisticRecord> implements StatisticCollector, ParserGeneral {

    private final String lineSep = System.getProperty("line.separator");
    private final boolean ignoreSuccessful = false;
    protected transient Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private int bufferSize = 500;
    private long ignoreBelowMilliseconds = 0;

    @Override
    public void send(StatisticQuery query) {
        if (ignoreSuccessful) {
            return;
        }
        StatisticRecord statisticRecord = new StatisticRecord(query.getIdentifier());
        if (query.getIdentifier() == null) {
            query.setIdentifier(generateIdentifier());
        }
        statisticRecord.setId(query.getIdentifier())
                .setRequestDate(toCurrentTimestamp())
                .setRelatedEntity(query.getEntityName())
                .setRequestType(query.getQueryType())
                .setStatus(CallStatus.SEND);

        put(query.getIdentifier(), statisticRecord);
    }

    @Override
    public void received(StatisticQuery query, Integer result) {
        if (ignoreSuccessful) {
            return;
        }
        if (query.getIdentifier() == null) {
            query.setIdentifier(generateIdentifier());
        }
        StatisticRecord statisticRecord = get(query.getIdentifier());
        if (statisticRecord == null) {
            statisticRecord = new StatisticRecord(query.getIdentifier());
            put(query.getIdentifier(), statisticRecord);
        }

        statisticRecord.setResponseDate(toCurrentTimestamp())
                .setStatus(CallStatus.RECEIVED);
        if (query.getSentQuery() != null) {

            statisticRecord.setSqlQuery(query.getSentQuery().replaceAll(lineSep, ""));
        }
        long executionTime = getExecutionTime(statisticRecord);
        if (executionTime < ignoreBelowMilliseconds) {
            remove(query.getIdentifier());
        }
        statisticRecord.setExecutionTime(executionTime);
    }

    @Override
    public void error(StatisticQuery query, Throwable result) {
        boolean daobabException = result instanceof DaobabException;
        String cause = "";
        if (daobabException) {
            DaobabException daobabException1 = (DaobabException) result;
            cause = daobabException1.getStatusDesc();
        } else {
            cause = result.getMessage();
        }
        StatisticRecord statisticRecord = get(query.getIdentifier());
        if (statisticRecord == null) {
            statisticRecord = new StatisticRecord(query.getIdentifier());
            put(query.getIdentifier(), statisticRecord);
        }
        statisticRecord.setStatus(CallStatus.ERROR)
                .setErrorDesc(cause);
        long executionTime = getExecutionTime(statisticRecord);
        if (executionTime < ignoreBelowMilliseconds) {
            remove(query.getIdentifier());
        }
        statisticRecord.setExecutionTime(executionTime);
    }

    private long getExecutionTime(StatisticRecord statisticRecord) {
        if (statisticRecord == null || statisticRecord.getResponseDate() == null || statisticRecord.getRequestDate() == null) {
            return 0;
        }
        return statisticRecord.getResponseDate().getTime() - statisticRecord.getRequestDate().getTime();
    }


    @Override
    public String sendProcedure(String procedureName) {
        if (ignoreSuccessful) {
            return "";
        }

        String identifier = generateIdentifier();
        StatisticRecord statisticRecord = new StatisticRecord(identifier)
                .setId(identifier)
                .setRequestDate(toCurrentTimestamp())
                .setProcedureName(procedureName)
                .setRequestType(QueryType.PROCEDURE)
                .setStatus(CallStatus.SEND);

        put(identifier, statisticRecord);

        return identifier;
    }

    @Override
    public void receivedProcedure(String procedureName, String identifier, String query, Integer result) {
        if (ignoreSuccessful) {
            return;
        }

        StatisticRecord statisticRecord = computeIfAbsent(identifier, r ->
                new StatisticRecord(identifier)
                        .setId(identifier)
                        .setRequestDate(toCurrentTimestamp())
                        .setProcedureName(procedureName)
                        .setRequestType(QueryType.PROCEDURE)
                        .setStatus(CallStatus.RECEIVED));

        statisticRecord.setResponseDate(toCurrentTimestamp());
        if (query != null) {
            statisticRecord.setSqlQuery(query.replaceAll(lineSep, ""));
        }
        long executionTime = getExecutionTime(statisticRecord);
        if (executionTime < ignoreBelowMilliseconds) {
            remove(identifier);
        }
        statisticRecord.setExecutionTime(executionTime);
    }

    @Override
    public void errorProcedure(String procedureName, String identifier, String query, Throwable result) {
        boolean daobabException = result instanceof DaobabException;
        String cause;
        if (daobabException) {
            DaobabException daobabException1 = (DaobabException) result;
            cause = daobabException1.getStatusDesc();
        } else {
            cause = result.getMessage();
        }
        StatisticRecord statisticRecord = get(identifier);
        if (statisticRecord == null) {
            statisticRecord = new StatisticRecord(identifier);
            put(identifier, statisticRecord);
        }
        statisticRecord.setStatus(CallStatus.ERROR)
                .setErrorDesc(cause);
        long executionTime = statisticRecord.getResponseDate().getTime() - statisticRecord.getRequestDate().getTime();
        if (executionTime < ignoreBelowMilliseconds) {
            remove(identifier);
        }
        statisticRecord.setExecutionTime(executionTime);
    }


    public Entities<StatisticRecord> getTarget() {
        EntityList<StatisticRecord> rv = new EntityList<>(new ArrayList<>(values()), StatisticRecord.class);
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

    @Override
    public Logger getLog() {
        return log;
    }
}
