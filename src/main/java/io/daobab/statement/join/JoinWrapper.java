package io.daobab.statement.join;

import io.daobab.experimental.dijsktra.Edge;
import io.daobab.model.*;
import io.daobab.query.marschal.Marschaller;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class JoinWrapper<E extends Entity> {

    private JoinType type;
    private Column<?, ?, ?> byColumn;
    private Column<?, ?, ?> byColumn2;
    private E table;

    private Where where;

    //TODO: kurcze czy te komulny nie powinny byc na odwrót?
    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType join, Column<E, F, R> table, Column<E1, F, R> byColumn) {
        setType(join);
        setByColumn(byColumn);
        setTable(table.getInstance());

        WhereAnd wh = new WhereAnd();
        wh.equal(table, byColumn);
        setWhere(wh);
    }

    public <E1 extends Entity, E2 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType join,Column<?, ?, ?> one, Column<?,?, ?> two, boolean mark) {
        setType(join);
        setByColumn(one);
        setByColumn2(two);
        setTable((E)one.getInstance());

        WhereAnd wh = new WhereAnd();
        wh.equalColumn( (Column<?, F, ?>)byColumn, (Column<?, F, ?>)byColumn2);
        setWhere(wh);
    }


    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType join, Column<E, F, R> table, Column<E1, F, R> byColumn, Where where) {
        setType(join);
        setByColumn(byColumn);
        setTable(table.getInstance());
        setWhere(where);
    }


    public JoinWrapper(JoinType join, E table, Where where) {
        setType(join);
        setTable(table);
        setWhere(where);
    }


    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType join, E table, Column<E1, F, R> byColumn) {
        //TODO: NPE
        setType(join);
        setByColumn(byColumn);
        setTable(table);

        WhereAnd wh = new WhereAnd();
        wh.equal(byColumn.transformTo(table), byColumn);
        setWhere(wh);
    }


    public <E1 extends Entity, F, R extends EntityRelation> JoinWrapper(JoinType join, E table, Column<E1, F, R> byColumn, Where where) {
        //TODO: NPE
        setType(join);
        setByColumn(byColumn);
        setTable(table);

        WhereAnd wh = new WhereAnd();
        wh.equal(byColumn.transformTo(table), byColumn);
        if (where != null) wh.and(where);
        setWhere(wh);
    }

    public JoinWrapper(JoinType join, Edge e) {
        //TODO: NPE
        setType(join);
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
        rv.put("byColumn", Marschaller.marschallColumnToString(byColumn));
        rv.put("table", table.getClass().getName());
        if (getWhere() != null) rv.put("where", getWhere().toMap());
        return rv;
    }
}
