package io.daobab.target.database;

import io.daobab.model.Entity;
import io.daobab.model.ProcedureParameters;
import io.daobab.query.QueryDelete;
import io.daobab.query.QueryInsert;
import io.daobab.query.QueryUpdate;
import io.daobab.query.base.Query;
import io.daobab.query.base.QuerySpecialParameters;

public interface QueryConsumer {

    <E extends Entity> String toDeleteSqlQuery(QueryDelete<E> base);

    <E extends Entity> QuerySpecialParameters toInsertSqlQuery(QueryInsert<E> base);

    <E extends Entity> String toSqlQuery(Query<E, ?> base);

    <E extends Entity> QuerySpecialParameters toUpdateSqlQuery(QueryUpdate<E> base);

    String toCallProcedureSqlQuery(String procedureName, ProcedureParameters input);
}
