package io.daobab.target.statistic;

import io.daobab.result.Entities;
import io.daobab.target.statistic.table.StatisticRecord;

public interface StatisticProvider {

    Entities<StatisticRecord> getStatistics();
}
