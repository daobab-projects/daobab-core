package io.daobab.target.statistic;

public interface StatisticCollectorProvider {

    StatisticCollector getStatisticCollector();

    boolean isStatisticCollectingEnabled();

    void enableStatisticCollecting(boolean enabled);

}
