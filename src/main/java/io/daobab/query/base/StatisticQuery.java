package io.daobab.query.base;

public interface StatisticQuery {

    String getIdentifier();

    String getEntityName();

    void setIdentifier(String identifier);


    String getSentQuery();

    QueryType getQueryType();
}
