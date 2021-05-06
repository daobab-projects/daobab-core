package io.daobab.statement;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.statement.function.base.ColumnFunction;
import io.daobab.statement.function.base.SubstringColumnRelation;

public interface FunctionWhisperer {

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(Column<E, F, R> column, int from, int to) {
        return new SubstringColumnRelation<>(column, DictFunction.SUBSTRING, String.class, from, to);
    }

    default <E extends Entity, F, R extends EntityRelation, C> ColumnFunction<E, F, R, C> substring(Class<C> clazz, Column<E, F, R> column, int from, int to) {
        return new SubstringColumnRelation<>(column, DictFunction.SUBSTRING, clazz, from, to);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(Column<E, F, R> column, int from, Column<?, ? extends Number, ?> to) {
        return new SubstringColumnRelation(column, DictFunction.SUBSTRING, String.class, from, to);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(Column<E, F, R> column, Column<?, Number, ?> from, Column<?, Number, ?> to) {
        return new SubstringColumnRelation<>(column, DictFunction.SUBSTRING, String.class, from, to);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.UPPER, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.LOWER, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> length(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.LENGTH, Double.class);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunction.CONCAT, String.class, ", ", columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> trim(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.TRIM, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> reverse(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.REVERSE, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation, C> ColumnFunction<E, F, R, C> reverse(Class<C> clazz, Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.REVERSE, clazz);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> camel(Column<E, F, R> column) {
        return concat(upper(substring(column, 0, 1)), lower(substring(column, 1, length(column))));
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> sum(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.SUM, Double.class);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.SUM, clazz);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Class<C> clazz, Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunction.SUM, clazz, "+", columns);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> avg(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.AVG, Double.class);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> avg(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.AVG, clazz);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> min(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.MIN, Double.class);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> min(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.MIN, clazz);
    }


    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> max(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.MAX, Double.class);
    }

    default <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> max(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.MAX, clazz);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.COUNT, Long.class);
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> count(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.COUNT, clazz);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> countDistinct(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.COUNTDISTICT, Long.class);
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> countDistinct(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.COUNTDISTICT, clazz);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(E entity) {
        return new ColumnFunction<>(DictFunction.COUNT, Long.class, entity);
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> replace(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.REPLACE, clazz);
    }

    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> replicate(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.REPLICATE, clazz);
    }

    //TODO: see substring
    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> right(Column<E, F, R> column, Class<C> clazz, int length) {
        return new ColumnFunction<>(column, DictFunction.RIGHT, clazz);
    }

    //TODO: see substring
    default <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> left(Column<E, F, R> column, Class<C> clazz, int length) {
        return new ColumnFunction<>(column, DictFunction.LEFT, clazz);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> distinct(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.DISTINCT, column.getFieldClass());
    }
}
