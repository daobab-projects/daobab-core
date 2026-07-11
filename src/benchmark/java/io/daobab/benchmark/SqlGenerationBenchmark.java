package io.daobab.benchmark;

import io.daobab.target.database.MockDataBase;
import io.daobab.test.dao.SakilaTables;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * The SQL generation cost of the query builder and the SqlProducer, without any database involved.
 * <p>
 * Run: {@code mvn -Pbenchmark test-compile exec:exec "-Dbenchmark.include=io.daobab.benchmark.SqlGenerationBenchmark.*"}
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class SqlGenerationBenchmark implements SakilaTables {

    private MockDataBase mysql;

    @Setup
    public void setup() {
        mysql = new MockDataBase();
    }

    @Benchmark
    public String selectWholeEntity() {
        return mysql.select(tabFilm).toSqlQuery();
    }

    @Benchmark
    public String selectSeveralColumns() {
        return mysql.select(tabFilm.colTitle(), tabFilm.colLength()).toSqlQuery();
    }

    @Benchmark
    public String selectSingleFieldWithWhere() {
        return mysql.select(tabFilm.colTitle())
                .whereEqual(tabFilm.colTitle(), "MATRIX")
                .toSqlQuery();
    }
}
