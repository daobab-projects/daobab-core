package io.daobab.statement.condition;

import io.daobab.model.ColumnHaving;
import io.daobab.statement.where.base.Where;

import static io.daobab.statement.condition.Operator.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class Having extends Where<Having> {

    // ------ Contructors
    public Having() {
        super();
    }

    public Having(Where where) {
        super();
        having(where);
    }

    public Having having(Where val) {
        temp(val);
        return this;
    }
    @Override
    public String getRelationBetweenExpressions() {
        return AND;
    }


    public final Having equal(String column, Object val) {
        tempHaving(new ColumnHaving<>(column),  EQ, val);
        return this;
    }

    public final Having greater(String column, Object val) {
        tempHaving(new ColumnHaving<>(column),  GT, val);
        return this;
    }
    public final Having greaterOrEqual(String column, Object val) {
        tempHaving(new ColumnHaving<>(column),  GTEQ, val);
        return this;
    }
    public final Having less(String column, Object val) {
        tempHaving(new ColumnHaving<>(column),  LT, val);
        return this;
    }
    public final Having lessOrEqual(String column, Object val) {
        tempHaving(new ColumnHaving<>(column),  LTEQ, val);
        return this;
    }
    public final Having notEqual(String column, Object val) {
        tempHaving(new ColumnHaving<>(column),  NOT_EQ, val);
        return this;
    }


}
