package io.daobab.result;

import io.daobab.query.base.QueryWhisperer;
import io.daobab.target.buffer.nonheap.NonHeapEntities;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Exercises the WHERE filtering of the buffer query paths - the heap {@link EntityList} (indexed and
 * non-indexed) and the off-heap {@link NonHeapEntities} (which runs {@code filterByIndexes}) - asserting the
 * matched rows, so the index/filter optimizations stay correct.
 */
class TestBufferQueryFilter implements SakilaTables, QueryWhisperer {

    private static List<Film> tenFilms() {
        List<Film> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new Film()
                    .setFilmId(i)
                    .setDescription("d" + i)
                    .setRating(i % 2 == 0 ? "good" : "poor")
                    .setLength(i));
        }
        return list;
    }

    private static TreeSet<Integer> idsOf(List<Film> films) {
        return films.stream().map(Film::getFilmId).collect(Collectors.toCollection(TreeSet::new));
    }

    @Test
    void heapIndexedAndFilter() {
        EntityList<Film> list = new EntityList<>(tenFilms(), Film.class);
        list.calculateIndexes();

        Entities<Film> res = list.select(tabFilm)
                .where(and()
                        .greater(tabFilm.colFilmId(), 3)
                        .equal(tabFilm.colRating(), "good"))
                .findMany();

        // filmId > 3 AND rating == "good" -> even ids above 3
        assertEquals(new TreeSet<>(List.of(4, 6, 8, 10)), idsOf(res));
    }

    @Test
    void heapNonIndexedAndFilter() {
        //no calculateIndexes(): the indexRepository is empty, so the direct finalFilter path runs
        EntityList<Film> list = new EntityList<>(tenFilms(), Film.class);

        Entities<Film> res = list.select(tabFilm)
                .where(and()
                        .greater(tabFilm.colFilmId(), 3)
                        .equal(tabFilm.colRating(), "good"))
                .findMany();

        assertEquals(new TreeSet<>(List.of(4, 6, 8, 10)), idsOf(res));
    }

    @Test
    void nonHeapAndFilter() {
        NonHeapEntities<Film> buf = new NonHeapEntities<>(tenFilms());

        Entities<Film> res = buf.select(tabFilm)
                .where(and()
                        .greater(tabFilm.colFilmId(), 3)
                        .equal(tabFilm.colRating(), "good"))
                .findMany();

        assertEquals(new TreeSet<>(List.of(4, 6, 8, 10)), idsOf(res));
    }

    @Test
    void nonHeapOrFilter() {
        NonHeapEntities<Film> buf = new NonHeapEntities<>(tenFilms());

        Entities<Film> res = buf.select(tabFilm)
                .where(or()
                        .equal(tabFilm.colFilmId(), 2)
                        .equal(tabFilm.colFilmId(), 7))
                .findMany();

        assertEquals(new TreeSet<>(List.of(2, 7)), idsOf(res));
    }

    @Test
    void nonHeapSingleEqualFilter() {
        NonHeapEntities<Film> buf = new NonHeapEntities<>(tenFilms());

        Entities<Film> res = buf.select(tabFilm)
                .where(and().equal(tabFilm.colFilmId(), 5))
                .findMany();

        assertEquals(new TreeSet<>(List.of(5)), idsOf(res));
    }

    // ---------------------------------------------------------------- range boundary coverage
    // guards the off-by-one that used to drop the maximum key from an indexed range query

    @Test
    void heapRangeBoundaries() {
        EntityList<Film> list = new EntityList<>(tenFilms(), Film.class);
        list.calculateIndexes();
        assertRangeBoundaries(list);
    }

    @Test
    void nonHeapRangeBoundaries() {
        assertRangeBoundaries(new NonHeapEntities<>(tenFilms()));
    }

    private void assertRangeBoundaries(Object buffer) {
        // the buffer is either a heap EntityList or an off-heap NonHeapEntities - both share the query API
        io.daobab.target.buffer.BufferQueryTarget target = (io.daobab.target.buffer.BufferQueryTarget) buffer;

        // greater than the second-highest key must still return the maximum
        assertEquals(new TreeSet<>(List.of(10)),
                idsOf(target.select(tabFilm).where(and().greater(tabFilm.colFilmId(), 9)).findMany()));
        assertEquals(new TreeSet<>(List.of(9, 10)),
                idsOf(target.select(tabFilm).where(and().greaterOrEqual(tabFilm.colFilmId(), 9)).findMany()));
        // nothing above the maximum
        assertEquals(new TreeSet<>(),
                idsOf(target.select(tabFilm).where(and().greater(tabFilm.colFilmId(), 10)).findMany()));
        // less-than around the minimum
        assertEquals(new TreeSet<>(List.of(1)),
                idsOf(target.select(tabFilm).where(and().less(tabFilm.colFilmId(), 2)).findMany()));
        assertEquals(new TreeSet<>(List.of(1, 2)),
                idsOf(target.select(tabFilm).where(and().lessOrEqual(tabFilm.colFilmId(), 2)).findMany()));
        // a whole-range scan returns every row
        assertEquals(new TreeSet<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
                idsOf(target.select(tabFilm).where(and().greaterOrEqual(tabFilm.colFilmId(), 1)).findMany()));
    }
}
