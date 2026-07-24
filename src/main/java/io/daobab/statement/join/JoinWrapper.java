package io.daobab.statement.join;

import io.daobab.experimental.dijsktra.Edge;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.query.marschal.Marshaller;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;

import java.util.HashMap;
import java.util.Map;

/**
 * One join of a query: its {@link JoinType type}, the joined {@link #getTable() table}, the {@code ON} column(s)
 * and the {@code ON}/extra {@link Where} condition. The various constructors build the {@code ON} condition from
 * a column pair, a single column (matched against its counterpart on the joined table), or an explicit
 * {@code Where}. Assembled by the {@code QueryJoin} mix-in and by the {@link JoinTracker}.
 *
 * @param <E> the joined entity type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JoinWrapper<E extends Entity> {

    private JoinType type;
    private Column<?, ?, ?> byColumn;
    private Column<?, ?, ?> byColumn2;
    private E table;
    private Where where;

    /**
     * Joins on {@code leftColumn = rightColumn}; the joined table is the left column's entity.
     */
    public <E1 extends Entity, F, R extends RelatedTo> JoinWrapper(JoinType type, Column<E, F, R> leftColumn, Column<E1, F, R> rightColumn) {
        setType(type);
        setByColumn(rightColumn);
        setTable(leftColumn.getInstance());

        WhereAnd wh = new WhereAnd();
        wh.equal(leftColumn, rightColumn);
        setWhere(wh);
    }

    /** Joins comparing two columns ({@code leftColumn = rightColumn}); {@code mark} selects this overload. */
    public <E1 extends Entity, E2 extends Entity, F, R extends RelatedTo> JoinWrapper(JoinType type, Column<?, ?, ?> leftColumn, Column<?, ?, ?> rightColumn, boolean mark) {
        setType(type);
        setByColumn(leftColumn);
        setByColumn2(rightColumn);
        setTable((E) leftColumn.getInstance());

        WhereAnd wh = new WhereAnd();
        wh.equalColumn((Column<?, F, ?>) byColumn, (Column<?, F, ?>) byColumn2);
        setWhere(wh);
    }

    /** Joins the table of {@code table} on {@code byColumn} with an explicit where condition. */
    public <E1 extends Entity, F, R extends RelatedTo> JoinWrapper(JoinType type, Column<E, F, R> table, Column<E1, F, R> byColumn, Where where) {
        setType(type);
        setByColumn(byColumn);
        setTable(table.getInstance());
        setWhere(where);
    }

    /** Joins {@code table} with an explicit where condition (no {@code ON} column). */
    public JoinWrapper(JoinType join, E table, Where where) {
        setType(join);
        setTable(table);
        setWhere(where);
    }

    /** Joins {@code table} on {@code byColumn}, matched against the same column transformed onto the table. */
    public <E1 extends Entity, F, R extends RelatedTo> JoinWrapper(JoinType type, E table, Column<E1, F, R> byColumn) {
        //TODO: NPE
        setType(type);
        setByColumn(byColumn);
        setTable(table);

        WhereAnd wh = new WhereAnd();
        wh.equal(byColumn.transformTo(table), byColumn);
        setWhere(wh);
    }

    /** Joins {@code table} on {@code byColumn} plus an extra where condition. */
    public <E1 extends Entity, F, R extends RelatedTo> JoinWrapper(JoinType type, E table, Column<E1, F, R> byColumn, Where where) {
        //TODO: NPE
        setType(type);
        setByColumn(byColumn);
        setTable(table);

        WhereAnd wh = new WhereAnd();
        wh.equal(byColumn.transformTo(table), byColumn);
        if (where != null) wh.and(where);
        setWhere(wh);
    }

    /** Joins along a computed graph {@link Edge} (used by the {@link JoinTracker} route finding). */
    public JoinWrapper(JoinType type, Edge e) {
        //TODO: NPE
        setType(type);
        setByColumn(e.getColumn());
        setTable((E) e.getToNode());

        WhereAnd wh = new WhereAnd();
        wh.equal(e.getColumn(), e.getToNode());
        setWhere(wh);
    }

    /** The join type. */
    public JoinType getType() {
        return type;
    }

    /** Sets the join type. */
    public void setType(JoinType type) {
        this.type = type;
    }

    /** The primary {@code ON} column. */
    public Column<?, ?, ?> getByColumn() {
        return byColumn;
    }

    /** Sets the primary {@code ON} column. */
    public void setByColumn(Column<?, ?, ?> byColumn) {
        this.byColumn = byColumn;
    }

    /** Sets the secondary {@code ON} column (for a column-to-column join). */
    public void setByColumn2(Column<?, ?, ?> byColumn) {
        this.byColumn2 = byColumn;
    }

    /** The joined table. */
    public E getTable() {
        return table;
    }

    /** Sets the joined table. */
    public void setTable(E table) {
        this.table = table;
    }

    /** The {@code ON}/extra condition of the join. */
    public Where getWhere() {
        return where;
    }

    /** Sets the {@code ON}/extra condition of the join. */
    public void setWhere(Where where) {
        this.where = where;
    }

    /** Serializes this join to a transport map (type, {@code ON} column, table and condition). */
    public Map<String, Object> toMap() {
        Map<String, Object> rv = new HashMap<>();
        rv.put("type", type.toString());
        rv.put("byColumn", Marshaller.marshallColumnToString(byColumn));
        rv.put("table", table.getClass().getName());
        if (getWhere() != null) rv.put("where", getWhere().toMap());
        return rv;
    }
}
