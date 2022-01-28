package io.daobab.statement.function.type;

import io.daobab.error.MandatoryFunctionParameter;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.dummy.DummyColumnTemplate;
import io.daobab.query.base.Query;
import io.daobab.query.marker.ColumnOrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class ColumnFunction<E extends Entity, F, R extends EntityRelation, C> implements Column<E, C, R> {

    private Map<String, Object> functionMap = new HashMap<>();
    private String mode;
    private String columnName;
    private String fieldName;
    protected Class<C> columnClass;
    private final E thisEntity;
    public String identifier;
    private boolean noParameter = false;

    public Column<E, F, R> column;
    public Query<E, ?> query;
    public Column[] columns;
    public String mediator;

    protected static Column dummy = DummyColumnTemplate.dummyColumn("dummy");

    public static String KEY_VALUES = "VALUES";
    public static String BEFORE_COL3 = "BEFORE_COL3";
    public static String BEFORE_COL2 = "BEFORE_COL2";
    public static String BEFORE_COL = "BEFORE_COL";
    public static String AFTER_COL = "AFTER_COL";
    public static String AFTER_COL2 = "AFTER_COL2";
    public static String AFTER_COL3 = "AFTER_COL3";
    public static String AFTER_COL4 = "AFTER_COL4";
    public static String KEY_ARGUMENT = "ARGUMENT";


    public ColumnFunction(String mode, Class<C> functionClass) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) dummy.getInstance();
    }

    public ColumnFunction(String mode, Class<C> functionClass, E entity) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = entity;
    }

    public ColumnFunction(Column<E, F, R> column, String mode, Class<C> functionClass, Object argument) {
        this(column, mode, functionClass);
        setKeyValue(KEY_VALUES, argument);
    }

    public ColumnFunction(String mode) {
        this(dummy, mode);
    }

    public ColumnFunction(Column<E, F, R> column, String mode) {
        setMode(mode);
        columnName = column.getColumnName();
        fieldName = column.getFieldName();
        columnClass = (Class<C>) column.getFieldClass();
        thisEntity = column.getInstance();
        this.column = column;
    }

    public ColumnFunction(ColumnOrQuery<E, F, R> col, String mode) {
        setMode(mode);
        if (col instanceof Column) {
            Column<E, F, R> column = (Column<E, F, R>) col;
            columnName = column.getColumnName();
            fieldName = column.getFieldName();
            columnClass = (Class<C>) column.getFieldClass();
            thisEntity = column.getInstance();
            this.column = column;
        } else {
            Query<E, ?> query = (Query<E, ?>) col;
            this.query = query;
            Column<E, F, R> column = (query.getFields().get(0)).getColumn();
            columnClass = (Class<C>) column.getFieldClass();
            thisEntity = (E) dummy.getInstance();
        }
    }


    public ColumnFunction(Column<E, F, R> column, String mode, Class<C> functionClass) {
        if (column == null) throw new MandatoryFunctionParameter(mode);
        setMode(mode);
        columnName = column.getColumnName();
        fieldName = column.getFieldName();
        columnClass = functionClass;
        thisEntity = column.getInstance();
        this.column = column;
    }


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
        } else {
            Query<E, ?> query = (Query<E, ?>) col;
            this.query = query;
            columnClass = functionClass;
            thisEntity = (E) dummy.getInstance();
        }

    }

    public ColumnFunction(String columnIdentifier, String mode, Class<C> functionClass) {
        if (columnIdentifier == null) throw new MandatoryFunctionParameter(mode);
        setMode(mode);
        columnClass = functionClass;
        identifier = columnIdentifier;
        this.thisEntity = (E) dummy.getInstance();
    }

    public ColumnFunction(String mode, Class<C> functionClass, String mediator, Column... columns) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) columns[0].getInstance();
        this.columns = columns;
        this.mediator = mediator;
    }

    public ColumnFunction(String mode, Class<C> functionClass, Column... columns) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) columns[0].getInstance();
        this.columns = columns;
    }

    public ColumnFunction(String mode, Class<C> functionClass, Object... values) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) columns[0].getInstance();
        setKeyValue(KEY_VALUES, values);
    }

    public ColumnFunction<E, F, R, C> as(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public ColumnFunction<E, F, R, C> as(String identifier, Class<C> functionClass) {
        this.identifier = identifier;
        this.columnClass = functionClass;
        return this;
    }

    public <N> ColumnFunction<E, F, R, N> asCast(String identifier, Class<N> functionClass) {
        ColumnFunction<E, F, R, N> rv = cast(functionClass).as(identifier);
        return rv.as(identifier);
    }

    public ColumnFunction<E, F, R, C> as(Class<C> functionClass) {
        this.columnClass = functionClass;
        return this;
    }

    public <N> ColumnFunction<E, F, R, N> cast(Class<N> clazz) {
        ColumnFunction<E, F, R, N> rv = (ColumnFunction<E, F, R, N>) this;
        rv.columnClass = clazz;
        return rv;
    }


    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getFieldName() {
        if (identifier != null) return identifier;
        if (fieldName != null) return mode + "_" + fieldName;
        return mode;
    }


    @Override
    public Class<C> getFieldClass() {
        return columnClass;
    }

    @Override
    public C getValue(R entity) {
        return getValueOf(entity);
    }

    @Override
    public void setValue(R entity, C value) {

    }

    @Override
    public E getInstance() {
        return thisEntity;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setKeyValue(String key, Object value) {
        if (value == null) return;
        getFunctionMap().put(key, value);
    }

    public void setMandatoryKeyValue(String key, Object value) {
        if (value == null) throw new MandatoryFunctionParameter(mode);
        getFunctionMap().put(key, value);
    }

    public Object getKeyValue(String key) {
        return getFunctionMap().get(key);
    }

    public Map<String, Object> getFunctionMap() {
        return functionMap;
    }

    public void setFunctionMap(Map<String, Object> functionMap) {
        this.functionMap = functionMap;
    }

    public boolean isNoParameter() {
        return noParameter;
    }

    public void setNoParameter(boolean noParameter) {
        this.noParameter = noParameter;
    }
}
