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
import io.daobab.target.database.converter.*;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.converter.type.TypeConverterPKBased;
import io.daobab.target.database.converter.type.TypeConverterPKBasedList;
import io.daobab.target.database.meta.MetaData;
import io.daobab.target.database.query.*;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryEntity;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryField;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryPlate;
import io.daobab.target.database.query.frozen.FrozenQueryProvider;
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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
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
            String sqlQuery = toDeleteSqlQuery(query);
            query.setSentQuery(sqlQuery);
            int rv = getResultSetReader().execute(sqlQuery, conn, this);
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

                Column column = s.getFields().get(0).getColumn();
                DatabaseTypeConverter<?, ?> typeConverter = query.getTarget().getConverterManager().getConverter(column).orElse(null);

                if (rs.next()) {
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(query, 1);
                    return rsReader.readCell(typeConverter, rs, 1, column);
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

                DatabaseTypeConverter<?, ?> optype = query.getTarget().getConverterManager().getConverter(column).orElse(null);

                while (rs.next()) {
                    rv.add(rsReader.readCell(optype, rs, 1, column));
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

    @Override
    default <E extends Entity, F> List<F> readFieldList(FrozenDataBaseQueryField<E, F> frozenQuery, List<Object> parameters, Column<?, ?, ?> column, DatabaseTypeConverter<?, ?> typeConverter) {
        return doSthOnConnection(frozenQuery, (s, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(frozenQuery);

            ResultSetReader rsReader = getResultSetReader();
            Statement stmt = null;
            List<F> rv = new ArrayList<>();
            try {
                String sqlQuery = withParameters(frozenQuery, parameters);
                frozenQuery.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                while (rs.next()) {
                    rv.add(rsReader.readCell(typeConverter, rs, 1, column));
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozenQuery, rv.size());
                return rv;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozenQuery, e);
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

                E entityInstance = clazz.newInstance();

                List<TableColumn> columns = entityQuery.getFields();
                Column[] columnsArray = new Column[columns.size()];

                for (int i = 0; i < columns.size(); i++) {
                    columnsArray[i] = columns.get(i).getColumn();
                }
                DatabaseTypeConverter<?, ?>[] typeConvertersArr = new DatabaseTypeConverter<?, ?>[columns.size()];

                for (int i = 0; i < columns.size(); i++) {
                    DatabaseTypeConverter<?, ?> typeConverter = query.getTarget().getConverterManager().getConverter(columnsArray[i]).orElse(null);

                    if (typeConverter instanceof TypeConverterPKBased) {
                        typeConvertersArr[i] = new TypeConverterPrimaryKeyToOneCache((TypeConverterPKBased) typeConverter);
                    } else if (typeConverter instanceof TypeConverterPKBasedList) {
                        typeConvertersArr[i] = new TypeConverterPrimaryKeyToManyCache(this, (TypeConverterPKBasedList) typeConverter, entityInstance, (Entity) columnsArray[i].getInnerTypeClass().getDeclaredConstructor().newInstance());
                    } else {
                        typeConvertersArr[i] = typeConverter;
                    }


                }

                while (rs.next()) {
                    E entity = clazz.getDeclaredConstructor().newInstance();

                    for (int i = 0; i < columns.size(); i++) {
                        Column col = columnsArray[i];
                        if (typeConvertersArr[i].isEntityConverter()) {
                            rsReader.readCell((KeyableCache) typeConvertersArr[i], rs, i + 1, columnsArray[i], e -> col.setValue((EntityRelation) entity, e));
                        } else {
                            columnsArray[i].setValue((EntityRelation) entity, rsReader.readCell(typeConvertersArr[i], rs, i + 1, columnsArray[i]));
                        }
                    }

                    entity.afterSelect(this);
                    rv.add(entity);
                }

                for (int i = 0; i < columns.size(); i++) {
                    if (typeConvertersArr[i].isEntityConverter()) {
                        EntityConverter entityConverter = (EntityConverter) typeConvertersArr[i];
                        entityConverter.readEntities();
                        entityConverter.applyEntities();
                    } else if (typeConvertersArr[i].isEntityListConverter()) {
                        EntityListConverter entityConverter = (EntityListConverter) typeConvertersArr[i];
                        entityConverter.readEntities(rv);
                        entityConverter.applyEntities();
                    }


                }

                if (isStatisticCollectingEnabled()) getStatisticCollector().received(entityQuery, rv.size());
                return new EntityList<>(rv, clazz);
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                     InvocationTargetException e1) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(query, e1);
                throw new DaobabEntityCreationException(clazz, e1);
            } finally {
                rsReader.closeStatement(stmt, this);
            }
        });
    }

    @Override
    default <E extends Entity> Entities<E> readEntityList(FrozenDataBaseQueryEntity<E> frozenQuery, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConvertersArr) {
        return doSthOnConnection(frozenQuery, (frozen, conn) -> {
            if (isStatisticCollectingEnabled()) getStatisticCollector().send(frozen);
            Class<E> clazz = frozen.getOriginalQuery().getEntityClass();

            Statement stmt = null;
            List<E> rv = new ArrayList<>();
            ResultSetReader rsReader = getResultSetReader();
            try {
                String sqlQuery = withParameters(frozenQuery, parameters);
                frozen.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                List<TableColumn> columns = frozen.getOriginalQuery().getFields();
                Column[] columnsArray = new Column[columns.size()];

                for (int i = 0; i < columns.size(); i++) {
                    columnsArray[i] = columns.get(i).getColumn();
                }

                while (rs.next()) {
                    E entity = clazz.getDeclaredConstructor().newInstance();

                    for (int i = 0; i < columns.size(); i++) {
                        Column col = columnsArray[i];
                        if (typeConvertersArr[i].isEntityConverter()) {
                            rsReader.readCell((KeyableCache) typeConvertersArr[i], rs, i + 1, col, e -> col.setValue((EntityRelation) entity, e));
                        } else {
                            col.setValue((EntityRelation) entity, rsReader.readCell(typeConvertersArr[i], rs, i + 1, col));
                        }
                    }

                    entity.afterSelect(this);
                    rv.add(entity);
                }

                for (int i = 0; i < columns.size(); i++) {
                    if (typeConvertersArr[i].isEntityConverter()) {
                        EntityConverter entityConverter = (EntityConverter) typeConvertersArr[i];
                        entityConverter.readEntities();
                        entityConverter.applyEntities();
                    } else if (typeConvertersArr[i].isEntityListConverter()) {
                        EntityListConverter entityConverter = (EntityListConverter) typeConvertersArr[i];
                        entityConverter.readEntities(rv);
                        entityConverter.applyEntities();
                    }
                }

                if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozen, rv.size());
                return new EntityList<>(rv, clazz);
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozenQuery, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                     InvocationTargetException e1) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozenQuery, e1);
                throw new DaobabEntityCreationException(clazz, e1);
            } finally {
                rsReader.closeStatement(stmt, this);
            }
        });
    }

    @Override
    default <E extends Entity> E readEntity(FrozenDataBaseQueryEntity<E> frozenQuery, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConvertersArr) {

        return doSthOnConnection(frozenQuery, (frozen, conn) -> {

            if (isStatisticCollectingEnabled()) getStatisticCollector().send(frozen);
            ResultSetReader rsReader = getResultSetReader();
            Class<E> clazz = frozen.getEntityClass();
            Statement stmt = null;
            try {
                String sqlQuery = withParameters(frozenQuery, parameters);
                frozen.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                if (rs.next()) {
                    E entity = clazz.getDeclaredConstructor().newInstance();
                    rsReader.readEntity(this, rs, entity, frozen.getOriginalQuery().getFields());
                    entity.afterSelect(this);
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozen, 1);
                    return entity;
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozen, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozen, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                     InvocationTargetException e1) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozen, e1);
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
                    rsReader.readEntity(query.getTarget(), rs, entity, queryEntity.getFields());
                    entity.afterSelect(this);
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(queryEntity, 1);
                    return entity;
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(queryEntity, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(queryEntity, e);
                throw new DaobabSQLException(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                     InvocationTargetException e1) {
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


    default Plate readPlate(FrozenDataBaseQueryPlate frozenQuery, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConverters) {
        return doSthOnConnection(frozenQuery, (frozen, conn) -> {
            Statement stmt = null;
            ResultSetReader rsReader = getResultSetReader();
            if (isStatisticCollectingEnabled()) getStatisticCollector().send(frozen);

            try {
                String sqlQuery = withParameters(frozenQuery, parameters);
                frozen.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                if (rs.next()) {
                    Plate plate = rsReader.readPlate(rs, frozen.getFields(), typeConverters);
                    if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozen, 1);
                    return plate;
                }
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozen, 0);
                return null;
            } catch (SQLException e) {
                if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozen, e);
                throw new DaobabSQLException(e);
            } finally {
                rsReader.closeStatement(stmt, this);
            }
        });
    }


    default Plate readPlate(DataBaseQueryPlate query) {
        getAccessProtector().removeViolatedInfoColumns(query.getFields(), OperationType.READ);
        return doSthOnConnection(query, (queryPlate, conn) -> {
            Statement stmt = null;
            ResultSetReader rsReader = getResultSetReader();
            if (isStatisticCollectingEnabled()) getStatisticCollector().send(queryPlate);

            DatabaseTypeConverter<?, ?>[] typeConverters = new DatabaseTypeConverter<?, ?>[queryPlate.getFields().size()];

            for (int i = 0; i < queryPlate.getFields().size(); i++) {
                typeConverters[i] = queryPlate.getTarget().getConverterManager().getConverter(queryPlate.getFields().get(i).getColumn()).orElse(null);
            }

            try {
                String sqlQuery = toSqlQuery(queryPlate);
                queryPlate.setSentQuery(sqlQuery);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuery);

                if (rs.next()) {
                    Plate plate = rsReader.readPlate(rs, queryPlate.getFields(), typeConverters);
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
    default PlateBuffer readPlateList(FrozenDataBaseQueryPlate frozenQuery, List<Object> parameters, DatabaseTypeConverter<?, ?>[] typeConverters) {
        List<TableColumn> fields = frozenQuery.getFields();

        getLog().debug("Start readPlateList");
        Connection conn = null;
        Statement stmt = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(frozenQuery);
        List<Plate> rv = new ArrayList<>();
        ResultSetReader rsReader = getResultSetReader();

        try {
            conn = this.getConnection();
            String sqlQuery = withParameters(frozenQuery, parameters);
            frozenQuery.setSentQuery(sqlQuery);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            getLog().debug(format("readPlateList executed statement: %s", sqlQuery));

            while (rs.next()) {
                rv.add(rsReader.readPlate(rs, fields, typeConverters));
            }

            if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozenQuery, rv.size());
            return new PlateBuffer(rv);
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozenQuery, e);
            throw new DaobabSQLException(e);
        } finally {
            rsReader.closeStatement(stmt, this);
            this.closeConnection(conn);
            getLog().debug("Finish readPlateList");
        }
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

        Column[] columnsArray = new Column[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            columnsArray[i] = fields.get(i).getColumn();
        }
        DatabaseTypeConverter<?, ?>[] typeConvertersArr = new DatabaseTypeConverter<?, ?>[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            typeConvertersArr[i] = query.getTarget().getConverterManager().getConverter(columnsArray[i]).orElse(null);
        }

        try {
            conn = this.getConnection();
            String sqlQuery = toSqlQuery(query);
            query.setSentQuery(sqlQuery);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            getLog().debug(format("readPlateList executed statement: %s", sqlQuery));

            while (rs.next()) {
                rv.add(rsReader.readPlate(rs, fields, typeConvertersArr));
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
        Statement stmt = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(query);
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(toSqlQuery(query));

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


    default long count(FrozenQueryProvider frozenQueryProvider, List<Object> parameters) {
        getLog().debug("Start count ");
        Connection conn = null;
        Statement stmt = null;
        if (isStatisticCollectingEnabled()) getStatisticCollector().send(frozenQueryProvider);
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();

            String sqlQuery = withParameters(frozenQueryProvider, parameters);
            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                long cnt = rs.getLong(1);
                if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozenQueryProvider, 1);
                return cnt;
            }
            if (isStatisticCollectingEnabled()) getStatisticCollector().received(frozenQueryProvider, 0);
            return 0;
        } catch (SQLException e) {
            if (isStatisticCollectingEnabled()) getStatisticCollector().error(frozenQueryProvider, e);
            throw new DaobabSQLException(e);
        } finally {
            getResultSetReader().closeStatement(stmt, frozenQueryProvider);
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
        String query = toCallProcedureSqlQuery(name, in, this);

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
