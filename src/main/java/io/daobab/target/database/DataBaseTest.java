package io.daobab.target.database;

import io.daobab.model.Entity;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class DataBaseTest extends DataBaseTarget implements SqlQueryResolver {

    @Override
    protected List<Entity> initTables() {
        return new ArrayList<>();
    }

    @Override
    protected DataSource initDataSource() {
        return null;
    }
}
