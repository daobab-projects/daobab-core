package io.daobab.target.database;

import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.DaobabException;
import io.daobab.error.DaobabSQLException;
import io.daobab.error.MandatoryWhere;
import io.daobab.model.*;
import io.daobab.parser.ParserNumber;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.buffer.single.EntityList;
import io.daobab.target.buffer.single.PlateBuffer;
import io.daobab.target.database.connection.ConnectionGateway;
import io.daobab.target.database.connection.QueryResolverTransmitter;
import io.daobab.target.database.connection.ResultSetReader;
import io.daobab.target.database.meta.MetaData;
import io.daobab.target.database.query.*;
import io.daobab.target.database.transaction.OpenTransactionDataBaseTargetImpl;
import io.daobab.target.protection.AccessProtector;
import io.daobab.target.protection.OperationType;
import io.daobab.target.statistic.StatisticCollectorProvider;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.String.format;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface DataBaseTargetLogic extends QueryResolverTransmitter, QueryTarget, TransactionalTarget, StatisticCollectorProvider {

    default OpenTransactionDataBaseTargetImpl beginTransaction() {
        return TransactionalTarget.super.beginTransaction();
    }

    String getDataBaseProductName();

    ResultSetReader getResultSetReader();

    void setShowSql(boolean enable);

    DataSource getDataSource();

    MetaData getMetaData();

    @Override
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
            getLog().debug(format("Connection retrieved: %s", connection));
            return connection;
        } catch (SQLException e) {
            throw new DaobabSQLException("Error while retrieving connection from datasource", e);
        } finally {
            getLog().debug("Finish getConnection");
        }
    }


    default <E extends Entity> int delete(DataBaseQueryDelete<E> query, boolean transaction) {
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
            String sqlquery = toDeleteSqlQuery(query);
            query.setSentQuery(sqlquery);
            int rv = getResultSetReader().execute(sqlquery, conn, this);
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

    default <E extends Entity> int update(DataBaseQueryUpdate<E> query, boolean transaction) {
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
            QuerySpecialParameters q = toUpdateSqlQuery(query);
            query.setSentQuery(q.getQuery().toString());
            int rv = getResultSetReader().executeUpdate(q, conn);
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

    @SuppressWarnings({"java:S3740", "unchecked", "rawtypes"})
    default <E extends Entity> E insert(DataBaseQueryInsert<E> query, boolean transaction) {
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
                    query.setPkNo(getResultSetReader().getSequenceNextId(conn, query.getSequenceName(), pk.colID().getFieldClass()));
                }
            }

            QuerySpecialParameters insertQueryParameters = toInsertSqlQuery(query);

            query.setSentQuery(insertQueryParameters.getQuery().toString());

            if (pk != null && pk.getIdGeneratorType().equals(IdGeneratorType.SEQUENCE)) {
                getResultSetReader().executeUpdate(insertQueryParameters, conn);
            } else {
                query.setPkNo(getResultSetReader().executeInsert(insertQueryParameters, conn, this, query.isPkResolved() ? ((PrimaryKey) query.getEntity()).colID() : null));
            }

            if (query.isPkResolved() && pk != null) {
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

    @SuppressWarnings({"java:S3776"})
    @Override
    default <E extends Entity, F> F readField(DataBaseQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (s, conn) -> {
            if (s.getFields() == null || s.getFields().isEmpty()) {
                throw new DaobabException("Query points no column to return");
            }

            ResultSetReader rsReader = getResultSetReader();
            Statement stmt = null;
            if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
            try {
                String sqlQuery = toSqlQuery(s);
                query.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                if (rs.next()) {
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
                    return rsReader.readCell(rs, 1, s.getFields().get(0).getColumn().getFieldClass());
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } finally {
                rsReader.closeStatement(stmt, this);
            }
        });
    }

    @Override
    default <E extends Entity, F> List<F> readFieldList(DataBaseQueryField<E, F> query) {
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (s, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
            if (s.getFields() == null || s.getFields().isEmpty()) {
                throw new DaobabException("Query points no column to return");
            }

            ResultSetReader rsReader = getResultSetReader();
            Statement stmt = null;
            List<F> rv = new ArrayList<>();
            try {
                String sqlQuery = toSqlQuery(s);
                query.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);
                Column<?, ?, ?> column = s.getFields().get(0).getColumn();

                while (rs.next()) {
                    rv.add(rsReader.readCell(rs, 1, column.getFieldClass()));
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
                return rv;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } finally {
                rsReader.closeStatement(stmt, this);
            }

        });
    }

    @SuppressWarnings("java:S3776")
    @Override
    default <E extends Entity> Entities<E> readEntityList(DataBaseQueryEntity<E> query) {
        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (entityQuery, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(entityQuery);
            Class<E> clazz = entityQuery.getEntityClass();
            Statement stmt = null;
            List<E> rv = new ArrayList<>();
            ResultSetReader rsReader = getResultSetReader();
            try {
                String sqlQuery = toSqlQuery(entityQuery);
                entityQuery.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                while (rs.next()) {
                    E entity = clazz.getDeclaredConstructor().newInstance();
                    rsReader.readEntity(rs, entity, entityQuery.getFields());
                    entity.afterSelect(this);
                    rv.add(entity);
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(entityQuery, rv.size());
                return new EntityList<>(rv, clazz);
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e1) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e1);
                throw new DaobabEntityCreationException(clazz, e1);
            } finally {
                rsReader.closeStatement(stmt, this);

            }
        });
    }

    @Override
    default <E extends Entity> E readEntity(DataBaseQueryEntity<E> query) {

        getAccessProtector().validateEntityAllowedFor(query.getEntityName(), OperationType.READ);
        getAccessProtector().removeViolatedInfoColumns3(query.getFields(), OperationType.READ);

        return doSthOnConnection(query, (queryEntity, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(queryEntity);
            ResultSetReader rsReader = getResultSetReader();
            Class<E> clazz = queryEntity.getEntityClass();
            Statement stmt = null;
            try {
                String sqlQuery = toSqlQuery(queryEntity);
                queryEntity.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                if (rs.next()) {
                    E entity = clazz.getDeclaredConstructor().newInstance();
                    rsReader.readEntity(rs, entity, queryEntity.getFields());
                    entity.afterSelect(this);
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(queryEntity, 1);
                    return entity;
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(queryEntity, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(queryEntity, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e1) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(queryEntity, e1);
                throw new DaobabEntityCreationException(clazz, e1);
            } finally {
                rsReader.closeStatement(stmt, this);
            }
        });
    }

    default <T, I> T doSthOnConnection(I functionInput, BiFunction<I, Connection, T> function) {
        Connection conn = null;
        try {
            conn = getConnection();
            return function.apply(functionInput, conn);
        } finally {
            if (!(this instanceof OpenTransactionDataBaseTargetImpl)) {
                ConnectionGateway.closeConnectionIfOpened(conn);
            }
        }
    }


    default Plate readPlate(DataBaseQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (queryPlate, conn) -> {
            Statement stmt = null;
            ResultSetReader rsReader = getResultSetReader();
            if (isStatisticCollectingEnabled()) getStatisticCollector().send(queryPlate);
            try {
                String sqlQuery = toSqlQuery(queryPlate);
                queryPlate.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                if (rs.next()) {
                    Plate plate = rsReader.readPlate(rs, queryPlate.getFields());
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(queryPlate, 1);
                    return plate;
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(queryPlate, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(queryPlate, e);
                throw new DaobabSQLException(e);
            } finally {
                rsReader.closeStatement(stmt, this);
            }
        });
    }

    @Override
    default PlateBuffer readPlateList(DataBaseQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        List<TableColumn> fields = query.getFields();

        getLog().debug("Start readPlateList");
        Connection conn = null;
        Statement stmt = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        List<Plate> rv = new ArrayList<>();
        ResultSetReader rsReader = getResultSetReader();
        try {
            conn = this.getConnection();
            String sqlQuery = toSqlQuery(query);
            query.setSentQuery(sqlQuery);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            getLog().debug(format("readPlateList executed statement: %s", sqlQuery));

            while (rs.next()) {
                rv.add(rsReader.readPlate(rs, fields));
            }

            if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, rv.size());
            return new PlateBuffer(rv);
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
            throw new DaobabSQLException(e);
        } finally {
            rsReader.closeStatement(stmt, this);
            this.closeConnection(conn);
            getLog().debug("Finish readPlateList");
        }
    }

    default long count(DataBaseQueryBase<?, ?> query) {
        getLog().debug("Start count ");
        Connection conn = null;
        PreparedStatement stmt = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        try {
            conn = this.getConnection();
            stmt = conn.prepareStatement(toSqlQuery(query));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
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
            getResultSetReader().closeStatement(stmt, query);
            this.closeConnection(conn);
            getLog().debug("Finish count");
        }
    }


    default DaobabDataBaseMetaData getDataBaseMetaData() {
        return doSthOnConnection("", (nothing, c) -> {

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

    default <O extends ProcedureParameters, I extends ProcedureParameters> O callProcedure(String name, I in, O outEmpty) {
        List<Column> columns = new ArrayList<>(outEmpty.getColumns());
        getAccessProtector().removeViolatedColumns(columns, OperationType.READ);
        String query = toCallProcedureSqlQuery(name, in);

        return doSthOnConnection(query, (procedureSql, conn) -> {
            PreparedStatement stmt = null;
            String identifier = null;
            if (isStatisticCollectingEnabled()) {
                identifier = getStatisticCollector().sendProcedure(name);
            }
            try {
                stmt = conn.prepareStatement(procedureSql);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    O plate = getResultSetReader().readProcedure(rs, outEmpty);
                    if (isStatisticCollectingEnabled())
                        getStatisticCollector().receivedProcedure(name, identifier, procedureSql, outEmpty.getColumns().size());
                    return plate;
                }
                if (isStatisticCollectingEnabled())
                    getStatisticCollector().receivedProcedure(name, identifier, query, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().errorProcedure(name, identifier, query, e);
                throw new DaobabSQLException(e);
            } finally {
                getResultSetReader().closeStatement(stmt, this);
            }
        });

    }
}
