package io.daobab.target.database.connection;

import io.daobab.error.DaobabSQLException;
import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.*;
import io.daobab.query.base.QuerySpecialParameters;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface RSReader {

    static Plate readPlate(ResultSet rs, List<TableColumn> col) throws SQLException {
        Plate plate = new Plate(col);
        for (int i = 0; i < col.size(); i++) {
            putColumnValueInPlate(rs, i + 1, plate, col.get(i));
        }
        return plate;
    }

    static <O extends ProcedureParameters> O readProcedure(ResultSet rs, O out) throws SQLException {
        for (int i = 1; i < out.getColumns().size() + 1; i++) { //starting from 1 not from 0
            out.setValue(i, rs.getObject(i));
        }
        return out;
    }

    @SuppressWarnings("unchecked")
    static void putColumnValueInPlate(ResultSet rs, int colNo, Plate e, TableColumn col) throws SQLException {
        e.setValue(col, rs.getObject(colNo, col.getColumn().getFieldClass()));
    }

    @SuppressWarnings({"unchecked"})
    static <E extends Entity> E readTableRowInfo(ResultSet rs, E e, List<TableColumn> col) {
        for (int i = 0; i < col.size(); i++) {
            putColumnValueIntoEntity(rs, i + 1, e, col.get(i).getColumn());
        }
        return e;
    }

    static Timestamp toTimeZone(Timestamp timestamp, TimeZone timeZone) {
        if (timestamp == null) return null;
        return new Timestamp(timestamp.getTime() - timeZone.getOffset(timestamp.getTime()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <E extends Entity, F, R extends EntityRelation> void putColumnValueIntoEntity(ResultSet rs, int colNo, E e, Column<E, F, R> col) {
        Class<F> columnType = col.getFieldClass();
        col.setValue((R) e, readSingleColumn(rs, colNo, columnType));
    }

    static <F> F readColumnValue(ResultSet rs, int colNo, Class<F> valueClazz) throws SQLException {
        return rs.getObject(colNo, valueClazz);
    }

    static <F> F readSingleColumnValue(ResultSet rs, int colNo, Column<?, F, ?> col) {
        return readSingleColumn(rs, colNo, col.getFieldClass());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <F> F readSingleColumn(ResultSet rs, int colNo, Class columnType) {
        try {
            if (Timestamp.class.equals(columnType)) {
                return (F) rs.getTimestamp(colNo, Calendar.getInstance(TimeZone.getDefault()));
            } else if (columnType.isEnum()) {
                String rd = rs.getObject(colNo, String.class);
                if (rd == null) return null;
                return (F) Enum.valueOf(columnType, rd);
            }
            return (F) rs.getObject(colNo, columnType);
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }
    }

    static void closeStatement(Statement stmt, ILoggerBean loggerBean) {
        try {
            if (stmt == null) {
                loggerBean.getLog().debug("Cannot close null statement");
            } else {
                if (stmt.isClosed()) {
                    loggerBean.getLog().debug("Cannot close already closed statement");
                } else {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }
    }

    static int execute(String query, Connection conn, ILoggerBean loggerBean) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(query);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            RSReader.closeStatement(stmt, loggerBean);
        }
    }

    static int executeUpdate(QuerySpecialParameters insertQueryParameters, Connection conn) {

        try (PreparedStatement stmt = conn.prepareStatement(insertQueryParameters.getQuery().toString())) {
            for (int i = 1; i < insertQueryParameters.getCounter(); i++) {
                stmt.setBinaryStream(i, new ByteArrayInputStream((byte[]) insertQueryParameters.getSpecialParameters().get(i)));
            }
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    static <E extends Entity, F, R extends EntityRelation> F executeInsert(QuerySpecialParameters insertQueryParameters, Connection conn, ILoggerBean loggerBean, Column<E, F, R> pk) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(insertQueryParameters.getQuery().toString(), Statement.RETURN_GENERATED_KEYS);

            for (int i = 1; i < insertQueryParameters.getCounter(); i++) {
                stmt.setBinaryStream(i, new ByteArrayInputStream((byte[]) insertQueryParameters.getSpecialParameters().get(i)));
            }

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next() && pk != null) {
                return readColumnValue(rs, 1, pk.getFieldClass());
            }
            return null;

        } catch (SQLException e) {
            throw new DaobabSQLException(e);
        } finally {
            RSReader.closeStatement(stmt, loggerBean);
        }
    }


}
