package io.daobab.target;

import io.daobab.model.Entity;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.SqlQueryResolver;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class TestTarget extends DataBaseTarget implements SqlQueryResolver {

    private final List<Entity> tables = new LinkedList<>();


    @Override
    protected List<Entity> initTables() {
        return tables;
    }

    @Override
    protected DataSource initDataSource() {
        return null;
    }


}
