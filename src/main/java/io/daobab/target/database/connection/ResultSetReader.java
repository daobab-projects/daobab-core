package io.daobab.target.database.connection;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.*;
import io.daobab.query.base.QuerySpecialParameters;

import java.sql.*;
import java.util.List;
import java.util.TimeZone;

public interface ResultSetReader {
    Plate readPlate(ResultSet rs, List<TableColumn> col) throws SQLException;

    <O extends ProcedureParameters> O readProcedure(ResultSet rs, O out) throws SQLException;

    @SuppressWarnings("unchecked")
    void readColumnValuePutIntoPlate(ResultSet rs, int colNo, Plate e, TableColumn col) throws SQLException;

    @SuppressWarnings({"unchecked"})
    <E extends Entity> E readTableColumnsPutIntoEntity(ResultSet rs, E entity, List<TableColumn> columns);

    Timestamp toTimeZone(Timestamp timestamp, TimeZone timeZone);

    @SuppressWarnings({"unchecked", "rawtypes"})
    <E extends Entity, F, R extends EntityRelation> void readColumnValuePutIntoEntity(ResultSet rs, int colNo, E e, Column<E, F, R> col);

    <F> F readColumnValue(ResultSet rs, int colNo, Class<F> valueClazz) throws SQLException;

    <F> F readSingleColumnValue(ResultSet rs, int colNo, Column<?, F, ?> col);

    @SuppressWarnings({"unchecked", "rawtypes"})
    <F> F readSingleColumn(ResultSet rs, int colNo, Class columnType);

    void closeStatement(Statement stmt, ILoggerBean loggerBean);

    int execute(String query, Connection conn, ILoggerBean loggerBean);

    int executeUpdate(QuerySpecialParameters insertQueryParameters, Connection conn);

    @SuppressWarnings("rawtypes")
    <E extends Entity, F, R extends EntityRelation> F executeInsert(QuerySpecialParameters insertQueryParameters, Connection conn, ILoggerBean loggerBean, Column<E, F, R> pk);
}
