package io.daobab.result;

import io.daobab.query.base.QueryWhisperer;
import io.daobab.target.buffer.noheap.NoHeapEntities;
import io.daobab.target.buffer.query.BufferQueryEntity;
import io.daobab.target.buffer.single.Entities;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import io.daobab.test.generator.EntityBufferGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class EntityBufferTest implements SakilaTables, QueryWhisperer {

    static Entities<Film> films = null;

    @BeforeAll
    public static void before() {
        EntityBufferGenerator generator = new EntityBufferGenerator();
        films = generator.getFilms();
        films.calculateIndexes();


    }

    @Test
    void test2() {
        EntityBufferGenerator generator = new EntityBufferGenerator();


        Entities<Film> films = generator.getFilms2();

        NoHeapEntities<Film> buf = new NoHeapEntities<>(films);

        for (int i = 0; i < 5; i++) {
            buf.add(films.get(i));
            System.out.println(buf.size() + " - " + buf.get(i));
        }

        buf.remove(3);
        System.out.println(buf.size());
        buf.remove(2);
        System.out.println(buf.size());

        for (int i = 5; i < 10; i++) {
            buf.add(films.get(i));
            System.out.println(buf.size() + " - " + buf.get(buf.size() - 1));
        }

        for (int i = 0; i < buf.size(); i++) {
            System.out.println(buf.get(i));
        }
    }
    @Test
    void test() {
        long start = 0;//System.currentTimeMillis();
        long stop = 0;

        NoHeapEntities<Film> noHeapEntities = new NoHeapEntities<>(films);

        start = System.currentTimeMillis();
        System.out.println("start");

        List<Film> resStream = films.stream()
                .filter(f -> (f.getFilmId() < 995000 && f.getFilmId() > 990000))//||f.getRating().equals("poor"))
                .filter(f -> f.getRating().equals("very good"))
//                .map(e -> e.getTitle())
//                .filter(f->f.getLength()<150)//||f.getRating().equals("poor"))
//                .filter(f->f.getLength()>100)//||f.getRating().equals("poor"))
//                .filter(f->f.getLength()==100)
//                .filter(f->f.getRating().equals("poor"))
//                .filter(f->f.getRentalRate().compareTo(new BigDecimal(18))==1)
                .collect(Collectors.toList());

        System.out.println("stop " + resStream.size());
        stop = System.currentTimeMillis();

        System.out.println("stream result: " + resStream.size() + " time: " + (stop - start));

        //films.forEach(bbuf::add);


        start = System.currentTimeMillis();
        BufferQueryEntity<Film> res = noHeapEntities.select(tabFilm)
                .where(and()
                        .greater(tabFilm.colFilmId(), 990000)
                        .less(tabFilm.colFilmId(), 995000)
                        .equal(tabFilm.colRating(), "very good"))

//                .whereEqual(tabFilm.colFilmId(), 10)

//                        .lessOrEqual(tabFilm.colFilmId(), 10000))
//                                .less(tabFilm.colLength(), 150)
//                                .or(tabFilm.colRating(),EQ,"poor")
//                         .and(tabFilm.colRentalRate(),GT,new BigDecimal(18))
                ;

//        System.out.println(noHeapEntities.size());

        Entities<Film> res2 = res.findMany();
        stop = System.currentTimeMillis();
        System.out.println("daobab result: " + res2.size() + " time: " + (stop - start));


    }
}
