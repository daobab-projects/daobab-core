package io.daobab.query.base;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.statement.condition.*;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.base.Where;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.List;
import java.util.Map;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface Query<E extends Entity, T extends Target & QueryHandler, Q extends Query> extends ILoggerBean {

    List<SetOperator> getSetOperatorList();

    boolean isUnique();

    boolean isJoin();

    boolean isGroupBy();

    List<Column<?, ?, ?>> getGroupBy();

    String getGroupByAlias();

    Where getWhereWrapper();

    void setWhereWrapper(Where whereWrapper);

    List<TableColumn> getFields();

    IdentifierStorage getIdentifierStorage();

    Q where(Where whereWrapper);

    Target getTarget();

    Q having(Having having);

    Order getOrderBy();

    Q orderBy(Order orderBy);

    Count getCount();

    Limit getLimit();

    boolean isLogQueryEnabled();

    Q distinct();

    String getEntityName();

    Class<E> getEntityClass();

    Having getHavingWrapper();

    List<JoinWrapper> getJoins();

    void setJoins(List<JoinWrapper> joins);

    Map<String, Object> toRemote(boolean singleResult);

    void fromRemote(T target, Map<String, Object> rv);

    String getIdentifier();

    Q setIdentifier(String identifier);

    QueryType getQueryType();

    String getSentQuery();

    void setSentQuery(String sentQuery);

    <E1 extends Entity> Q from(E1 entity);
}
