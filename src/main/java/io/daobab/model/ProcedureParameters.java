package io.daobab.model;

import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryColumn;
import io.daobab.model.dummy.DummyColumnTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ordered parameters of a stored-procedure call: a positional list of columns (their types) whose values are
 * held in a backing {@link Plate}. Built from an entity, a set of columns or a fixed length, then filled with
 * {@link #setValue}/{@link #setValues} and read back with {@link #getValue}/{@link #getValues}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ProcedureParameters {

    private Plate plate;

    private final List<Column<?, ?, ?>> columns = new ArrayList<>();
    private final int length;

    /**
     * Parameters shaped after an entity's columns.
     */
    public <E extends Entity> ProcedureParameters(E entity) {
        this.length = entity.columns().size();
        for (int i = 0; i < entity.columns().size(); i++) {
            Column<?, ?, ?> col = entity.columns().get(i).getColumn();
            specifyValue(i + 1, col);
        }
        plate = new Plate(entity.columns().stream().map(TableColumn::getColumn).collect(Collectors.toList()));
    }

    /** Parameters shaped after the given table columns. */
    public ProcedureParameters(TableColumn... columns) {
        if (columns == null) {
            throw new MandatoryColumn();
        }
        this.length = columns.length;
        List<TableColumn> tableColumns = Arrays.asList(columns);
        for (int i = 0; i < columns.length; i++) {
            specifyValue(i + 1, columns[i].getColumn());
        }
        plate = new Plate(tableColumns.stream().map(TableColumn::getColumn).collect(Collectors.toList()));
    }

    /** Parameters shaped after the given columns. */
    public <E extends Entity> ProcedureParameters(Column<?, ?, ?>... columns) {
        this(columns == null ? 0 : columns.length);
        if (columns == null) {
            throw new MandatoryColumn();
        }
        for (int i = 0; i < columns.length; i++) {
            specifyValue(i + 1, columns[i]);
        }
    }

    /** A fixed number of (initially unspecified) parameters. */
    public ProcedureParameters(int length) {
        this.length = length;
    }

    /** The value bound to the given column. */
    public <F> F getValue(Column<?, F, ?> column) {
        return plate.getValue(column);
    }

    /** The value bound to the named parameter. */
    public <F> F getValue(String name) {
        return plate.getValue(name);
    }

    /** The parameter values, in order. */
    public List<Object> getValues() {
        List<Object> rv = new ArrayList<>(length);

        for (Column<?, ?, ?> column : columns) {
            rv.add(getValue(column));
        }
        return rv;
    }

    /**
     * Binds all the values, in order.
     *
     * @throws DaobabException when the object is uninitialized or the value count does not match
     */
    public void setValues(Object... objects) {
        if (length == 0) {
            throw new DaobabException(this.getClass().getSimpleName() + " - object is not initialised correctly");
        } else if (objects == null) {
            throw new DaobabException(this.getClass().getSimpleName() + " - null array of values are not allowed.");
        } else if (objects.length != this.length) {
            throw new DaobabException(this.getClass().getSimpleName() + " - there is " + this.length + " parameters, but the amount of values: " + objects.length + " is not equal");
        }
        for (int i = 0; i < getColumns().size(); i++) {
            plate.setValue(getColumns().get(i), objects[i]);
        }
    }

    /** Binds the value at the given 1-based position. */
    public void setValue(int position, Object value) {
        if (position > (length + 1))
            throw new DaobabException("position greater than allowed value which is " + length);
        if (position <= 0) throw new DaobabException("position must be greater than 0 ");
        plate.setValue(columns.get(position - 1), value);
    }

    /** Registers the column at the given 1-based position (defines the parameter, not its value). */
    @SuppressWarnings("rawtypes")
    protected <E extends Entity, F, R extends RelatedTo> void specifyValue(int position, Column<E, F, R> column) {
        if (position > (length + 1))
            throw new DaobabException("position greater than allowed value which is " + length);
        if (position <= 0) throw new DaobabException("position must be greater than 0 ");
        columns.add(position - 1, column);
    }

    /** Registers a named parameter of the given type at the given position. */
    protected <F> void specifyValue(int position, String name, Class<F> type) {
        specifyValue(position, DummyColumnTemplate.dummyColumn(name, type));
    }

    /** The parameter columns, in order. */
    public List<Column<?, ?, ?>> getColumns() {
        return columns;
    }
}
