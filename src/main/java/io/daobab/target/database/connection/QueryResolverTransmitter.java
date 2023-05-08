package io.daobab.target.database.connection;

import io.daobab.model.Entity;
import io.daobab.model.ProcedureParameters;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.query.DataBaseQueryInsert;
import io.daobab.target.database.query.DataBaseQueryUpdate;

/**
 * A technical interface to link some methods between SQLProducer and a target
 */
public interface QueryResolverTransmitter {

    <E extends Entity> String toDeleteSqlQuery(DataBaseQueryDelete<E> base);

    <E extends Entity> QuerySpecialParameters toInsertSqlQuery(DataBaseQueryInsert<E> base);

    <E extends Entity> QuerySpecialParameters toUpdateSqlQuery(DataBaseQueryUpdate<E> base);

    String toCallProcedureSqlQuery(String procedureName, ProcedureParameters input, QueryTarget queryTarget);
}
