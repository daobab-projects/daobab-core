package io.daobab.model;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class ColumnHaving<E extends Entity, R extends EntityRelation> implements Column<E, String, R> {

    private final String name;

    public ColumnHaving(String name) {
        this.name = name;
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
    public void setValue(R entity, String value) {

    }

    @Override
    public E getInstance() {
        return null;
    }
}
