package io.daobab.target.database.connection;

import io.daobab.error.DaobabException;
import io.daobab.error.DaobabSQLException;
import io.daobab.error.NoSequenceException;
import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.*;
import io.daobab.query.base.QuerySpecialParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class JDBCResultSetReader implements ResultSetReader, ILoggerBean {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("unchecked")
    @Override
    public Plate readPlate(ResultSet rs, List<TableColumn> columns) throws SQLException {
        Plate plate = new Plate(columns);
        for (int i = 0; i < columns.size(); i++) {
            TableColumn tableColumn = columns.get(i);
            plate.setValue(tableColumn, rs.getObject(i + 1, tableColumn.getColumn().getFieldClass()));
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
    public <E extends Entity> E readEntity(ResultSet rs, E entity, List<TableColumn> columns) {
        for (int i = 0; i < columns.size(); i++) {
            Column<E, ?, EntityRelation> col = columns.get(i).getColumn();
            col.setValue((EntityRelation) entity, readCell(rs, i + 1, col.getFieldClass()));
        }
        return entity;
    }

    @Override
    public Timestamp toTimeZone(Timestamp timestamp, TimeZone timeZone) {
        if (timestamp == null) return null;
        return new Timestamp(timestamp.getTime() - timeZone.getOffset(timestamp.getTime()));
    }

    @Override
    public <F> F readCellEasy(ResultSet rs, int colNo, Class<F> valueClazz) throws SQLException {
        return rs.getObject(colNo, valueClazz);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F> F readCell(ResultSet rs, int colNo, Class columnType) {
        try {
            if (Timestamp.class.equals(columnType)) {
                return (F) rs.getTimestamp(colNo, Calendar.getInstance(TimeZone.getDefault()));
            } else if (columnType.isEnum()) {
                String rd = rs.getString(colNo);
                if (rd == null) return null;
                return (F) Enum.valueOf(columnType, rd);
            } else if (columnType.equals(BigInteger.class)) {
                String rd = rs.getString(colNo);
                if (rd == null) return null;
                return (F) new BigInteger(rd);
            } else if (columnType.equals(Object.class)) {
                return (F) rs.getString(colNo);
            }
            return (F) rs.getObject(colNo, columnType);
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
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("select " + sequenceName + ".nextval from dual");
            ResultSet rs = stmt.executeQuery();
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
