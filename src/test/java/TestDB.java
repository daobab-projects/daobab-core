//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;

import io.daobab.model.Entity;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.connection.SqlProducer;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TestDB extends DataBaseTarget implements SqlProducer {//implements MyTables{

    @Override
    protected DataSource initDataSource() {

//
//
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl( "jdbc:h2:tcp://localhost/mm8;MVCC=true;MODE=Oracle" );
//        config.setUsername( "sa" );
//        config.setPassword( "" );
//        config.setDriverClassName("org.h2.Driver");
//
//        config.addDataSourceProperty( "cachePrepStmts" , "true" );
//        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
//        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
//        return new HikariDataSource( config );

        return null;
    }



    @Override
    public List<Entity> initTables() {
        return Collections.emptyList();
    }


}
