package io.daobab.benchmark;

import io.daobab.creation.EntityCreator;
import io.daobab.test.dao.table.Actor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * The entity hot paths: creation via the immutable setters (a map copy and a new instance per setter),
 * creation via the builder (one map, one instance - the result set row path) and the field reads.
 * <p>
 * Run: {@code mvn -Pbenchmark test-compile exec:exec "-Dbenchmark.include=io.daobab.benchmark.EntityBenchmark.*"}
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class EntityBenchmark {

    private Timestamp now;
    private Actor actor;

    @Setup
    public void setup() {
        now = new Timestamp(System.currentTimeMillis());
        actor = new Actor()
                .setActorId(1)
                .setFirstName("John")
                .setLastName("Doe")
                .setLastUpdate(now);
    }

    @Benchmark
    public Actor createByChainedSetters() {
        return new Actor()
                .setActorId(1)
                .setFirstName("John")
                .setLastName("Doe")
                .setLastUpdate(now);
    }

    @Benchmark
    public Actor createByBuilder() {
        return EntityCreator.builder(Actor.class, 4)
                .add(actor.colActorId(), 1)
                .add(actor.colFirstName(), "John")
                .add(actor.colLastName(), "Doe")
                .add(actor.colLastUpdate(), now)
                .build();
    }

    @Benchmark
    public void readFields(Blackhole bh) {
        bh.consume(actor.getActorId());
        bh.consume(actor.getFirstName());
        bh.consume(actor.getLastName());
        bh.consume(actor.getLastUpdate());
    }

    /**
     * A column access goes through the DaobabCache.
     */
    @Benchmark
    public Object columnLookup() {
        return actor.colFirstName();
    }
}
