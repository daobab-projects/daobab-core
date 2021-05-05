package daobab.result;

import io.daobab.result.Entities;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import io.daobab.test.generator.EntityBufferGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ProjectionBoxTest implements SakilaTables {

    static Entities<Film> films=null;

    @BeforeAll
    public static void before(){
        EntityBufferGenerator generator=new EntityBufferGenerator();
        films=generator.getFilms();
        films.calculateIndexes();

    }


    @Test
    public void test(){
        long start = 0;//System.currentTimeMillis();
        long stop = 0;

//        start=System.currentTimeMillis();
//        Plates res2=films.select(tabFilm.colFilmId(),tabFilm.colRentalRate())
//                .where(or()
//                                .lteq(tabFilm.colFilmId(),LTEQ,100)
//                                .or(tabFilm.colLength(),GT,10)
////                                .or(tabFilm.colRating(),EQ,"poor")
////                         .and(tabFilm.colRentalRate(),GT,new BigDecimal(18))
//                ).findMany();


//        Entities<Film> res=res2.findMany();

//        System.out.println("projectionbox result: "+res2.size()+" time: "+(stop-start));

//        List<Integer> result=res2.select(tabFilm.colFilmId()).where(and()
//                .and(tabFilm.colRentalRate(),LTEQ,toBigDecimal(5))
//                .and(tabFilm.colLength(),LTEQ,70))
//                .findMany();

        stop = System.currentTimeMillis();
//        System.out.println("daobab result: "+result.size()+" time: "+(stop-start));


    }
}
