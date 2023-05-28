package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ColumnHaving<E extends Entity, R extends RelatedTo> implements Column<E, String, R> {

    private final String name;
    private E instance;


    //If a column is identified by 'as' keyword
    private boolean identifiedAs = false;

    public ColumnHaving(String name) {
        this.name = name;
        this.instance = (E) new Dual();
    }

    public ColumnHaving(String name, boolean identifiedAs) {
        this.name = name;
        this.identifiedAs = identifiedAs;
        this.instance = (E) new Dual();
    }

    @Override
    public String getColumnName() {
        return name;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public Class<String> getFieldClass() {
        return String.class;
    }

    @Override
    public String getValue(R entity) {
        return name; //TODO: here is value nat name isn't?
    }

    @Override
    public R setValue(R entity, String value) {
        return entity;
    }

    @Override
    public E getInstance() {
        return instance;
    }


    public boolean isIdentifiedAs() {
        return identifiedAs;
    }
}
