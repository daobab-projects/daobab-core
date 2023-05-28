package io.daobab.target.database.connection;

import io.daobab.model.Dual;
import io.daobab.statement.function.FunctionWhispererH2;
import io.daobab.target.database.MockDataBase;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestSqlProducer implements SakilaTables, FunctionWhispererH2 {

    private MockDataBase db = new MockDataBase();

    @Test
    void testMany() {
        String sql = db.select(sumRows(tabFilm.colRentalRate(), tabFilm.colReplacementCost()).as("my"))
                .groupBy(tabFilm.colLanguageId())
                .toSqlQuery();

        assertTrue(areEqual("select (ihs1.RENTAL_RATE + ihs1.REPLACEMENT_COST)  as my \n" +
                " from FILM ihs1 \n" +
                " group by ihs1.LANGUAGE_ID", sql));
    }

    @Test
    void testHaving() {
        Film c = tabFilm;
        String sql = db.select(c.colRating(), count(c.colID()).as("cnt", Long.class))
                .groupBy(c.colRating())
                .logQuery()
                .havingGreater("cnt", 200)
                .toSqlQuery();


        assertTrue(areEqual("select ihs1.RATING,COUNT(ihs1.FILM_ID) as cnt \n" +
                " from FILM ihs1 \n" +
                " group by ihs1.RATING\n" +
                " having  cnt > 200", sql));
    }

    @Test
    void testCountAs() {
        String sql = db.select(
                        db.select(count(tabFilm)).as("cntFilm"),
                        db.select(count(tabCustomer)).as("cntCustomer"))
                .from(new Dual())
                .toSqlQuery();

        System.out.println(sql);
    }

    private boolean areEqual(String expected, String actual) {
        boolean same = expected.trim().replaceAll("\\r|\\n", "").equals(actual.trim().replaceAll("\\r|\\n", ""));

        if (!same) {
            System.out.println("Strings are not equal. \nExpected:\n" + expected + "\nActual:\n" + actual);
        }
        return same;
    }


}
