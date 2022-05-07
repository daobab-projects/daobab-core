package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class JoinOn<E1 extends Entity, E2 extends Entity, F> {

    private Column<E1, F, ?> left;
    private Column<E2, F, ?> right;

    public JoinOn(Column<E1, F, ?> left, Column<E2, F, ?> right) {
        setLeft(left);
        setRight(right);
    }

    public static <E1 extends Entity, E2 extends Entity, F> JoinOn<E1, E2, F> ON(Column<E1, F, ?> left, Column<E2, F, ?> right) {
        return new JoinOn<>(left, right);
    }

    public Column<E1, F, ?> getLeft() {
        return left;
    }

    public void setLeft(Column<E1, F, ?> left) {
        this.left = left;
    }

    public Column<E2, F, ?> getRight() {
        return right;
    }

    public void setRight(Column<E2, F, ?> right) {
        this.right = right;
    }


}
