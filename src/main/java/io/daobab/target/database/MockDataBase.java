package io.daobab.target.database;

import io.daobab.dict.DictDatabaseType;
import io.daobab.model.Entity;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class MockDataBase extends DataBaseTarget implements SqlQueryResolver {

    public MockDataBase() {
        this(DictDatabaseType.MYSQL, "8");
    }

    public MockDataBase(String dataBaseProductName, String dataBaseMajorVersion) {
        this.dataBaseProductName = dataBaseProductName;
        this.dataBaseMajorVersion = dataBaseMajorVersion;
    }

    @Override
    protected DataSource initDataSource() {
        return null;
    }

    @Override
    protected List<Entity> initTables() {
        return new LinkedList<>();
    }


}
