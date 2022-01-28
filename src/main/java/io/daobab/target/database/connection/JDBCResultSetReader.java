package io.daobab.target.database.connection;

import io.daobab.error.DaobabException;
import io.daobab.error.DaobabSQLException;
import io.daobab.error.NoSequenceException;
import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.*;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.converter.KeyableCache;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JDBCResultSetReader implements ResultSetReader, ILoggerBean {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Plate readPlate(ResultSet rs, List<TableColumn> fields, DatabaseTypeConverter<?, ?>[] typeConverters) throws SQLException {
        Plate plate = new Plate(fields);
        for (int i = 0; i < fields.size(); i++) {
            TableColumn tableColumn = fields.get(i);
            plate.setValue(tableColumn, readCell(typeConverters[i], rs, i + 1, tableColumn.getColumn()));
        }
        return plate;
    }

    @Override
    public <O extends ProcedureParameters> O readProcedure(ResultSet rs, O out) throws SQLException {
        for (int i = 1; i < out.getColumns().size() + 1; i++) { //starting from 1 not from 0
            out.setValue(i, rs.getObject(i));
        }
        return out;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity> E readEntity(QueryTarget target, ResultSet rs, E entity, List<TableColumn> columns) {

        Column[] columnsArr = new Column[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnsArr[i] = columns.get(i).getColumn();
        }
        DatabaseTypeConverter<?, ?>[] typeConvertersArr = new DatabaseTypeConverter[columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            typeConvertersArr[i] = target.getConverterManager().getConverter(columnsArr[i]).orElse(null);
        }

        for (int i = 0; i < columns.size(); i++) {
            columnsArr[i].setValue((EntityRelation) entity, readCell(typeConvertersArr[i], rs, i + 1, columnsArr[i]));
        }
        return entity;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity> E readEntity(ResultSet rs, E entity, Column[] columnsArr, DatabaseTypeConverter<?, ?>[] typeConverters) {
        for (int i = 0; i < columnsArr.length; i++) {
            columnsArr[i].setValue((EntityRelation) entity, readCell(typeConverters[i], rs, i + 1, columnsArr[i]));
        }
        return entity;
    }

    @Override
    public Timestamp toTimeZone(Timestamp timestamp, TimeZone timeZone) {
        if (timestamp == null) return null;
        return new Timestamp(timestamp.getTime() - timeZone.getOffset(timestamp.getTime()));
    }

    @Override
    public <F> F readCellEasy(ResultSet rs, int columnIndex, Class valueClazz) throws SQLException {
        return rs.getObject(columnIndex, (Class<F>) valueClazz);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F> F readCell(DatabaseTypeConverter<?, ?> typeConverter, ResultSet rs, int columnIndex, Column column) {
        Class columnType = column.getFieldClass();
        try {
            if (typeConverter != null) {
                try {
                    return (F) typeConverter.readAndConvert(rs, columnIndex);
                } catch (ClassCastException e) {
                    throw new DaobabException("Problem during reading column " + column + " using TypeConverter: " + typeConverter.getClass().getName(), e);
                }
            } else if (Timestamp.class.equals(columnType)) {
                return (F) rs.getTimestamp(columnIndex, Calendar.getInstance(TimeZone.getDefault()));
            } else if (columnType.isEnum()) {
                String rd = rs.getString(columnIndex);
                if (rd == null) return null;
                return (F) Enum.valueOf(columnType, rd);
            } else if (columnType.equals(BigInteger.class)) {
                String rd = rs.getString(columnIndex);
                if (rd == null) return null;
                return (F) new BigInteger(rd);
            } else if (columnType.equals(Object.class)) {
                return (F) rs.getString(columnIndex);
            }
            return (F) rs.getObject(columnIndex, columnType);
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void readCell(KeyableCache typeConverter, ResultSet rs, int columnIndex, Column column, Consumer consumer) {
        try {
//            F key = typeConverter.readFromResultSet(rs, columnIndex);
            typeConverter.addKey(typeConverter.readFromResultSet(rs, columnIndex), consumer);

        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }
    }

    @Override
    public void closeStatement(Statement stmt, ILoggerBean loggerBean) {
        try {
            if (stmt == null || stmt.isClosed()) {
                log.debug("Cannot close the statement");
            } else {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }
    }

    @Override
    public int execute(String query, Connection conn, ILoggerBean loggerBean) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            closeStatement(stmt, loggerBean);
        }
    }

    @Override
    public int executeUpdate(QuerySpecialParameters insertQueryParameters, Connection conn) {

        try (PreparedStatement stmt = conn.prepareStatement(insertQueryParameters.getQuery().toString())) {
            for (int i = 1; i < insertQueryParameters.getCounter(); i++) {
                stmt.setBinaryStream(i, new ByteArrayInputStream((byte[]) insertQueryParameters.getSpecialParameters().get(i)));
            }
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public <E extends Entity, F, R extends EntityRelation> F executeInsert(QuerySpecialParameters insertQueryParameters, Connection conn, ILoggerBean loggerBean, Column<E, F, R> pk) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(insertQueryParameters.getQuery().toString(), Statement.RETURN_GENERATED_KEYS);

            for (int i = 1; i < insertQueryParameters.getCounter(); i++) {
                stmt.setBinaryStream(i, new ByteArrayInputStream((byte[]) insertQueryParameters.getSpecialParameters().get(i)));
            }

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next() && pk != null) {
                return readCellEasy(rs, 1, pk.getFieldClass());
            }
            return null;

        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            closeStatement(stmt, loggerBean);
        }
    }


    @Override
    public <F> F getSequenceNextId(Connection conn, String sequenceName, Class<F> fieldClazz) {
        if (sequenceName == null || sequenceName.isEmpty()) throw new NoSequenceException();
        log.debug("Getting the sequence = {}", sequenceName);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select " + sequenceName + ".nextval from dual");
            if (rs.next()) {
                F rv = readCellEasy(rs, 1, fieldClazz);
                log.debug("Took the sequence = {} value: {}", sequenceName, rv);
                return rv;
            } else {
                throw new DaobabException("Getting the sequence '{}' value failed. Database does not return anything. Is the name of the sequence correct?", sequenceName);
            }
        } catch (SQLException e) {
            throw new DaobabSQLException("Error during generation of ID for object type = " + sequenceName, e);
        } finally {
            closeStatement(stmt, this);
        }
    }

    @Override
    public Logger getLog() {
        return log;
    }
}
