package io.daobab.query.base;

import io.daobab.error.NullParameter;
import io.daobab.model.*;
import io.daobab.statement.join.JoinTracker;
import io.daobab.statement.join.JoinType;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;
import io.daobab.target.Target;
import io.daobab.target.database.QueryTarget;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code JOIN} fragment of a query. Joins can be spelled out on explicit columns, derived from a primary
 * key, added conditionally with flags, or inferred by the {@link JoinTracker} ({@link #smartJoins()},
 * {@link #joinRoute}, {@link #joinThrough}). The default {@link JoinType} is {@code INNER}. For example:
 * <pre>{@code
 * db.select(tabEmployee)
 *   .join(tabDepartment, tabEmployee.colDepartmentId())
 *   .whereEqual(tabDepartment.colName(), "SALES")
 *   .findMany();
 *
 * // join on the joined table's primary key
 * db.select(tabRental).joinPk(tabInventory).findMany();
 * }</pre>
 *
 * @param <Q> the concrete query type, returned for chaining
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryJoin<Q extends Query> {

    /**
     * The joins of the query.
     */
    List<JoinWrapper> getJoins();

    /** Replaces the joins of the query. */
    void setJoins(List<JoinWrapper> joins);

    /** The target this query runs against. */
    Target getTarget();

    /** The name of the queried entity. */
    String getEntityName();

    /** Lets Daobab infer the joins from the columns and entities the query references. */
    Q smartJoins();

    /** Inner-joins each foreign-key table to {@code tablePK} on its id. */
    default <E extends Entity & PrimaryKey, R extends RelatedTo> Q joinByPk(E tablePK, R... tablesFK) {
        if (tablePK == null) throw new NullParameter("tablesFK");
        for (R pk : tablesFK) {
            getJoins().add(new JoinWrapper(JoinType.INNER, pk, tablePK.colID()));
        }
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the column only when the flag is set. */
    default <E extends Entity> Q joinByFlag1(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn) {
        if (enableTFK1) join(JoinType.INNER, joinedTable, joinByColumn);
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the column, with an extra where condition, only when the flag is set. */
    default <E extends Entity> Q joinByFlag1(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn, Where where) {
        if (enableTFK1) join(JoinType.INNER, joinedTable, joinByColumn, where);
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the flagged columns (those whose flag is set). */
    default <E extends Entity> Q joinByFlag2(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2) {
        List<Column<?, ?, ?>> columns = new ArrayList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (!columns.isEmpty()) join(JoinType.INNER, joinedTable, columns);
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the flagged columns, with an extra where condition. */
    default <E extends Entity> Q joinByFlag2(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, Where where) {
        List<Column<?, ?, ?>> columns = new ArrayList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (!columns.isEmpty()) {
            join(JoinType.INNER, joinedTable, columns, where);
        }
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the flagged columns (up to three). */
    default <E extends Entity> Q joinByFlag3(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3) {
        List<Column<?, ?, ?>> columns = new ArrayList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (!columns.isEmpty()) join(JoinType.INNER, joinedTable, columns);
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the flagged columns (up to three), with an extra where condition. */
    default <E extends Entity> Q joinByFlag3(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3, Where where) {
        List<Column<?, ?, ?>> columns = new ArrayList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (!columns.isEmpty()) {
            join(JoinType.INNER, joinedTable, columns, where);
        }
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the flagged columns (up to four). */
    default <E extends Entity> Q joinByFlag4(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3, boolean enableTFK4, Column<?, ?, ?> joinByColumn4) {
        List<Column<?, ?, ?>> columns = new ArrayList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (enableTFK4) columns.add(joinByColumn4);
        if (!columns.isEmpty()) join(JoinType.INNER, joinedTable, columns);
        return (Q) this;
    }

    /** Inner-joins {@code joinedTable} on the flagged columns (up to four), with an extra where condition. */
    default <E extends Entity> Q joinByFlag4(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3, boolean enableTFK4, Column<?, ?, ?> joinByColumn4, Where where) {
        List<Column<?, ?, ?>> columns = new ArrayList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (enableTFK4) columns.add(joinByColumn4);
        if (!columns.isEmpty()) {
            join(JoinType.INNER, joinedTable, columns, where);
        }
        return (Q) this;
    }

    /** Inner-joins the target tables, routing through the given intermediate tables. */
    default <R extends RelatedTo> Q joinThrough(QueryTarget target, Set<String> totables, R... throughTables) {
        return joinThrough(target, JoinType.INNER, totables, throughTables);
    }

    /** Inner-joins the given tables to {@code queryEntity}, letting the tracker find the route. */
    default <E extends Entity, R extends RelatedTo> Q joinRoute(QueryTarget target, E queryEntity, R... joinedTables) {
        return joinRoute(target, JoinType.INNER, queryEntity, joinedTables);
    }

    /** Inner-joins {@code joinedTable} on {@code one = two}. */
    default <E extends Entity> Q join(E joinedTable, Column<?, ?, ?> one, Column<?, ?, ?> two) {
        return join(JoinType.INNER, one, two, false);
    }

    /** Joins {@code joinedTable} on {@code one = two} with the given join type. */
    default <E extends Entity> Q join(JoinType type, E joinedTable, Column<?, ?, ?> one, Column<?, ?, ?> two) {
        return join(type, one, two, false);
    }

    /** Inner-joins {@code joinedTable} on the given columns. */
    default <E extends Entity> Q join(E joinedTable, Column<?, ?, ?>... joinByColumn) {
        return join(JoinType.INNER, joinedTable, joinByColumn);
    }

    /** Inner-joins {@code joinedTable} on a composite key. */
    default <E extends Entity, K extends Composite> Q join(E joinedTable, CompositeColumns<K> compositeColumns) {
        return join(JoinType.INNER, joinedTable, compositeColumns.getFirst().getColumn(), new WhereAnd().equal(compositeColumns, (K) joinedTable));
    }

    /** Inner-joins {@code joinedTable} on the column, with an extra where condition. */
    default <E extends Entity> Q join(E joinedTable, Column<?, ?, ?> onColumn, Where where) {
        return join(JoinType.INNER, joinedTable, onColumn, where);
    }

    /** Inner-joins each table on its own primary key. */
    default Q joinPk(PrimaryKey... joinedTable) {
        if (joinedTable == null) return (Q) this;
        Q rv = (Q) this;
        for (PrimaryKey pk : joinedTable) {
            rv = join(JoinType.INNER, (Entity) pk, pk.colID());
        }
        return rv;
    }

    /** Joins {@code joinedTable} on its own primary key with the given join type. */
    default <E extends Entity & PrimaryKey> Q joinPk(JoinType type, E joinedTable) {
        return join(type, joinedTable, joinedTable.colID());
    }

    /** Joins {@code joinedTable} on its own primary key with the given join type and an extra where condition. */
    default <E extends Entity & PrimaryKey> Q joinPk(JoinType type, E joinedTable, Where where) {
        return join(type, joinedTable, joinedTable.colID(), where);
    }

    /** Inner-joins {@code joinedTable} with an explicit where condition (no {@code ON} columns). */
    default <E extends Entity> Q join(E joinedTable, Where where) {
        return join(JoinType.INNER, joinedTable, where);
    }

    /** Inner-joins {@code joinedTable} on a {@link JoinOn} predicate, with an extra where condition. */
    default <E extends Entity> Q join(E joinedTable, JoinOn<E, ?, ?> on, Where where) {
        return join(JoinType.INNER, joinedTable, on, where);
    }

    /** Inner-joins {@code joinedTable} on a {@link JoinOn} predicate. */
    default <E extends Entity> Q join(E joinedTable, JoinOn<E, ?, ?> on) {
        return join(JoinType.INNER, joinedTable, on);
    }

    /** Inner-joins {@code joinedTable} on the given columns, with an extra where condition. */
    default <E extends Entity> Q join(E joinedTable, Where where, Column<?, ?, ?>... joinByColumn) {
        return join(JoinType.INNER, joinedTable, where, joinByColumn);
    }

    /** Inner-joins a related table with an explicit where condition. */
    default <R extends RelatedTo> Q joinWhere(R joinedTable, Where where) {
        return joinWhere(JoinType.INNER, joinedTable, where);
    }

    /** Joins each foreign-key table to {@code tablePK} on its id with the given join type. */
    default <E extends Entity & PrimaryKey, R extends RelatedTo> Q joinByPk(JoinType type, E tablePK, R... tablesFK) {
        if (tablePK == null) throw new NullParameter("tablesFK");
        for (R pk : tablesFK) {
            getJoins().add(new JoinWrapper(type, pk, tablePK.colID()));
        }
        return (Q) this;
    }

    /** Joins the target tables routing through the given intermediate tables, with the given join type. */
    default <R extends RelatedTo> Q joinThrough(QueryTarget target, JoinType type, Set<String> totables, R... throughTables) {
        if (throughTables == null) return (Q) this;
        Set<String> src = new HashSet<>();
        src.add(getEntityName());
        List<String> tables = Arrays.stream(throughTables).map(e -> target.getEntityName(e.entityClass())).collect(Collectors.toCollection(LinkedList::new));
        setJoins(JoinTracker.calculateThrough(getTarget(), getTarget().getTables(), src, totables, getJoins(), tables));
        return (Q) this;
    }

    /** Joins the given tables to {@code queryEntity} with the given join type, letting the tracker find the route. */
    default <E extends Entity, R extends RelatedTo> Q joinRoute(QueryTarget target, JoinType type, E queryEntity, R... joinedTables) {
        if (queryEntity == null || joinedTables == null) return (Q) this;
        Set<String> src = new HashSet<>();
        src.add(target.getEntityName(queryEntity.entityClass()));
        Set<String> tables = Arrays.stream(joinedTables).map(e -> target.getEntityName(e.entityClass())).collect(Collectors.toSet());
        setJoins(JoinTracker.calculateJoins(getTarget(), getTarget().getTables(), src, tables, getJoins()));
        return (Q) this;
    }

    /** Joins on {@code one = two} with the given join type (the low-level primitive the other overloads delegate to). */
    default <E extends Entity, F> Q join(JoinType type, Column<?, ?, ?> one, Column<?, ?, ?> two, boolean mark) {
        getJoins().add(new JoinWrapper(type, one, two, false));
        return (Q) this;
    }

    /** Joins {@code joinedTable} on the columns only when {@code enabled}. */
    default <E extends Entity> Q joinIfTrue(boolean enabled, JoinType type, E joinedTable, Column<?, ?, ?>... joinByColumn) {
        if (enabled) {
            return join(type, joinedTable, joinByColumn);
        }
        return (Q) this;
    }

    /** Joins {@code joinedTable} on the given columns with the given join type (multiple columns are AND-ed). */
    default <E extends Entity> Q join(JoinType type, E joinedTable, Column<?, ?, ?>... joinByColumn) {
        if (joinByColumn.length == 1) {
            getJoins().add(new JoinWrapper(type, joinedTable, joinByColumn[0]));
        } else {

            WhereAnd whr = new WhereAnd();
            for (Column<?, ?, ?> c : joinByColumn) {
                whr.equalColumn((Column<?, Object, ?>) c, (Column<?, Object, ?>) c.transformTo(joinedTable));
            }

            getJoins().add(new JoinWrapper<>(type, joinedTable, whr));
        }
        return (Q) this;
    }

    /** Joins {@code joinedTable} on the given columns with the given join type (multiple columns are AND-ed). */
    default <E extends Entity> Q join(JoinType type, E joinedTable, List<Column<?, ?, ?>> joinByColumns) {
        if (joinByColumns.size() == 1) {
            getJoins().add(new JoinWrapper(type, joinedTable, joinByColumns.getFirst()));
        } else {

            WhereAnd whr = new WhereAnd();
            for (Column<?, ?, ?> c : joinByColumns) {
                whr.equalColumn((Column<?, Object, ?>) c, (Column<?, Object, ?>) c.transformTo(joinedTable));
            }

            getJoins().add(new JoinWrapper(type, joinedTable, whr));
        }

        return (Q) this;
    }

    /** Joins {@code joinedTable} on the columns, with an extra where condition, only when {@code enabled}. */
    default <E extends Entity> Q joinIfTrue(boolean enabled, JoinType type, E joinedTable, List<Column<?, ?, ?>> joinByColumns, Where where) {
        if (enabled) {
            return join(type, joinedTable, joinByColumns, where);
        }
        return (Q) this;
    }

    /** Joins {@code joinedTable} on the given columns (AND-ed) plus an extra where condition. */
    default <E extends Entity> Q join(JoinType type, E joinedTable, List<Column<?, ?, ?>> joinByColumns, Where where) {
        if (joinByColumns.size() == 1) {
            getJoins().add(new JoinWrapper(type, joinedTable, joinByColumns.getFirst()));
        } else {

            WhereAnd whr = new WhereAnd();
            for (Column<?, ?, ?> c : joinByColumns) {
                whr.equalColumn((Column<?, Object, ?>) c, (Column<?, Object, ?>) c.transformTo(joinedTable));
            }
            whr.and(where);

            getJoins().add(new JoinWrapper(type, joinedTable, whr));
        }

        return (Q) this;
    }

    /** Joins {@code joinedTable} on {@code onColumn} with an explicit where condition. */
    default <E extends Entity> Q join(JoinType type, E joinedTable, Column<?, ?, ?> onColumn, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, onColumn, where));
        return (Q) this;
    }

    /** Joins {@code joinedTable} on {@code onColumn} with an explicit where condition, only when {@code enabled}. */
    default <E extends Entity> Q joinIfTrue(boolean enabled, JoinType type, E joinedTable, Column<?, ?, ?> onColumn, Where where) {
        if (enabled) {
            getJoins().add(new JoinWrapper(type, joinedTable, onColumn, where));
        }
        return (Q) this;
    }

    /** Joins {@code joinedTable} with an explicit where condition (no {@code ON} columns). */
    default <E extends Entity> Q join(JoinType type, E joinedTable, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, where));
        return (Q) this;
    }

    /** Joins {@code joinedTable} on a {@link JoinOn} predicate with an extra where condition. */
    default <E extends Entity> Q join(JoinType type, E joinedTable, JoinOn<E, ?, ?> on, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, on.getRight(), where));
        return (Q) this;
    }


    /** Joins {@code joinedTable} on a {@link JoinOn} predicate. */
    default <E extends Entity> Q join(JoinType type, E joinedTable, JoinOn<E, ?, ?> on) {
        getJoins().add(new JoinWrapper(type, joinedTable, on.getRight()));
        return (Q) this;
    }

    /** Joins {@code joinedTable} on the given columns (AND-ed) plus an extra where condition. */
    default <E extends Entity> Q join(JoinType type, E joinedTable, Where where, Column<?, ?, ?>... joinByColumn) {
        if (joinByColumn.length == 1) {
            getJoins().add(new JoinWrapper(type, joinedTable, joinByColumn[0], where));
        } else {

            WhereAnd whr = new WhereAnd();
            for (Column<?, ?, ?> c : joinByColumn) {
                whr.equalColumn((Column<?, Object, ?>) c, (Column<?, Object, ?>) c.transformTo(joinedTable));
            }
            if (where != null && !where.isEmpty()) whr.and(where);

            getJoins().add(new JoinWrapper(type, joinedTable, whr));
        }

        return (Q) this;
    }

    /** Joins a related table with an explicit where condition and the given join type. */
    default <R extends RelatedTo> Q joinWhere(JoinType type, R joinedTable, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, where));
        return (Q) this;
    }


}
