package io.daobab.query.base;

import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.statement.condition.Having;
import io.daobab.statement.condition.Limit;
import io.daobab.statement.condition.Order;
import io.daobab.statement.condition.SetOperator;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.base.Where;
import io.daobab.target.QueryHandler;
import io.daobab.target.Target;

import java.util.List;
import java.util.Map;


/**
 * The full read model of a query: its selected fields, the where and having clauses, the joins, the group by,
 * the order by, the limit and the set operators, together with the owning {@link Target} and the remote
 * (de)serialization. The concrete query types (entity / field / plate) implement it, while the fluent building
 * comes from the {@link QueryWhere}, {@link QueryJoin}, {@link QueryHaving}, {@link QueryOrder},
 * {@link QueryLimit}, {@link QueryGroupBy} and {@link QuerySetOperator} mix-ins.
 *
 * @param <E> the queried entity
 * @param <T> the target type (a {@link Target} that is also a {@link QueryHandler})
 * @param <Q> the concrete query type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface Query<E extends Entity, T extends Target & QueryHandler, Q extends Query> extends ILoggerBean, StatisticQuery {

    /**
     * The set operators ({@code UNION}, ...) combining this query with others.
     */
    List<SetOperator> getSetOperatorList();

    /** Whether the query has any join. */
    boolean isJoin();

    /** Whether the query groups its result. */
    boolean isGroupBy();

    /** The columns the query groups by. */
    List<Column<?, ?, ?>> getGroupBy();

    /** The alias the query groups by, when grouping by an alias. */
    String getGroupByAlias();

    /** The where clause, or {@code null} when there is none. */
    Where getWhereWrapper();

    /** Sets the where clause. */
    void setWhereWrapper(Where whereWrapper);

    /** The selected columns. */
    List<TableColumn> getFields();

    /** The storage of the table/column identifiers (aliases) used while rendering the SQL. */
    IdentifierStorage getIdentifierStorage();

    /** Sets the where clause and returns this query for chaining. */
    Q where(Where whereWrapper);

    /** Sets the query identifier (alias). */
    void setIdentifier(String identifier);

    /** The target this query runs against. */
    Target getTarget();

    /** Sets the having clause and returns this query for chaining. */
    Q having(Having having);

    /** The order by, or {@code null} when there is none. */
    Order getOrderBy();

    /** Sets the order by and returns this query for chaining. */
    Q orderBy(Order orderBy);

    /** The limit, or {@code null} when there is none. */
    Limit getLimit();

    /** Whether this query logs its SQL at INFO regardless of the target's global setting. */
    boolean isLogQueryEnabled();

    /** The name of the queried entity. */
    String getEntityName();

    /** The class of the queried entity. */
    Class<E> getEntityClass();

    /** The having clause, or {@code null} when there is none. */
    Having getHavingWrapper();

    /** The joins of the query. */
    List<JoinWrapper> getJoins();

    /** Replaces the joins of the query. */
    void setJoins(List<JoinWrapper> joins);

    /**
     * Serializes this query to a transport map.
     *
     * @param singleResult whether a single result is expected
     * @return the transport representation
     */
    Map<String, Object> toRemote(boolean singleResult);

    /**
     * Rebuilds this query from its transport map, resolving columns and entities against the given target.
     *
     * @param target the target the query is resolved against
     * @param rv     the transport representation
     */
    void fromRemote(T target, Map<String, Object> rv);

    /** Records the SQL actually sent to the target. */
    void setSentQuery(String sentQuery);

    /** Sets the queried entity (the {@code FROM}) and returns this query for chaining. */
    <E1 extends Entity> Q from(E1 entity);
}
