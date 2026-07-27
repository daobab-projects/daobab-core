package io.daobab.target.buffer.nonheap;

import io.daobab.test.dao.table.Film;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Exercises the off-heap {@link NonHeapEntities} buffer: build, add, remove and read back, asserting that the
 * bookkeeping (size, positions, values) stays correct - the scenario {@code EntityBufferTest.test2} runs.
 */
class TestNonHeapEntities {

    private static Film film(int id, String rating, int length) {
        //Table is immutable: each setter returns a new entity, so chain them
        return new Film()
                .setFilmId(id)
                .setDescription("d" + id)
                .setRating(rating)
                .setLength(length);
    }

    private static List<Film> films(int count) {
        List<Film> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(film(i, i % 2 == 0 ? "good" : "poor", 60 + i));
        }
        return list;
    }

    @Test
    void buildsFromListAndReadsBack() {
        List<Film> source = films(10);
        NonHeapEntities<Film> buf = new NonHeapEntities<>(source);

        assertEquals(10, buf.size());
        for (int i = 0; i < buf.size(); i++) {
            Film f = buf.get(i);
            assertEquals(i + 1, f.getFilmId());
            assertEquals(60 + i + 1, f.getLength());
            assertEquals(source.get(i).getRating(), f.getRating());
        }
    }

    @Test
    void addRemoveAddKeepsSizeAndValues() {
        List<Film> source = films(10);
        NonHeapEntities<Film> buf = new NonHeapEntities<>(source);

        //add 5 more (ids 1..5 again)
        for (int i = 0; i < 5; i++) {
            buf.add(source.get(i));
        }
        assertEquals(15, buf.size());

        //remove two rows
        buf.remove(3);
        buf.remove(2);
        assertEquals(13, buf.size());

        //add 5 more (ids 6..10)
        for (int i = 5; i < 10; i++) {
            buf.add(source.get(i));
        }
        assertEquals(18, buf.size());

        //every row must be readable and consistent (no crash, no null id)
        for (int i = 0; i < buf.size(); i++) {
            Film f = buf.get(i);
            assertEquals(f.getFilmId(), f.getLength() - 60, "row " + i + " is inconsistent: " + f);
        }
    }
}
