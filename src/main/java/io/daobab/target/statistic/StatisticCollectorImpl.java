package io.daobab.target.statistic;

import io.daobab.error.DaobabException;
import io.daobab.parser.ParserGeneral;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryType;
import io.daobab.result.Entities;
import io.daobab.result.EntityList;
import io.daobab.target.statistic.dictionary.CallStatus;
import io.daobab.target.statistic.table.StatisticRecord;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
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

        record.setResponseDate(toCurrentTimestamp())
                .setStatus(CallStatus.RECEIVED);
        if (query.getSentQuery() != null) {

            record.setSqlQuery(query.getSentQuery().replaceAll(lineSep, ""));
        }
        long executionTime = getExecutionTime(record);
        if (executionTime < ignoreBelowMilliseconds) {
            remove(query.getIdentifier());
        }
        record.setExecutionTime(executionTime);
    }

    @Override
    public void error(Query<?, ?> query, Throwable result) {
        boolean daobabException = result instanceof DaobabException;
        String couse="";
        if (daobabException) {
            DaobabException daobabException1 = (DaobabException) result;
            couse = daobabException1.getStatusDesc();
        }else{
            couse = result.getMessage();
        }
        StatisticRecord statisticRecord = get(query.getIdentifier());
        if (statisticRecord == null) {
            statisticRecord = new StatisticRecord(query.getIdentifier());
            put(query.getIdentifier(), statisticRecord);
        }
        statisticRecord.setStatus(CallStatus.ERROR)
                .setErrorDesc(couse);
        long executionTime = getExecutionTime(statisticRecord);
        if (executionTime < ignoreBelowMilliseconds) {
            remove(query.getIdentifier());
        }
        statisticRecord.setExecutionTime(executionTime);
    }

    private long getExecutionTime(StatisticRecord record){
        if (record==null||record.getResponseDate()==null||record.getRequestDate()==null){
            return 0;
        }
        return record.getResponseDate().getTime() - record.getRequestDate().getTime();
    }


    @Override
    public String sendProcedure(String procedureName) {
        if (ignoreSuccessful) {
            return "";
        }

        String identifier=generateIdentifier();
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

        StatisticRecord record = get(identifier);
        if (record == null) {
            record = new StatisticRecord(identifier)
                    .setId(identifier)
                    .setRequestDate(toCurrentTimestamp())
                    .setProcedureName(procedureName)
                    .setRequestType(QueryType.PROCEDURE)
                    .setStatus(CallStatus.RECEIVED);
            put(identifier, record);
        }

        record.setResponseDate(toCurrentTimestamp());
        if (query != null) {
            record.setSqlQuery(query.replaceAll(lineSep, ""));
        }
        long executionTime = getExecutionTime(record);
        if (executionTime < ignoreBelowMilliseconds) {
            remove(identifier);
        }
        record.setExecutionTime(executionTime);
    }

    @Override
    public void errorProcedure(String procedureName, String identifier, String query,  Throwable result) {
        boolean daobabException = result instanceof DaobabException;
        String couse="";
        if (daobabException) {
            DaobabException daobabException1 = (DaobabException) result;
            couse = daobabException1.getStatusDesc();
        }else{
            couse = result.getMessage();
        }
        StatisticRecord statisticRecord = get(identifier);
        if (statisticRecord == null) {
            statisticRecord = new StatisticRecord(identifier);
            put(identifier, statisticRecord);
        }
        statisticRecord.setStatus(CallStatus.ERROR)
                .setErrorDesc(couse);
        long executionTime = statisticRecord.getResponseDate().getTime() - statisticRecord.getRequestDate().getTime();
        if (executionTime < ignoreBelowMilliseconds) {
            remove(identifier);
        }
        statisticRecord.setExecutionTime(executionTime);
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
