package io.daobab.query.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;

/**
 * A join predicate pairing a left column with a right column of the same type ({@code left = right}).
 * <p>
 * Built with {@link #ON} and passed to the {@code join(...)} methods:
 * <pre>{@code
 * db.select(tabEmployee)
 *   .join(tabDepartment, JoinOn.ON(tabEmployee.colDepartmentId(), tabDepartment.colId()))
 *   .findMany();
 * }</pre>
 *
 * @param <E1> the left column's entity
 * @param <E2> the right column's entity
 * @param <F>  the shared column type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class JoinOn<E1 extends Entity, E2 extends Entity, F> {

    private Column<E1, F, ?> left;
    private Column<E2, F, ?> right;

    /**
     * @param left  the left column
     * @param right the right column, of the same type
     */
    public JoinOn(Column<E1, F, ?> left, Column<E2, F, ?> right) {
        setLeft(left);
        setRight(right);
    }

    /**
     * Creates a join predicate {@code left = right}.
     *
     * @param left  the left column
     * @param right the right column, of the same type
     * @return the predicate
     */
    public static <E1 extends Entity, E2 extends Entity, F> JoinOn<E1, E2, F> ON(Column<E1, F, ?> left, Column<E2, F, ?> right) {
        return new JoinOn<>(left, right);
    }

    /**
     * The left column.
     */
    public Column<E1, F, ?> getLeft() {
        return left;
    }

    /** Sets the left column. */
    public void setLeft(Column<E1, F, ?> left) {
        this.left = left;
    }

    /** The right column. */
    public Column<E2, F, ?> getRight() {
        return right;
    }

    /** Sets the right column. */
    public void setRight(Column<E2, F, ?> right) {
        this.right = right;
    }


}
