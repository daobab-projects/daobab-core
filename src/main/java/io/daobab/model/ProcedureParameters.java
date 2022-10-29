package io.daobab.model;

import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryColumn;
import io.daobab.model.dummy.DummyColumnTemplate;

import java.util.ArrayList;
import java.util.List;

public class ProcedureParameters {

    private final Plate plate = new Plate();

    private final List<Column<?, ?, ?>> columns;
    private final int length;

    public <E extends Entity> ProcedureParameters(E entity) {
        this(entity.columns().size());
        for (int i = 0; i < entity.columns().size(); i++) {
            specifyValue(i + 1, entity.columns().get(i).getColumn());
        }
    }

    public <E extends Entity> ProcedureParameters(Column<?, ?, ?>... columns) {
        this(columns == null ? 0 : columns.length);
        if (columns == null) {
            throw new MandatoryColumn();
        }
        for (int i = 0; i < columns.length; i++) {
            specifyValue(i + 1, columns[i]);
        }
    }

    public ProcedureParameters(int length) {
        this.length = length;
        columns = new ArrayList<>(length);
    }

    public <F> F getValue(Column<?, F, ?> column) {
        return plate.getValue(column);
    }

    public <F> F getValue(String name) {
        return plate.getValue(name);
    }

    public List<Object> getValues() {
        List<Object> rv = new ArrayList<>(length);

        for (Column<?, ?, ?> column : columns) {
            rv.add(getValue(column));
        }
        return rv;
    }

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

    public void setValue(int position, Object value) {
        if (position > (length + 1))
            throw new DaobabException("position greater than allowed value which is " + length);
        if (position <= 0) throw new DaobabException("position must be greater than 0 ");
        plate.setValue(columns.get(position - 1), value);
    }

    @SuppressWarnings("rawtypes")
    protected <E extends Entity, F, R extends EntityRelation> void specifyValue(int position, Column<E, F, R> column) {
        if (position > (length + 1))
            throw new DaobabException("position greater than allowed value which is " + length);
        if (position <= 0) throw new DaobabException("position must be greater than 0 ");
        columns.add(position - 1, column);
    }

    protected <F> void specifyValue(int position, String name, Class<F> type) {
        specifyValue(position, DummyColumnTemplate.dummyColumn(name, type));
    }

    public List<Column<?, ?, ?>> getColumns() {
        return columns;
    }
}
