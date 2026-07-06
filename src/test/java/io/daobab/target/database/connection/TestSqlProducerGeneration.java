package io.daobab.target.database.connection;

import io.daobab.dict.DictDatabaseType;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.statement.condition.Order;
import io.daobab.target.database.MockDataBase;
import io.daobab.target.database.query.*;
import io.daobab.target.database.query.frozen.DaoParam;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryField;
import io.daobab.test.dao.Lang;
import io.daobab.test.dao.SakilaTables;
import io.daobab.test.dao.table.Film;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests of the SQL generation done by {@link SqlProducer}.
 */
class TestSqlProducerGeneration implements SakilaTables {

    private final MockDataBase mysql = new MockDataBase();
    private final MockDataBase h2 = new MockDataBase(DictDatabaseType.H2, "2");
    private final MockDataBase oracle = new MockDataBase(DictDatabaseType.ORACLE, "19");
    private final MockDataBase postgres = new MockDataBase(DictDatabaseType.PostgreSQL, "15");
    private final MockDataBase mssql = new MockDataBase(DictDatabaseType.MicrosoftSQL, "2019");

    private String normalize(CharSequence sql) {
        return sql.toString().trim().replaceAll("\\s+", " ");
    }

    private long placeholders(CharSequence sql) {
        return sql.chars().filter(c -> c == '?').count();
    }

    //------------------------------------------------ select ------------------------------------------------

    @Test
    void selectSingleField() {
        String sql = mysql.select(tabFilm.colTitle()).toSqlQuery();

        assertEquals("select ihs1.TITLE from FILM ihs1", normalize(sql));
    }

    @Test
    void selectSeveralColumns() {
        String sql = mysql.select(tabFilm.colTitle(), tabFilm.colLength()).toSqlQuery();

        assertEquals("select ihs1.TITLE,ihs1.LENGTH from FILM ihs1", normalize(sql));
    }

    @Test
    void selectWholeEntityListsEveryColumn() {
        String sql = mysql.select(tabLanguage).toSqlQuery();

        assertEquals("select ihs1.LANGUAGE_ID,ihs1.NAME,ihs1.LAST_UPDATE from LANGUAGE ihs1", normalize(sql));
    }

    //------------------------------------------------ where ------------------------------------------------

    @Test
    void whereEqualBecomesPlaceholder() {
        DataBaseQueryField<Film, String> query = mysql.select(tabFilm.colTitle())
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        String sql = query.toSqlQuery();

        assertEquals("select ihs1.TITLE from FILM ihs1 where ihs1.TITLE = ?", normalize(sql));
        assertEquals(Collections.singletonList("MATRIX"), query.getIdentifierStorage().getBoundParameters());
    }

    @Test
    void whereIsNullProducesNoParameter() {
        DataBaseQueryField<Film, String> query = mysql.select(tabFilm.colTitle())
                .whereIsNull(tabFilm.colDescription());

        String sql = query.toSqlQuery();

        assertEquals(0, placeholders(sql));
        assertTrue(query.getIdentifierStorage().getBoundParameters().isEmpty());
        assertTrue(normalize(sql).contains("ihs1.DESCRIPTION is NULL"));
    }

    @Test
    void whereLikeKeepsWildcardsInParameter() {
        DataBaseQueryField<Film, String> query = mysql.select(tabFilm.colTitle())
                .whereLike(tabFilm.colTitle(), "%MAT%");

        String sql = query.toSqlQuery();

        assertTrue(normalize(sql).contains("ihs1.TITLE LIKE ?"));
        assertEquals(Collections.singletonList("%MAT%"), query.getIdentifierStorage().getBoundParameters());
    }

    @Test
    void whereInProducesPlaceholderPerElement() {
        DataBaseQueryField<Film, String> query = mysql.select(tabFilm.colTitle())
                .whereIn(tabFilm.colLength(), 90, 120, 150);

        String sql = query.toSqlQuery();

        assertTrue(normalize(sql).contains("ihs1.LENGTH in (?,?,?)"));
        assertEquals(Arrays.asList(90, 120, 150), query.getIdentifierStorage().getBoundParameters());
    }

    @Test
    void whereOrCombinesConditions() {
        DataBaseQueryField<Film, String> query = mysql.select(tabFilm.colTitle())
                .whereOr(or -> or
                        .equal(tabFilm.colLength(), 90)
                        .equal(tabFilm.colLength(), 120));

        String sql = query.toSqlQuery();

        assertTrue(normalize(sql).contains("ihs1.LENGTH = ? or ihs1.LENGTH = ?"));
        assertEquals(Arrays.asList(90, 120), query.getIdentifierStorage().getBoundParameters());
    }

    @Test
    void whereColumnAgainstColumnProducesNoParameter() {
        DataBaseQueryField<Film, Integer> query = mysql.select(tabFilm.colLength())
                .whereEqual(tabFilm.colLength(), tabFilm.colRentalDuration());

        String sql = query.toSqlQuery();

        assertEquals(0, placeholders(sql));
        assertTrue(normalize(sql).contains("ihs1.LENGTH = ihs1.RENTAL_DURATION"));
    }

    @Test
    void whereWithInnerSelectSharesParametersInPlaceholderOrder() {
        DataBaseQueryEntity<Film> query = mysql.select(tabFilm)
                .whereAnd(and -> and
                        .equal(tabFilm.colRating(), "PG")
                        .in(tabFilm.colLanguageId(), mysql.select(tabLanguage.colLanguageId())
                                .whereEqual(tabLanguage.colName(), Lang.English)));

        String sql = query.toSqlQuery();
        String normalized = normalize(sql);

        assertTrue(normalized.contains("ihs1.RATING = ?"));
        //the inner query gets its own identifier storage, so its aliases start from ihs1 again
        assertTrue(normalized.contains("ihs1.LANGUAGE_ID in ( select ihs1.LANGUAGE_ID from LANGUAGE ihs1 where ihs1.NAME = ?)"),
                "unexpected sql: " + normalized);
        assertEquals(Arrays.asList("PG", "English"), query.getIdentifierStorage().getBoundParameters());
    }

    //------------------------------------------------ order, group, union ------------------------------------------------

    @Test
    void orderByAscAndDesc() {
        String sql = mysql.select(tabFilm.colTitle())
                .orderBy(() -> new Order().asc(tabFilm.colTitle()).desc(tabFilm.colLength()))
                .toSqlQuery();

        assertTrue(normalize(sql).contains("order by ihs1.TITLE asc, ihs1.LENGTH desc"));
    }

    @Test
    void groupByColumn() {
        String sql = mysql.select(tabFilm.colRating())
                .groupBy(tabFilm.colRating())
                .toSqlQuery();

        assertTrue(normalize(sql).contains("group by ihs1.RATING"));
    }

    @Test
    void unionCollectsParametersOfBothQueries() {
        DataBaseQueryField<Film, String> query = mysql.select(tabFilm.colTitle())
                .whereEqual(tabFilm.colRating(), "PG")
                .union(mysql.select(tabFilmText.colTitle())
                        .whereEqual(tabFilmText.colTitle(), "ALIEN"));

        String sql = query.toSqlQuery();
        String normalized = normalize(sql);

        assertTrue(normalized.contains("union"));
        assertEquals(2, placeholders(sql));
        assertEquals(Arrays.asList("PG", "ALIEN"), query.getIdentifierStorage().getBoundParameters());
    }

    //------------------------------------------------ limit per dialect ------------------------------------------------

    @Test
    void limitMySql() {
        String sql = mysql.select(tabFilm.colTitle()).limitBy(10).toSqlQuery();

        assertTrue(normalize(sql).endsWith("limit 10"));
    }

    @Test
    void limitWithOffsetMySql() {
        String sql = mysql.select(tabFilm.colTitle()).limitBy(5, 10).toSqlQuery();

        assertTrue(normalize(sql).endsWith("limit 5,10"));
    }

    @Test
    void limitWithOffsetPostgres() {
        String sql = postgres.select(tabFilm.colTitle()).limitBy(5, 10).toSqlQuery();

        assertTrue(normalize(sql).endsWith("limit 10 offset 5"));
    }

    @Test
    void limitMicrosoftSqlUsesTop() {
        String sql = mssql.select(tabFilm.colTitle()).limitBy(10).toSqlQuery();

        assertTrue(normalize(sql).startsWith("select top(10) ihs1.TITLE"));
    }

    @Test
    void limitOracleUsesRownum() {
        String sql = oracle.select(tabFilm.colTitle())
                .whereEqual(tabFilm.colRating(), "PG")
                .limitBy(10)
                .toSqlQuery();

        String normalized = normalize(sql);
        assertTrue(normalized.contains("ROWNUM <= '10'"));
        assertTrue(normalized.contains("ihs1.RATING = ?"));
    }

    //------------------------------------------------ delete ------------------------------------------------

    @Test
    void deleteOnMySqlSkipsAliases() {
        DataBaseQueryDelete<Film> query = new DataBaseQueryDelete<>(mysql, tabFilm)
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        String sql = mysql.toDeleteSqlQuery(query);

        assertEquals("delete from FILM where TITLE = ?", normalize(sql));
        assertEquals(Collections.singletonList("MATRIX"), query.getIdentifierStorage().getBoundParameters());
    }

    @Test
    void deleteOnH2UsesAliases() {
        DataBaseQueryDelete<Film> query = new DataBaseQueryDelete<>(h2, tabFilm)
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        String sql = h2.toDeleteSqlQuery(query);

        assertEquals("delete from FILM ihs1 where ihs1.TITLE = ?", normalize(sql));
        assertEquals(Collections.singletonList("MATRIX"), query.getIdentifierStorage().getBoundParameters());
    }

    //------------------------------------------------ update ------------------------------------------------

    @Test
    void updateRendersSetAndWhereAsPlaceholders() {
        DataBaseQueryUpdate<Film> query = mysql.update(tabFilm.colRentalRate(), new BigDecimal("9.99"))
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        QuerySpecialParameters parameters = mysql.toUpdateSqlQuery(query);

        assertEquals("update FILM ihs1 set ihs1.RENTAL_RATE = ? where ihs1.TITLE = ?",
                normalize(parameters.getQuery()));
        assertEquals(3, parameters.getCounter());
        assertEquals(new BigDecimal("9.99"), parameters.getSpecialParameters().get(1));
        assertEquals("MATRIX", parameters.getSpecialParameters().get(2));
    }

    @Test
    void updateWithNullValueRendersNullLiteral() {
        DataBaseQueryUpdate<Film> query = mysql.update(tabFilm.colDescription(), (String) null)
                .whereEqual(tabFilm.colTitle(), "MATRIX");

        QuerySpecialParameters parameters = mysql.toUpdateSqlQuery(query);

        assertTrue(normalize(parameters.getQuery()).contains("set ihs1.DESCRIPTION = null"));
        assertEquals(1, placeholders(parameters.getQuery()));
        assertEquals(2, parameters.getCounter());
        assertEquals("MATRIX", parameters.getSpecialParameters().get(1));
    }

    //------------------------------------------------ insert ------------------------------------------------

    @Test
    void insertRendersPlaceholderPerValue() {
        Film film = new Film()
                .setTitle("MATRIX")
                .setLength(136);

        DataBaseQueryInsert<Film> query = mysql.insert(film);
        QuerySpecialParameters parameters = mysql.toInsertSqlQuery(query);
        String sql = normalize(parameters.getQuery());

        assertEquals(placeholders(sql), parameters.getCounter() - 1L);
        assertTrue(sql.startsWith("insert into FILM"));
        assertTrue(sql.contains("values"));
        assertTrue(parameters.getSpecialParameters().containsValue("MATRIX"));
        assertTrue(parameters.getSpecialParameters().containsValue(136));
    }

    //------------------------------------------------ frozen queries ------------------------------------------------

    @Test
    void frozenQueryInlinesValuesAndKeepsDaoParamMarker() {
        FrozenDataBaseQueryField<Film, String> frozen = mysql.select(tabFilm.colTitle())
                .whereEqual(tabFilm.colTitle(), "MATRIX")
                .limitBy(DaoParam.param(1))
                .freezeQuery();

        String frozenSql = frozen.getFrozenQuery();

        assertEquals(0, placeholders(frozenSql));
        assertTrue(frozenSql.contains("'MATRIX'"));
        assertTrue(frozenSql.contains("~~~1~~~"));
        assertEquals(1, frozen.getQueryParametersInjectionPoints().size());
    }

    @Test
    void frozenQueryParameterInjection() {
        FrozenDataBaseQueryField<Film, String> frozen = mysql.select(tabFilm.colTitle())
                .limitBy(DaoParam.param(1))
                .freezeQuery();

        String sql = mysql.withParameters(frozen, Collections.singletonList(10));

        assertFalse(sql.contains("~~~"));
        assertTrue(sql.contains("10"));
    }

    //------------------------------------------------ regeneration ------------------------------------------------

    @Test
    void regeneratedQueryIsIdentical() {
        DataBaseQueryPlate query = mysql.select(tabFilm.colTitle(), tabFilm.colLength())
                .whereEqual(tabFilm.colRating(), "PG");

        String first = query.toSqlQuery();
        String second = query.toSqlQuery();

        assertEquals(first, second);
        assertEquals(Collections.singletonList("PG"), query.getIdentifierStorage().getBoundParameters());
    }
}
