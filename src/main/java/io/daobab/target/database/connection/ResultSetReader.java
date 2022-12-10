package io.daobab.target.database.connection;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.*;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.converter.TypeConverterPrimaryKeyToOneCache;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;

import java.sql.*;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

public interface ResultSetReader {
    Plate readPlate(ResultSet rs, List<TableColumn> fields, DatabaseTypeConverter<?, ?>[] typeConverters) throws SQLException;

    <O extends ProcedureParameters> O readProcedure(ResultSet rs, O out) throws SQLException;

    <E extends Entity> E readEntity(QueryTarget target, ResultSet rs, E entity, List<TableColumn> columns);

    Timestamp toTimeZone(Timestamp timestamp, TimeZone timeZone);

    <F> F readCellEasy(ResultSet rs, int colNo, Class<F> valueClazz) throws SQLException;

    @SuppressWarnings({"rawtypes"})
    <F> F readCell(DatabaseTypeConverter<?, ?> cellTypeConverter, ResultSet rs, int colNo, Column column);


    @SuppressWarnings({"rawtypes"})
    <E extends Entity & PrimaryKey<E, F, ?>, F> void readCell(TypeConverterPrimaryKeyToOneCache<F, E> typeConverter, ResultSet rs, int columnIndex, Column column, Consumer<E> consumer);

    void closeStatement(Statement stmt, ILoggerBean loggerBean);

    int execute(String query, Connection conn, ILoggerBean loggerBean);

    int executeUpdate(QuerySpecialParameters insertQueryParameters, Connection conn);

    @SuppressWarnings("rawtypes")
    <E extends Entity, F, R extends EntityRelation> F executeInsert(QuerySpecialParameters insertQueryParameters, Connection conn, ILoggerBean loggerBean, Column<E, F, R> pk);

    <F> F getSequenceNextId(Connection conn, String sequenceName, Class<F> fieldClazz);
}
