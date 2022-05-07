package io.daobab.statement.join;

import io.daobab.experimental.dijsktra.Edge;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.marschal.Marshaller;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JoinWrapper<E extends Entity> {

    private JoinType type;
    private Column<?, ?, ?> byColumn;
    private Column<?, ?, ?> byColumn2;
    private E table;
    private Where where;

    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType type, Column<E, F, R> leftColumn, Column<E1, F, R> rightColumn) {
        setType(type);
        setByColumn(rightColumn);
        setTable(leftColumn.getInstance());

        WhereAnd wh = new WhereAnd();
        wh.equal(leftColumn, rightColumn);
        setWhere(wh);
    }

    public <E1 extends Entity, E2 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType type, Column<?, ?, ?> leftColumn, Column<?, ?, ?> rightColumn, boolean mark) {
        setType(type);
        setByColumn(leftColumn);
        setByColumn2(rightColumn);
        setTable((E) leftColumn.getInstance());

        WhereAnd wh = new WhereAnd();
        wh.equalColumn((Column<?, F, ?>) byColumn, (Column<?, F, ?>) byColumn2);
        setWhere(wh);
    }

    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType type, Column<E, F, R> table, Column<E1, F, R> byColumn, Where where) {
        setType(type);
        setByColumn(byColumn);
        setTable(table.getInstance());
        setWhere(where);
    }

    public JoinWrapper(JoinType join, E table, Where where) {
        setType(join);
        setTable(table);
        setWhere(where);
    }

    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType type, E table, Column<E1, F, R> byColumn) {
        //TODO: NPE
        setType(type);
        setByColumn(byColumn);
        setTable(table);

        WhereAnd wh = new WhereAnd();
        wh.equal(byColumn.transformTo(table), byColumn);
        setWhere(wh);
    }

    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType type, E table, Column<E1, F, R> byColumn, Where where) {
        //TODO: NPE
        setType(type);
        setByColumn(byColumn);
        setTable(table);

        WhereAnd wh = new WhereAnd();
        wh.equal(byColumn.transformTo(table), byColumn);
        if (where != null) wh.and(where);
        setWhere(wh);
    }

    public JoinWrapper(JoinType type, Edge e) {
        //TODO: NPE
        setType(type);
        setByColumn(e.getColumn());
        setTable((E) e.getToNode());

        WhereAnd wh = new WhereAnd();
        wh.equal(e.getColumn(), e.getToNode());
        setWhere(wh);
    }

    public JoinType getType() {
        return type;
    }

    public void setType(JoinType type) {
        this.type = type;
    }

    public Column<?, ?, ?> getByColumn() {
        return byColumn;
    }

    public void setByColumn(Column<?, ?, ?> byColumn) {
        this.byColumn = byColumn;
    }

    public void setByColumn2(Column<?, ?, ?> byColumn) {
        this.byColumn2 = byColumn;
    }

    public E getTable() {
        return table;
    }

    public void setTable(E table) {
        this.table = table;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> rv = new HashMap<>();
        rv.put("type", type.toString());
        rv.put("byColumn", Marshaller.marshallColumnToString(byColumn));
        rv.put("table", table.getClass().getName());
        if (getWhere() != null) rv.put("where", getWhere().toMap());
        return rv;
    }
}
