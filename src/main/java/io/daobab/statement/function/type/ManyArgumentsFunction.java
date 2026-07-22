package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.model.TableColumn;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.target.database.query.DataBaseQueryField;

import java.util.Arrays;
import java.util.List;

/**
 * A function taking several arguments joined by a separator, e.g. {@code CONCAT(a, b, c)} or
 * {@code CONCAT_WS(sep, a, b)}. The arguments are stored under {@link #KEY_VALUES} and the joiner under
 * {@link #KEY_ARGUMENT}; the result type is inferred from the first column-like argument.
 *
 * @param <E> the entity
 * @param <F> the field type
 * @param <R> the relation type
 * @param <C> the result type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ManyArgumentsFunction<E extends Entity, F, R extends RelatedTo, C> extends ColumnFunction<E, F, R, C> {

    private String mediator;

    /**
     * @param mode     the SQL function name
     * @param mediator the token placed between the arguments (in addition to the separator)
     * @param values   the arguments
     */
    public ManyArgumentsFunction(String mode, String mediator, List values) {
        super(dummy, mode, determineClass(values));
        this.mediator = mediator;
        setKeyValue(KEY_VALUES, values);
        setKeyValue(KEY_ARGUMENT, ", ");
    }

    /**
     * @param mode          the SQL function name
     * @param functionClass the Java result type
     */
    public ManyArgumentsFunction(String mode, Class<C> functionClass) {
        super(dummy, mode, functionClass);
    }

    /**
     * @param mode   the SQL function name
     * @param values the arguments, joined by {@code ", "}
     */
    public ManyArgumentsFunction(String mode, Object... values) {
        super(dummy, mode, determineClass(values));
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, ", ");
    }

    /**
     * @param mode   the SQL function name
     * @param clazz  the Java result type
     * @param values the column/subquery arguments, joined by {@code ", "}
     */
    public ManyArgumentsFunction(String mode, Class clazz, ColumnOrQuery<?, ?, ?>... values) {
        super(dummy, mode, clazz);
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, ", ");
    }

    /**
     * @param mode      the SQL function name
     * @param separator the token joining the arguments
     * @param values    the column/subquery arguments
     */
    public ManyArgumentsFunction(String mode, String separator, ColumnOrQuery<?, F, ?>... values) {
        super(dummy, mode, determineClass(values));
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, separator);
    }

    /**
     * @param mode      the SQL function name
     * @param clazz     the Java result type
     * @param separator the token joining the arguments
     * @param values    the column/subquery arguments
     */
    public ManyArgumentsFunction(String mode, Class<C> clazz, String separator, ColumnOrQuery<?, F, ?>... values) {
        super(dummy, mode, clazz);
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, separator);
    }

    /** The field type of the first column-like argument, or {@code Object.class} when none is found. */
    private static Class determineClass(Object... values) {
        if (values == null) return Object.class;
        for (Object o : values) {
            if (o instanceof Column) {
                return ((Column) o).getFieldClass();
            }
            if (o instanceof DataBaseQueryField) {
                List<TableColumn> columns = ((DataBaseQueryField<?, ?>) o).getFields();
                if (columns == null || columns.isEmpty()) continue;
                return columns.get(0).getColumn().getFieldClass();
            }
        }
        return Object.class;
    }

    /**
     * The token placed between the arguments, or {@code null}.
     */
    public String getMediator() {
        return mediator;
    }

}
