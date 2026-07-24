package io.daobab.model;

/**
 * A {@link Column} that addresses a column of a {@code HAVING} clause by its identifier/alias name (bound to a
 * {@link Dual} dummy instance). {@link #isIdentifiedAs()} tells whether the name is an {@code AS} alias rather
 * than a real column.
 *
 * @param <E> the entity type
 * @param <R> the relation type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ColumnHaving<E extends Entity, R extends RelatedTo> implements Column<E, String, R> {

    private final String name;
    private E instance;


    //If a column is identified by 'as' keyword
    private boolean identifiedAs = false;

    /**
     * @param name the column identifier/alias name
     */
    @SuppressWarnings("unchecked")
    public ColumnHaving(String name) {
        this.name = name;
        this.instance = (E) new Dual();
    }

    /**
     * @param name         the column identifier/alias name
     * @param identifiedAs whether the name is an {@code AS} alias
     */
    @SuppressWarnings("unchecked")
    public ColumnHaving(String name, boolean identifiedAs) {
        this.name = name;
        this.identifiedAs = identifiedAs;
        this.instance = (E) new Dual();
    }

    /**
     * {@inheritDoc} The identifier/alias name.
     */
    @Override
    public String getColumnName() {
        return name;
    }

    /** {@inheritDoc} The identifier/alias name. */
    @Override
    public String getFieldName() {
        return name;
    }

    /** {@inheritDoc} Always {@code String}. */
    @Override
    public Class<String> getFieldClass() {
        return String.class;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue(R entity) {
        return name; //TODO: here is value nat name isn't?
    }

    /** No-op: this column is not writable. */
    @Override
    public R setValue(R entity, String value) {
        return entity;
    }

    /** {@inheritDoc} A {@link Dual} dummy instance. */
    @Override
    public E getInstance() {
        return instance;
    }


    /** Whether the name refers to an {@code AS} alias rather than a real column. */
    public boolean isIdentifiedAs() {
        return identifiedAs;
    }
}
