package io.daobab.target.database;

import io.daobab.model.IdGeneratorType;
import io.daobab.error.*;
import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.model.PrimaryKey;
import io.daobab.model.TableColumn;
import io.daobab.parser.ParserNumber;
import io.daobab.query.*;
import io.daobab.query.base.Query;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.result.Entities;
import io.daobab.result.EntityList;
import io.daobab.result.PlateBuffer;
import io.daobab.target.QueryTarget;
import io.daobab.target.meta.MetaData;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.OperationType;
import io.daobab.target.statistic.StatisticCollectorProvider;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.String.format;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface DataBaseTargetLogic extends QueryConsumer, QueryTarget, TransactionalTarget, StatisticCollectorProvider {

    String getDataBaseProductName();

    default boolean isConnectedToDatabase() {
        return true;
    }

    DataSource getDataSource();

    MetaData getMetaData();

    AccessProtector getAccessProtector();

    default void closeConnection(Connection connection) {
        closeConnectionPsychically(connection);
    }

    default Connection getConnection() {
        return openConnection();
    }

    default void closeConnectionPsychically(Connection connection) {
        try {
            if (getLog().isDebugEnabled()) {
                getLog().debug(format("Start closeConnectionIfOpen (connection = %s)", connection));
            }
            if (connection == null) {
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Cannot close null connection");
                }
            } else {
                if (connection.isClosed()) {
                    if (getLog().isDebugEnabled()) {
                        getLog().debug("Cannot close already closed connection");
                    }
                } else {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            if (getLog().isDebugEnabled()) {
                getLog().debug("Finish closeConnectionIfOpen");
            }
        }
    }


    default Connection openConnection() {
        try {
            getLog().debug("Start getConnection");
            Connection connection = getDataSource().getConnection();
            getLog().debug(format("Connection retrieved: %s",connection));
            return connection;
        } catch (SQLException e) {
            throw new DaobabSQLException("Error while retrieving connection from datasource", e);
        } finally {
            getLog().debug("Finish getConnection");
        }
    }

    default <F> F getNextId(Connection conn, String sequenceName, Class<F> fieldClazz) {
        if (sequenceName == null || sequenceName.isEmpty()) throw new NoSequenceException();
        getLog().debug(format("Start getNextId (conn = %s, sequenceName =%s)",conn,sequenceName));
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("select " + sequenceName + ".nextval from dual");
            getLog().debug("Executing statement");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return RSReader.readColumnValue(rs, 1, fieldClazz);
            } else {
                throw new DaobabException("No data from result set");
            }
        } catch (SQLException e) {
            throw new DaobabSQLException("Error during generation of ID for object type = " + sequenceName, e);
        } finally {
            RSReader.closeStatement(stmt, this);
            getLog().debug("Finish getNextId");
        }
    }


    default <E extends Entity> int delete(QueryDelete<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.DELETE);
        if (query.getWhereWrapper() == null) {
            throw new MandatoryWhere();
        }
        Connection conn = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        try {
            conn = getConnection();
            if (transaction) {
                conn.setAutoCommit(false);
            }
            if (query.getEntity() != null) query.getEntity().beforeDelete(this);
            String sqlquery = deleteQueryToExpression(query);
            query.setSentQuery(sqlquery);
            int rv = RSReader.execute(sqlquery, conn, this);
            if (query.getEntity() != null) query.getEntity().afterDelete(this);
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
            return rv;
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
            throw new DaobabSQLException(e);
        } finally {
            finalConnectionClose(conn, transaction);
        }
    }


    default <E extends Entity> int update(QueryUpdate<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.UPDATE);
        if (query.getWhereWrapper() == null) {
            throw new MandatoryWhere();
        }
        Connection conn = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        try {
            conn = getConnection();
            if (transaction) {
                conn.setAutoCommit(false);
            }
            if (query.getEntity() != null) query.getEntity().beforeUpdate(this);
            QuerySpecialParameters q = toQueryUpdateExpression(query);
            query.setSentQuery(q.getQuery().toString());
            int rv = RSReader.executeUpdate(q, conn);
            if (query.getEntity() != null) query.getEntity().afterUpdate(this);
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
            return rv;
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
        } finally {
            finalConnectionClose(conn, transaction);
        }
    }

    @SuppressWarnings("java:S3740")
    default <E extends Entity> E insert(QueryInsert<E> query, boolean transaction) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.INSERT);
        Connection conn = null;
        PrimaryKey pk = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        try {
            conn = getConnection();
            if (transaction) {
                conn.setAutoCommit(false);
            }
            if (query.isPkResolved()) {
                pk = (PrimaryKey) query.getEntity();
                if (pk.getIdGeneratorType().equals(IdGeneratorType.SEQUENCE)) {
                    query.setPkNo(getNextId(conn, query.getSequenceName(), pk.colID().getFieldClass()));
                }
            }

//            query.getEntity().beforeInsert(this);
            QuerySpecialParameters insertQueryParameters = insertQueryToExpression(query);


            query.setSentQuery(insertQueryParameters.getQuery().toString());

            if (pk != null && pk.getIdGeneratorType().equals(IdGeneratorType.SEQUENCE)) {
                RSReader.executeUpdate(insertQueryParameters, conn);
            } else {
                query.setPkNo(RSReader.executeInsert(insertQueryParameters, conn, this, query.isPkResolved() ? ((PrimaryKey) query.getEntity()).colID() : null));
            }

            if (query.isPkResolved()) {
                pk.setId(query.getPkNo());
            }

            query.getEntity().afterInsert(this);
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
            return query.getEntity();
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
        } finally {
            finalConnectionClose(conn, transaction);
        }
    }

    default void finalConnectionClose(Connection conn, boolean transaction) {
        if (conn != null) {
            try {
                if (transaction) {
                    conn.commit();
                }

                if (!conn.isClosed()) conn.close();

            } catch (SQLException e) {
                try {
                    if (transaction) conn.rollback();
                    if (!conn.isClosed()) conn.close();
                } catch (SQLException ext) {
                    //todo: specify non transactional close exception
                    throw new DaobabSQLException("Rollback problem.", ext);
                }
                throw new DaobabSQLException(e);
            }
        }
    }


    @Override
    default <E extends Entity, F> F readField(QueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (s, conn) -> {
            if (s.getFields() == null || s.getFields().isEmpty()) {
                throw new DaobabException("No column to return specified.");
            }

            PreparedStatement stmt = null;
            if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
            try {
                String sqlQuery = toSqlQuery(s);
                query.setSentQuery(sqlQuery);
                stmt = conn.prepareStatement(sqlQuery);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
                    return (F) RSReader.readSingleColumnValue(rs, 1, s.getFields().get(0).getColumn());
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } finally {
                RSReader.closeStatement(stmt, this);

            }
        });
    }

    @Override
    default <E extends Entity, F> List<F> readFieldList(QueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (s, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
            if (s.getFields() == null || s.getFields().isEmpty()) {
                throw new DaobabException("No column to return specified.");
            }

            PreparedStatement stmt = null;
            List<F> rv = new ArrayList<>();
            try {
                String sqlQuery = toSqlQuery(s);
                query.setSentQuery(sqlQuery);
                stmt = conn.prepareStatement(sqlQuery);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    rv.add((F) RSReader.readSingleColumnValue(rs, 1, s.getFields().get(0).getColumn()));
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
                return rv;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } finally {
                RSReader.closeStatement(stmt, this);
            }

        });
    }

    @Override
    default <E extends Entity> Entities<E> readEntityList(QueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (s, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
            Class<E> clazz = s.getEntityClass();
            PreparedStatement stmt = null;
            List<E> rv = new ArrayList<>();
            try {
                String sqlQuery = toSqlQuery(s);
                query.setSentQuery(sqlQuery);
                stmt = conn.prepareStatement(sqlQuery);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    E e = clazz.getDeclaredConstructor().newInstance();
                    e = RSReader.readTableRowInfo(rs, e, s.getFields());
                    e.afterSelect(this);
                    rv.add(e);
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
                return new EntityList<>(rv, query.getEntityClass());
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e1) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e1);
                throw new DaobabEntityCreationException(clazz, e1);
            } finally {
                RSReader.closeStatement(stmt, this);

            }
        });
    }

    @Override
    default <E extends Entity> E readEntity(QueryEntity<E> query) {

        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);

        return doSthOnConnection(query, (s, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
            Class<E> clazz = s.getEntityClass();
            PreparedStatement stmt = null;
            try {
                String sqlQuery = toSqlQuery(s);
                query.setSentQuery(sqlQuery);
                stmt = conn.prepareStatement(sqlQuery);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    E rv = clazz.getDeclaredConstructor().newInstance();
                    rv = RSReader.readTableRowInfo(rs, rv, s.getFields());
                    rv.afterSelect(this);
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
                    return rv;
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e1) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e1);
                throw new DaobabEntityCreationException(clazz, e1);
            } finally {
                RSReader.closeStatement(stmt, this);
            }
        });
    }

    default <T, I> T doSthOnConnection(I functionInput, BiFunction<I, Connection, T> function) {
        Connection conn = null;
        try {
            conn = getConnection();
            return function.apply(functionInput, conn);
        } finally {
            if (!(this instanceof OpenTransactionDataBaseTarget)) {
                ConnectionGateway.closeConnectionIfOpened(conn);
            }
        }
    }


    default Plate readPlate(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (s, conn) -> {
            PreparedStatement stmt = null;
            if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
            try {
                String sqlQuery = toSqlQuery(s);
                query.setSentQuery(sqlQuery);
                stmt = conn.prepareStatement(sqlQuery);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Plate plate = RSReader.readPlate(rs, s.getFields());
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
                    return plate;
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } finally {
                RSReader.closeStatement(stmt, this);
            }
        });
    }

    @Override
    default PlateBuffer readPlateList(QueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        List<TableColumn> fields = query.getFields();

        getLog().debug("Start readPlateList");
        Connection conn = null;
        PreparedStatement stmt = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        List<Plate> rv = new ArrayList<>();
        try {
            conn = this.getConnection();
            String sqlQuery = toSqlQuery(query);
            query.setSentQuery(sqlQuery);
            stmt = conn.prepareStatement(sqlQuery);

            ResultSet rs = stmt.executeQuery();
            getLog().debug(format("readPlateList executed statement: %s",sqlQuery));

            while (rs.next()) {
                rv.add(RSReader.readPlate(rs, fields));
            }

            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
            return new PlateBuffer(rv);
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
            throw new DaobabSQLException(e);
        } finally {
            RSReader.closeStatement(stmt, this);
            this.closeConnection(conn);
            getLog().debug("Finish readPlateList");
        }
    }

    default long count(Query<?, ?> query) {
        getLog().debug("Start count ");
        Connection conn=null;
        PreparedStatement stmt=null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement(toSqlQuery(query));

            ResultSet rs = stmt.executeQuery();
//            getLog().debug("count executed statement: #0");

            while (rs.next()) {
                long cnt = rs.getLong(1);
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
                return cnt;
            }
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
            return 0;
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
        } finally {
            RSReader.closeStatement(stmt, query);
            this.closeConnection(conn);
            getLog().debug("Finish count");

        }
    }


    default DaobabDataBaseMetaData getDataBaseMetaData() {
        return doSthOnConnection("", (s, c) -> {

            DaobabDataBaseMetaData rv = new DaobabDataBaseMetaData();

            try {
                rv.setDatabaseMajorVersion(ParserNumber.toString(c.getMetaData().getDatabaseMajorVersion()));
                rv.setDatabaseProductName(c.getMetaData().getDatabaseProductName());
                rv.setDriverName(c.getMetaData().getDriverName());
                rv.setDriverVersion(c.getMetaData().getDriverVersion());
                rv.setMaxConnections(c.getMetaData().getMaxConnections());

            } catch (SQLException e) {
                throw new DaobabSQLException(e);
            }
            return rv;
        });
    }


}
