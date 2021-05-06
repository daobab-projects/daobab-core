package io.daobab.statement.function.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class ColumnFunction<E extends Entity, F, R extends EntityRelation, C> implements Column<E, C, R> {

    private String mode;
    private String columnName;
    private String fieldName;
    private Class<C> columnClass;
    private final E thisEntity;
    public String identifier;

    public Column<E, F, R> column;
    public Column[] columns;
    public String mediator;

//    public C apply(){
//        if (this.column instanceof ColumnFunction){
//            ColumnFunction<E,?,R,F> cc=(ColumnFunction<E,?,R,F>) this.column;
//            F some=cc.apply();
//        }
//        return null;
//    }

    public ColumnFunction(String mode, Class<C> functionClass, E entity) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = entity;
    }

    public ColumnFunction(Column<E, F, R> column, String mode, Class<C> functionClass) {
        setMode(mode);
        columnName = column.getColumnName();
        fieldName = column.getFieldName();
        columnClass = functionClass;
        thisEntity = column.getInstance();
        this.column = column;
    }

    public ColumnFunction(String mode, Class<C> functionClass, String mediator, Column... columns) {
        setMode(mode);
        columnClass = functionClass;
        thisEntity = (E) columns[0].getInstance();
        this.columns = columns;
        this.mediator = mediator;
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

    public ColumnFunction<E, F, R, C> as(Class<C> functionClass) {
        this.columnClass = functionClass;
        return this;
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
}
