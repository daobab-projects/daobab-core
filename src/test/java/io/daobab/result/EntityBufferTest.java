package io.daobab.result;

import io.daobab.query.base.QueryWhisperer;
import io.daobab.target.buffer.nonheap.NonHeapEntities;
import io.daobab.target.buffer.query.BufferQueryEntity;
import io.daobab.target.buffer.single.Entities;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import io.daobab.test.generator.EntityBufferGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class EntityBufferTest implements SakilaTables, QueryWhisperer {

    @Test
    void test2() {
        EntityBufferGenerator generator = new EntityBufferGenerator();
        Entities<Film> films = generator.getFilms2();

        //the constructor buffers all 10 entities off-heap
        NonHeapEntities<Film> buf = new NonHeapEntities<>(films);
        assertEquals(10, buf.size());

        //add five more rows
        for (int i = 0; i < 5; i++) {
            buf.add(films.get(i));
        }
        assertEquals(15, buf.size());

        //remove two rows: the freed physical slots are reused by the next adds
        buf.remove(3);
        buf.remove(2);
        assertEquals(13, buf.size());

        for (int i = 5; i < 10; i++) {
            buf.add(films.get(i));
        }
        assertEquals(18, buf.size());

        //every row stays readable: a real, distinct entity (getFilms2 builds filmId 1..10 with a random rating/length)
        for (int i = 0; i < buf.size(); i++) {
            Film f = buf.get(i);
            assertNotNull(f.getFilmId(), "row " + i + " lost its id");
            assertTrue(f.getFilmId() >= 1 && f.getFilmId() <= 10, "row " + i + " has an unexpected id: " + f.getFilmId());
            assertNotNull(f.getRating(), "row " + i + " lost its rating");
        }
    }

    @Test
    @Disabled("benchmark: builds 1,000,000 entities on the heap, compares stream vs off-heap query")
    void test() {
        EntityBufferGenerator generator = new EntityBufferGenerator();
        Entities<Film> films = generator.getFilms();
        films.calculateIndexes();

        NonHeapEntities<Film> nonHeapEntities = new NonHeapEntities<>(films);

        long start = System.currentTimeMillis();
        List<Film> resStream = films.stream()
                .filter(f -> (f.getFilmId() < 995000 && f.getFilmId() > 990000))
                .filter(f -> f.getRating().equals("very good"))
                .collect(Collectors.toList());
        long stop = System.currentTimeMillis();
        System.out.println("stream result: " + resStream.size() + " time: " + (stop - start));

        start = System.currentTimeMillis();
        BufferQueryEntity<Film> res = nonHeapEntities.select(tabFilm)
                .where(and()
                        .greater(tabFilm.colFilmId(), 990000)
                        .less(tabFilm.colFilmId(), 995000)
                        .equal(tabFilm.colRating(), "very good"));
        Entities<Film> res2 = res.findMany();
        stop = System.currentTimeMillis();
        System.out.println("daobab result: " + res2.size() + " time: " + (stop - start));
    }
}
