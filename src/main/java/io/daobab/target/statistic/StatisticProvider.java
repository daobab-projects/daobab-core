package io.daobab.target.statistic;

import io.daobab.target.buffer.single.Entities;
import io.daobab.target.statistic.table.StatisticRecord;

public interface StatisticProvider {

    Entities<StatisticRecord> getStatistics();
}
