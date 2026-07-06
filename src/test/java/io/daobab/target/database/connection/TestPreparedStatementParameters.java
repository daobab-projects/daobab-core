package io.daobab.target.database.connection;

import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.target.database.MockDataBase;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.query.DataBaseQueryEntity;
import io.daobab.target.database.query.DataBaseQueryUpdate;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that the values are rendered as '?' placeholders
 * and collected as PreparedStatement parameters in the placeholders order.
 */
class TestPreparedStatementParameters implements SakilaTables {

    private final MockDataBase db = new MockDataBase();

    private long countPlaceholders(CharSequence sql) {
        return sql.chars().filter(c -> c == '?').count();
    }

    @Test
    void selectWhereScalar() {
        DataBaseQueryEntity<Film> query = db.select(tabFilm)
                .whereAnd(and -> and
                        .equal(tabFilm.colTitle(), "MATRIX")
                        .greater(tabFilm.colLength(), 120));

        String sql = query.toSqlQuery();
        List<Object> parameters = query.getIdentifierStorage().getBoundParameters();

        assertEquals(2, countPlaceholders(sql));
        assertEquals(Arrays.asList("MATRIX", 120), parameters);
    }

    @Test
    void selectWhereInCollection() {
        DataBaseQueryEntity<Film> query = db.select(tabFilm)
                .whereIn(tabFilm.colLength(), 90, 120, 150);

        String sql = query.toSqlQuery();
        List<Object> parameters = query.getIdentifierStorage().getBoundParameters();

        assertEquals(3, countPlaceholders(sql));
        assertEquals(Arrays.asList(90, 120, 150), parameters);
    }

    @Test
    void regenerationDoesNotDuplicateParameters() {
        DataBaseQueryEntity<Film> query = db.select(tabFilm)
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        query.toSqlQuery();
        String sql = query.toSqlQuery();

        assertEquals(1, countPlaceholders(sql));
        assertEquals(1, query.getIdentifierStorage().getBoundParameters().size());
    }

    @Test
    void updateSetAndWhereKeepPlaceholdersOrder() {
        DataBaseQueryUpdate<Film> query = db.update(tabFilm.colRentalRate(), new BigDecimal("9.99"))
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        QuerySpecialParameters querySpecialParameters = db.toUpdateSqlQuery(query);
        String sql = querySpecialParameters.getQuery().toString();

        assertEquals(2, countPlaceholders(sql));
        assertEquals(3, querySpecialParameters.getCounter());
        assertEquals(new BigDecimal("9.99"), querySpecialParameters.getSpecialParameters().get(1));
        assertEquals("MATRIX", querySpecialParameters.getSpecialParameters().get(2));
    }

    @Test
    void deleteWhereCollectsParameters() {
        DataBaseQueryDelete<Film> query = new DataBaseQueryDelete<>(db, tabFilm)
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        String sql = db.toDeleteSqlQuery(query);
        List<Object> parameters = query.getIdentifierStorage().getBoundParameters();

        assertEquals(1, countPlaceholders(sql));
        assertEquals(Arrays.asList("MATRIX"), parameters);
    }

    @Test
    void frozenQueryKeepsInlinedValues() {
        String sql = db.select(tabFilm)
                .whereEqual(tabFilm.colTitle(), "MATRIX")
                .freezeQuery()
                .getFrozenQuery();

        assertEquals(0, countPlaceholders(sql));
        assertTrue(sql.contains("'MATRIX'"));
    }
}
