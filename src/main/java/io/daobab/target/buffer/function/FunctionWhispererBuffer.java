package io.daobab.target.buffer.function;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.dummy.DummyColumnTemplate;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.statement.function.dictionary.DictFunctionBuffer;
import io.daobab.statement.function.type.ColumnFunction;

@SuppressWarnings({"rawtypes", "unused"})
public interface FunctionWhispererBuffer {

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> count(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.COUNT, Long.class);
    }

    @SuppressWarnings("unchecked")
    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> countAll() {
        return new ColumnFunction<>((Column<E, F, R>) DummyColumnTemplate.dummyColumn("countAll"), DictFunctionBuffer.COUNT, Long.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> distinct(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.DISTINCT, Long.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, Long> length(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.LENGTH, Long.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> upper(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.UPPER, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> lower(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.LOWER, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> sum(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.SUM, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> max(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.MAX, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> min(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.MIN, String.class);
    }

    default <E extends Entity, F, R extends EntityRelation> ColumnFunction<E, F, R, String> avg(ColumnOrQuery<E, F, R> columnOrQuery) {
        return new ColumnFunction<>(columnOrQuery, DictFunctionBuffer.AVG, String.class);
    }

}
