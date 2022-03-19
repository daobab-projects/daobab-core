package io.daobab.statement.base;

import io.daobab.target.buffer.function.FunctionWhispererBuffer;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.buffer.single.Plates;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TestBufferFunction implements FunctionWhispererBuffer, SakilaTables {

    @Test
    void test() {
        List<Film> filmy = new ArrayList<>();

        filmy.add(new Film().setId(5).setDescription("a"));
        filmy.add(new Film().setId(12).setDescription("bb"));
        filmy.add(new Film().setId(3).setDescription("dbc"));

        Entities<Film> films = new EntityList<>(filmy, Film.class);

        Plates rv = films.select(upper(lower(tabFilm.colDescription())).as("cos"),
                        length(tabFilm.colDescription())
//                        count(distinct(tabFilm.colID())),
//                        sum(tabFilm.colID()),
//                        max(tabFilm.colID()).as("minimum"),
//                        avg(tabFilm.colID()).as("srednia").cast(BigDecimal.class),
//                        min(tabFilm.colID()).as("maximum"))
                )
                .orderDescBy(tabFilm.colID())
                .findMany();//.forEach(p -> System.out.println(p.toJSON()));

        System.out.println(rv.toJSON());
    }

    @Test
    void testField() {
        List<Film> filmy = new ArrayList<>();

        filmy.add(new Film().setId(5).setDescription("a"));
        filmy.add(new Film().setId(2).setDescription("ab"));
        filmy.add(new Film().setId(3).setDescription("abc"));

        Entities<Film> films = new EntityList<>(filmy, Film.class);

        List<Long> s = films.select(count(tabFilm.colDescription()))
//                )
                .findMany();

        s.forEach(System.out::println);
    }
}
