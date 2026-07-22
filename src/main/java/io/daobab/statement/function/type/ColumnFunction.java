package io.daobab.statement.function.type;

import io.daobab.error.MandatoryFunctionParameter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.model.dummy.DummyColumnTemplate;
import io.daobab.query.base.Query;
import io.daobab.query.marker.ColumnOrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * A SQL function applied to a column, itself usable as a {@link Column} - so a function may be selected, ordered
 * by, or nested inside another function. The {@link #getMode() mode} is the SQL function name (e.g. {@code SUM},
 * {@code UPPER}, {@code CAST}), {@code columnClass} is the result type, and the arguments live in the
 * {@link #getFunctionMap() function map} (positioned before/after the column, or as a value list); a function
 * may also wrap several {@link #columns} joined by a {@link #mediator}, or an inner {@link #query}.
 * <p>
 * Built through the dialect {@code FunctionWhisperer} mix-ins rather than directly, and aliased with
 * {@link #as(String)} / typed with {@link #cast(Class)}.
 *
 * @param <E> the column's entity
 * @param <F> the column's field type
 * @param <R> the column's relation type
 * @param <C> the result type of the function
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ColumnFunction<E extends Entity, F, R extends RelatedTo, C> implements Column<E, C, R> {

    /**
     * Function-map key for the argument value list.
     */
    public static final String KEY_VALUES = "VALUES";
    /** Function-map key for the third argument placed before the column. */
    public static final String BEFORE_COL3 = "BEFORE_COL3";
    /** Function-map key for the second argument placed before the column. */
    public static final String BEFORE_COL2 = "BEFORE_COL2";
    /** Function-map key for the argument placed before the column. */
    public static final String BEFORE_COL = "BEFORE_COL";
    /** Function-map key for the argument placed after the column. */
    public static final String AFTER_COL = "AFTER_COL";
    /** Function-map key for the second argument placed after the column. */
    public static final String AFTER_COL2 = "AFTER_COL2";
    /** Function-map key for the third argument placed after the column. */
    public static final String AFTER_COL3 = "AFTER_COL3";
    /** Function-map key for the fourth argument placed after the column. */
    public static final String AFTER_COL4 = "AFTER_COL4";
    /** Function-map key for the argument separator. */
    public static final String KEY_ARGUMENT = "ARGUMENT";
    @SuppressWarnings("rawtypes")
    protected static Column dummy = DummyColumnTemplate.dummyColumn("dummy");
    private final E thisEntity;
    /** The alias of the function ({@code AS identifier}), when set. */
    public String identifier;
    /** The column the function is applied to, or {@code null} for a column-less function. */
    public Column<E, F, R> column;
    /** The inner query, when the function wraps a subquery. */
    public Query<E, ?, ?> query;
    /** The columns, when the function takes several. */
    public Column[] columns;
    /** The token joining several column arguments. */
    public String mediator;
    protected Class<C> columnClass;
    private Map<String, Object> functionMap = new HashMap<>();
    private String mode;
    private String columnName;
    private String fieldName;
    private boolean noParameter = false;
    private boolean parentFunction = false;

    /** A column-less function of the given result type (applied to a dummy column). */
    @SuppressWarnings("unchecked")
    public ColumnFunction(String mode, Class<C> functionClass) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) dummy.getInstance();
    }

    /** A column-less function bound to the given entity. */
    public ColumnFunction(String mode, Class<C> functionClass, E entity) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = entity;
    }

    /** A function of a column with a single extra argument. */
    public ColumnFunction(Column<E, F, R> column, String mode, Class<C> functionClass, Object argument) {
        this(column, mode, functionClass);
        setKeyValue(KEY_VALUES, argument);
    }

    /** A column-less function (applied to a dummy column), result type inferred later. */
    @SuppressWarnings("unchecked")
    public ColumnFunction(String mode) {
        this(dummy, mode);
    }

    /** A function of a column, inheriting the column's field type as the result type. */
    @SuppressWarnings("unchecked")
    public ColumnFunction(Column<E, F, R> column, String mode) {
        setMode(mode);
        columnName = column.getColumnName();
        fieldName = column.getFieldName();
        columnClass = (Class<C>) column.getFieldClass();
        thisEntity = column.getInstance();
        this.column = column;
        this.parentFunction = column instanceof ColumnFunction;
    }

    /** A function of a column or an inner query, inheriting the field type as the result type. */
    @SuppressWarnings("unchecked")
    public ColumnFunction(ColumnOrQuery<E, F, R> col, String mode) {
        setMode(mode);
        if (col instanceof Column) {
            Column<E, F, R> column = (Column<E, F, R>) col;
            columnName = column.getColumnName();
            fieldName = column.getFieldName();
            columnClass = (Class<C>) column.getFieldClass();
            thisEntity = column.getInstance();
            this.column = column;
            this.parentFunction = column instanceof ColumnFunction;
        } else {
            Query<E, ?, ?> query = (Query<E, ?, ?>) col;
            this.query = query;
            Column<E, F, R> column = (query.getFields().get(0)).getColumn();
            columnClass = (Class<C>) column.getFieldClass();
            thisEntity = (E) dummy.getInstance();
        }
    }

    /** A function of a column with an explicit result type (the column is mandatory). */
    public ColumnFunction(Column<E, F, R> column, String mode, Class<C> functionClass) {
        if (column == null) throw new MandatoryFunctionParameter(mode);
        setMode(mode);
        columnName = column.getColumnName();
        fieldName = column.getFieldName();
        columnClass = functionClass;
        thisEntity = column.getInstance();
        this.column = column;
        this.parentFunction = column instanceof ColumnFunction;
    }

    /** A function of a column or an inner query with an explicit result type (the argument is mandatory). */
    @SuppressWarnings("unchecked")
    public ColumnFunction(ColumnOrQuery<E, F, R> col, String mode, Class<C> functionClass) {
        if (col == null) throw new MandatoryFunctionParameter(mode);
        setMode(mode);
        if (col instanceof Column) {
            Column<E, F, R> column = (Column<E, F, R>) col;
            columnName = column.getColumnName();
            fieldName = column.getFieldName();
            columnClass = functionClass;
            thisEntity = column.getInstance();
            this.column = column;
            this.parentFunction = column instanceof ColumnFunction;
        } else {
            this.query = (Query<E, ?, ?>) col;
            columnClass = functionClass;
            thisEntity = (E) dummy.getInstance();
        }
    }

    /** A function over a column addressed by its identifier/alias, with an explicit result type. */
    @SuppressWarnings("unchecked")
    public ColumnFunction(String columnIdentifier, String mode, Class<C> functionClass) {
        if (columnIdentifier == null) throw new MandatoryFunctionParameter(mode);
        setMode(mode);
        columnClass = functionClass;
        identifier = columnIdentifier;
        this.thisEntity = (E) dummy.getInstance();
    }

    /** A function over several columns joined by a mediator token. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ColumnFunction(String mode, Class<C> functionClass, String mediator, Column... columns) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) columns[0].getInstance();
        this.columns = columns;
        this.mediator = mediator;
    }

    /** A function over several columns. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ColumnFunction(String mode, Class<C> functionClass, Column... columns) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) columns[0].getInstance();
        this.columns = columns;
    }

    /** A function over a list of plain argument values. */
    @SuppressWarnings("unchecked")
    public ColumnFunction(String mode, Class<C> functionClass, Object... values) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) columns[0].getInstance();
        setKeyValue(KEY_VALUES, values);
    }

    /** Aliases the function ({@code AS identifier}). */
    public ColumnFunction<E, F, R, C> as(String identifier) {
        this.identifier = identifier;
        return this;
    }

    /** Aliases the function and sets its result type. */
    public ColumnFunction<E, F, R, C> as(String identifier, Class<C> functionClass) {
        this.identifier = identifier;
        this.columnClass = functionClass;
        return this;
    }

    /** Casts the function to the given result type and aliases it. */
    public <N> ColumnFunction<E, F, R, N> asCast(String identifier, Class<N> functionClass) {
        ColumnFunction<E, F, R, N> rv = cast(functionClass).as(identifier);
        return rv.as(identifier);
    }

    /** Sets the function's result type. */
    public ColumnFunction<E, F, R, C> as(Class<C> functionClass) {
        this.columnClass = functionClass;
        return this;
    }

    /** Reinterprets this function as one of the given result type (same instance, new type parameter). */
    @SuppressWarnings("unchecked")
    public <N> ColumnFunction<E, F, R, N> cast(Class<N> clazz) {
        ColumnFunction<E, F, R, N> rv = (ColumnFunction<E, F, R, N>) this;
        rv.columnClass = clazz;
        return rv;
    }


    /** {@inheritDoc} */
    @Override
    public String getColumnName() {
        return columnName;
    }

    /**
     * The field name of the function: the alias when set, otherwise {@code mode_columnField}, otherwise the mode.
     */
    @Override
    public String getFieldName() {
        if (identifier != null) return identifier;
        if (fieldName != null) return mode + "_" + fieldName;
        return mode;
    }


    /** {@inheritDoc} */
    @Override
    public Class<C> getFieldClass() {
        return columnClass;
    }

    /** {@inheritDoc} */
    @Override
    public C getValue(R entity) {
        return getValueOf(entity);
    }

    /** A function is read-only: setting does nothing and returns the entity unchanged. */
    @Override
    public R setValue(R entity, C value) {
        return entity;
    }

    /** {@inheritDoc} */
    @Override
    public E getInstance() {
        return thisEntity;
    }

    /** The SQL function name. */
    public String getMode() {
        return mode;
    }

    /** Sets the SQL function name. */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /** Stores a function argument under the given key (a {@code null} value is ignored). */
    public void setKeyValue(String key, Object value) {
        if (value == null) return;
        getFunctionMap().put(key, value);
    }

    /**
     * Stores a mandatory function argument under the given key.
     *
     * @throws MandatoryFunctionParameter when {@code value} is {@code null}
     */
    public void setMandatoryKeyValue(String key, Object value) {
        if (value == null) throw new MandatoryFunctionParameter(mode);
        getFunctionMap().put(key, value);
    }

    /** The function argument stored under the given key. */
    public Object getKeyValue(String key) {
        return getFunctionMap().get(key);
    }

    /** The map of the function arguments (keyed by the {@code BEFORE_COL}/{@code AFTER_COL}/... constants). */
    public Map<String, Object> getFunctionMap() {
        return functionMap;
    }

    /** Replaces the function-argument map. */
    public void setFunctionMap(Map<String, Object> functionMap) {
        this.functionMap = functionMap;
    }

    /** Whether the function takes no column argument (e.g. {@code PI()}). */
    public boolean isNoParameter() {
        return noParameter;
    }

    /** Sets whether the function takes no column argument. */
    public void setNoParameter(boolean noParameter) {
        this.noParameter = noParameter;
    }

    /** The innermost real column, unwrapping any nested functions. */
    public Column getFinalColumn() {
        if (!hasChild()) {
            return column;
        }
        ColumnFunction columnFunction = (ColumnFunction) column;
        return columnFunction.getFinalColumn();
    }

    /** Whether this function wraps another function (rather than a plain column). */
    public boolean hasChild() {
        return parentFunction;
    }

    /** The wrapped inner function, or {@code null} when this function wraps a plain column. */
    public ColumnFunction getChild() {
        if (!hasChild()) {
            return null;
        }
        return (ColumnFunction) column;
    }
}
