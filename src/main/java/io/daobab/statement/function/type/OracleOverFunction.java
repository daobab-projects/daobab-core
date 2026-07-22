package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;

/**
 * An Oracle window function carrying an {@code OVER (...)} clause (e.g. {@code ROW_NUMBER() OVER (...)}).
 *
 * @param <E> the entity
 * @param <F> the field type
 * @param <R> the relation type
 * @param <C> the result type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class OracleOverFunction<E extends Entity, F, R extends RelatedTo, C> extends ColumnFunction<E, F, R, C> {

    /**
     * @param mode          the SQL function name
     * @param functionClass the Java result type
     */
    public OracleOverFunction(String mode, Class<C> functionClass) {
        super(mode, functionClass);
    }

    /**
     * Adds the {@code PARTITION BY} column of the {@code OVER} clause.
     *
     * @param partitionBy the partitioning column
     * @return this function
     */
    public OracleOverFunction<E, F, R, C> over(Column<?, ?, ?> partitionBy) {
        return this;
    }
}
