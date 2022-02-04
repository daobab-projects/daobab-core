package io.daobab.query.base;

import io.daobab.error.NullParameter;
import io.daobab.model.*;
import io.daobab.statement.join.JoinTracker;
import io.daobab.statement.join.JoinType;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;
import io.daobab.target.Target;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked", "rawtypes", "UnusedReturnValue", "unused"})
public interface QueryJoin<Q extends Query> {

    List<JoinWrapper> getJoins();

    void setJoins(List<JoinWrapper> joins);

    Target getTarget();

    String getEntityName();

    Q smartJoins();

    /**
     * Inner Join tables by their ForeignKey
     *
     * @param tablePK  - table with PrimaryKey
     * @param tablesFK - tables with relagtion ForeignKey to tablePK
     * @param <E>
     * @param <R>
     * @return
     */
    default <E extends Entity & PrimaryKey, R extends EntityRelation> Q joinByPk(E tablePK, R... tablesFK) {
        if (tablePK == null) throw new NullParameter("tablesFK");
        for (R pk : tablesFK) {
            getJoins().add(new JoinWrapper(JoinType.INNER, pk, tablePK.colID()));
        }
        return (Q) this;
    }

    default <E extends Entity> Q joinByFlag1(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn) {
        if (enableTFK1) join(JoinType.INNER, joinedTable, joinByColumn);
        return (Q) this;
    }

    default <E extends Entity> Q joinByFlag1(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn, Where where) {
        if (enableTFK1) join(JoinType.INNER, joinedTable, joinByColumn, where);
        return (Q) this;
    }


    default <E extends Entity> Q joinByFlag2(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2) {
        List<Column<?, ?, ?>> columns = new LinkedList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (!columns.isEmpty()) join(JoinType.INNER, joinedTable, columns);
        return (Q) this;
    }

    default <E extends Entity> Q joinByFlag2(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, Where where) {
        List<Column<?, ?, ?>> columns = new LinkedList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (!columns.isEmpty()) {
            join(JoinType.INNER, joinedTable, columns, where);
        }
        return (Q) this;
    }


    default <E extends Entity> Q joinByFlag3(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3) {
        List<Column<?, ?, ?>> columns = new LinkedList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (!columns.isEmpty()) join(JoinType.INNER, joinedTable, columns);
        return (Q) this;
    }

    default <E extends Entity> Q joinByFlag3(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3, Where where) {
        List<Column<?, ?, ?>> columns = new LinkedList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (!columns.isEmpty()) {
            join(JoinType.INNER, joinedTable, columns, where);
        }
        return (Q) this;
    }


    default <E extends Entity> Q joinByFlag4(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3, boolean enableTFK4, Column<?, ?, ?> joinByColumn4) {
        List<Column<?, ?, ?>> columns = new LinkedList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (enableTFK4) columns.add(joinByColumn4);
        if (!columns.isEmpty()) join(JoinType.INNER, joinedTable, columns);
        return (Q) this;
    }

    default <E extends Entity> Q joinByFlag4(E joinedTable, boolean enableTFK1, Column<?, ?, ?> joinByColumn1, boolean enableTFK2, Column<?, ?, ?> joinByColumn2, boolean enableTFK3, Column<?, ?, ?> joinByColumn3, boolean enableTFK4, Column<?, ?, ?> joinByColumn4, Where where) {
        List<Column<?, ?, ?>> columns = new LinkedList<>();
        if (enableTFK1) columns.add(joinByColumn1);
        if (enableTFK2) columns.add(joinByColumn2);
        if (enableTFK3) columns.add(joinByColumn3);
        if (enableTFK4) columns.add(joinByColumn4);
        if (!columns.isEmpty()) {
            join(JoinType.INNER, joinedTable, columns, where);
        }
        return (Q) this;
    }

    default <R extends EntityRelation> Q joinThrough(Set<String> totables, R... throughTables) {
        return joinThrough(JoinType.INNER, totables, throughTables);
    }


    default <E extends Entity, R extends EntityRelation> Q joinRoute(E queryEntity, R... joinedTables) {
        return joinRoute(JoinType.INNER, queryEntity, joinedTables);
    }

    default <E extends Entity> Q join(E joinedTable, Column<?, ?, ?> one, Column<?, ?, ?> two) {
        return join(JoinType.INNER, one, two, false);
    }


    default <E extends Entity> Q join(JoinType type, E joinedTable, Column<?, ?, ?> one, Column<?, ?, ?> two) {
        return join(type, one, two, false);
    }

    default <E extends Entity> Q join(E joinedTable, Column<?, ?, ?>... joinByColumn) {
        return join(JoinType.INNER, joinedTable, joinByColumn);
    }

    default <E extends Entity, K extends Composite> Q join(E joinedTable, CompositeColumns<K> compositeColumns) {
        return join(JoinType.INNER, joinedTable, compositeColumns.get(0).getColumn(), new WhereAnd().equal(compositeColumns, (K) joinedTable));
    }

    default <E extends Entity> Q join(E joinedTable, Column<?, ?, ?> onColumn, Where where) {
        return join(JoinType.INNER, joinedTable, onColumn, where);
    }

    default Q joinPk(PrimaryKey... joinedTable) {
        if (joinedTable == null) return (Q) this;
        Q rv = (Q) this;
        for (PrimaryKey pk : joinedTable) {
            rv = join(JoinType.INNER, (Entity) pk, pk.colID());
        }
        return rv;
    }

    default <E extends Entity & PrimaryKey> Q joinPk(JoinType type, E joinedTable) {
        return join(type, joinedTable, joinedTable.colID());
    }

    default <E extends Entity & PrimaryKey> Q joinPk(JoinType type, E joinedTable, Where where) {
        return join(type, joinedTable, joinedTable.colID(), where);
    }

    default <E extends Entity> Q join(E joinedTable, Where where) {
        return join(JoinType.INNER, joinedTable, where);
    }


    default <E extends Entity> Q join(E joinedTable, JoinOn<E, ?, ?> on, Where where) {
        return join(JoinType.INNER, joinedTable, on, where);
    }

    default <E extends Entity> Q join(E joinedTable, JoinOn<E, ?, ?> on) {
        return join(JoinType.INNER, joinedTable, on);
    }

    default <E extends Entity> Q join(E joinedTable, Where where, Column<?, ?, ?>... joinByColumn) {
        return join(JoinType.INNER, joinedTable, where, joinByColumn);
    }


    default <R extends EntityRelation> Q joinWhere(R joinedTable, Where where) {
        return joinWhere(JoinType.INNER, joinedTable, where);
    }


    /**
     * Inner Join tables by their ForeignKey
     *
     * @param tablePK  - table with PrimaryKey
     * @param tablesFK - tables with relagtion ForeignKey to tablePK
     * @param <E>
     * @param <R>
     * @return
     */
    default <E extends Entity & PrimaryKey, R extends EntityRelation> Q joinByPk(JoinType type, E tablePK, R... tablesFK) {
        if (tablePK == null) throw new NullParameter("tablesFK");
        for (R pk : tablesFK) {
            getJoins().add(new JoinWrapper(type, pk, tablePK.colID()));
        }
        return (Q) this;
    }


    default <R extends EntityRelation> Q joinThrough(JoinType type, Set<String> totables, R... throughTables) {
        if (throughTables == null) return (Q) this;
        Set<String> src = new HashSet<>();
        src.add(getEntityName());
        List<String> tables = Arrays.stream(throughTables).map(Entity::getEntityName).collect(Collectors.toCollection(LinkedList::new));
        setJoins(JoinTracker.calculateThrougth(getTarget().getTables(), src, totables, getJoins(), tables));
        return (Q) this;
    }


    default <E extends Entity, R extends EntityRelation> Q joinRoute(JoinType type, E queryEntity, R... joinedTables) {
        if (queryEntity == null || joinedTables == null) return (Q) this;
        Set<String> src = new HashSet<>();
        src.add(queryEntity.getEntityName());
        Set<String> tables = Arrays.stream(joinedTables).map(Entity::getEntityName).collect(Collectors.toSet());
        setJoins(JoinTracker.calculateJoins(getTarget().getTables(), src, tables, getJoins()));
        return (Q) this;
    }

    default <E extends Entity, F> Q join(JoinType type, Column<?, ?, ?> one, Column<?, ?, ?> two, boolean mark) {
        getJoins().add(new JoinWrapper(type, one, two, false));
        return (Q) this;
    }

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


    default <E extends Entity> Q join(JoinType type, E joinedTable, List<Column<?, ?, ?>> joinByColumns) {
        if (joinByColumns.size() == 1) {
            getJoins().add(new JoinWrapper(type, joinedTable, joinByColumns.get(0)));
        } else {

            WhereAnd whr = new WhereAnd();
            for (Column<?, ?, ?> c : joinByColumns) {
                whr.equalColumn((Column<?, Object, ?>) c, (Column<?, Object, ?>) c.transformTo(joinedTable));
            }

            getJoins().add(new JoinWrapper(type, joinedTable, whr));
        }

        return (Q) this;
    }


    default <E extends Entity> Q join(JoinType type, E joinedTable, List<Column<?, ?, ?>> joinByColumns, Where where) {
        if (joinByColumns.size() == 1) {
            getJoins().add(new JoinWrapper(type, joinedTable, joinByColumns.get(0)));
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

    default <E extends Entity> Q join(JoinType type, E joinedTable, Column<?, ?, ?> onColumn, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, onColumn, where));
        return (Q) this;
    }

    default <E extends Entity> Q join(JoinType type, E joinedTable, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, where));
        return (Q) this;
    }


    default <E extends Entity> Q join(JoinType type, E joinedTable, JoinOn<E, ?, ?> on, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, on.getRight(), where));
        return (Q) this;
    }


    default <E extends Entity> Q join(JoinType type, E joinedTable, JoinOn<E, ?, ?> on) {
        getJoins().add(new JoinWrapper(type, joinedTable, on.getRight()));
        return (Q) this;
    }

    default <E extends Entity> Q join(JoinType type, E joinedTable, Where where, Column<?, ?, ?>... joinByColumn) {
        if (joinByColumn.length == 1) {
            getJoins().add(new JoinWrapper(type, joinedTable, joinByColumn[0], where));
        } else {

            WhereAnd whr = new WhereAnd();
            for (Column<?, ?, ?> c : joinByColumn) {
                whr.equalColumn((Column<?, Object, ?>) c, (Column<?, Object, ?>) c.transformTo(joinedTable));
            }
            if (where != null) whr.and(where);

            getJoins().add(new JoinWrapper(type, joinedTable, whr));
        }

        return (Q) this;
    }


    default <R extends EntityRelation> Q joinWhere(JoinType type, R joinedTable, Where where) {
        getJoins().add(new JoinWrapper(type, joinedTable, where));
        return (Q) this;
    }


}
