package io.daobab.statement.function.type;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.TableColumn;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.target.database.query.DataBaseQueryField;

import java.util.Arrays;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ManyArgumentsFunction<E extends Entity, F, R extends EntityRelation, C> extends ColumnFunction<E, F, R, C> {

    public ManyArgumentsFunction(String mode, Class<C> functionClass) {
        super(dummy, mode, functionClass);
    }

    public ManyArgumentsFunction(String mode, Object... values) {
        super(dummy, mode, determineClass(values));
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, ", ");
    }

    public ManyArgumentsFunction(String mode, Class clazz, ColumnOrQuery<?, ?, ?>... values) {
        super(dummy, mode, clazz);
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, ", ");
    }

    public ManyArgumentsFunction(String mode, String separator, ColumnOrQuery<?, F, ?>... values) {
        super(dummy, mode, determineClass(values));
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, separator);
    }

    public ManyArgumentsFunction(String mode, Class<C> clazz, String separator, ColumnOrQuery<?, F, ?>... values) {
        super(dummy, mode, clazz);
        setKeyValue(KEY_VALUES, Arrays.asList(values));
        setKeyValue(KEY_ARGUMENT, separator);
    }

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

}
