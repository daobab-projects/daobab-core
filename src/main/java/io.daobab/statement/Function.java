package io.daobab.statement;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.statement.function.base.CastColumnRelation;
import io.daobab.statement.function.base.CastType;
import io.daobab.statement.function.base.ColumnFunction;
import io.daobab.statement.function.base.SubstringColumnRelation;

public interface Function  {

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(Column<E, F, R> column, int from, int to) {
        return new SubstringColumnRelation<>(column, DictFunction.SUBSTRING, String.class, from, to);
    }

    static <E extends Entity, F, R extends EntityRelation, C> ColumnFunction<E, F, R, C> substring(Class<C> clazz, Column<E, F, R> column, int from, int to) {
        return new SubstringColumnRelation<>(column, DictFunction.SUBSTRING, clazz, from, to);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(Column<E, F, R> column, int from, Column<?, ? extends Number, ?> to) {
        return new SubstringColumnRelation(column, DictFunction.SUBSTRING, String.class, from, to);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> substring(Column<E, F, R> column, Column<?, Number, ?> from, Column<?, Number, ?> to) {
        return new SubstringColumnRelation<>(column, DictFunction.SUBSTRING, String.class, from, to);
    }


    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.UPPER, String.class);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.LOWER, String.class);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> length(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.LENGTH, Double.class);
    }


    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> concat(Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunction.CONCAT, String.class, ", ", columns);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> trim(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.TRIM, String.class);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> reverse(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.REVERSE, String.class);
    }

    static <E extends Entity, F, R extends EntityRelation, C> ColumnFunction<E, F, R, C> reverse(Class<C> clazz, Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.REVERSE, clazz);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> camel(Column<E, F, R> column) {
        return concat(upper(substring(column, 0, 1)), lower(substring(column, 1, length(column))));
    }


    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> sum(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.SUM, Double.class);
    }

    static <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.SUM, clazz);
    }

    static <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> sum(Class<C> clazz, Column<?, ?, ?>... columns) {
        return new ColumnFunction<>(DictFunction.SUM, clazz, "+", columns);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> avg(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.AVG, Double.class);
    }

    static <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> avg(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.AVG, clazz);
    }


    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> min(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.MIN, Double.class);
    }

    static <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> min(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.MIN, clazz);
    }


    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Double> max(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.MAX, Double.class);
    }

    static <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> max(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.MAX, clazz);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(E entity) {
        return new ColumnFunction<>(DictFunction.COUNT, Long.class, entity);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.COUNT, Long.class);
    }

    static <C extends Number, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> count(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.COUNT, clazz);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> countDistinct(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.COUNTDISTICT, Long.class);
    }

    static <C, E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, C> countDistinct(Column<E, F, R> column, Class<C> clazz) {
        return new ColumnFunction<>(column, DictFunction.COUNTDISTICT, clazz);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, F> distinct(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.DISTINCT, column.getFieldClass());
    }


//    /**
//     * String CURRENT_TIMESTAMP="CURRENT_TIMESTAMP";
//     * String DATEADD="DATEADD";
//     * String DATEDIFF="DATEDIFF";
//     * String DATEFROMPARTS="DATEFROMPARTS";
//     * String DATENAME="DATENAME";
//     * String DATEPART="DATEPART";
//     * String DAY="DAY";
//     * String GETDATE="GETDATE";
//     * String GETUTCDATE="GETUTCDATE";
//     * String ISDATE="ISDATE";
//     * String MONTH="MONTH";
//     * String SYSDATETIME="SYSDATETIME";
//     * String YEAR="YEAR";
//     *
//     * @return
//     */

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> day(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.DAY, String.class);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> year(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.YEAR, String.class);
    }

    static <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> month(Column<E, F, R> column) {
        return new ColumnFunction<>(column, DictFunction.MONTH, String.class);
    }

    static <E extends Entity, F, R extends EntityRelation, C> ColumnFunction<E, F, R, C> cast(Column<E, F, R> column, CastType type, Class<C> castType) {
        return new CastColumnRelation<>(column, DictFunction.CAST, type, castType);
    }
}
