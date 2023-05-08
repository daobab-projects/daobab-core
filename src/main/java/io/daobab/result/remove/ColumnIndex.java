package io.daobab.result.remove;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ColumnIndex<P extends Entity, O extends Number, R extends EntityRelation> {

    private Column<P, O, R> column;
    private Index<P, O> matchedIndex;

    public ColumnIndex(Column<P, O, R> column, Index<P, O> matchedPkList) {
        this.column = column;
        this.setMatchedIndex(matchedPkList);
    }


    public Column<P, O, R> getColumn() {
        return column;
    }

    public void setColumn(Column<P, O, R> column) {
        this.column = column;
    }


    public Index<P, O> getMatchedIndex() {
        return matchedIndex;
    }

    public void setMatchedIndex(Index<P, O> matchedIndex) {
        this.matchedIndex = matchedIndex;
    }
}
