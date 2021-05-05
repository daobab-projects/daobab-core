package io.daobab.target.database;

import io.daobab.model.Entity;
import io.daobab.query.QueryDelete;
import io.daobab.query.QueryInsert;
import io.daobab.query.QueryUpdate;
import io.daobab.query.base.Query;
import io.daobab.query.base.QuerySpecialParameters;

public interface QueryConsumer {

    <E extends Entity> String deleteQueryToExpression(QueryDelete<E> base);

    <E extends Entity> QuerySpecialParameters insertQueryToExpression(QueryInsert<E> base);

    <E extends Entity> E insert(QueryInsert<E> query, boolean transaction);

    <E extends Entity> String toSqlQuery(Query<E, ?> base);

    <E extends Entity> QuerySpecialParameters toQueryUpdateExpression(QueryUpdate<E> base);
}
