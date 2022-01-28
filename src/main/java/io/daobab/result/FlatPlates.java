package io.daobab.result;

import io.daobab.clone.EntityDuplicator;
import io.daobab.converter.JsonHandler;
import io.daobab.model.FlatPlate;

import java.io.Serializable;
import java.util.Collection;

/**
 * FlatBuffer collection.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class FlatPlates extends FieldsBuffer<FlatPlate> implements Serializable, JsonHandler, Cloneable {


    private static final long serialVersionUID = 2291798166104201910L;

    public FlatPlates() {
        super();
    }

    public FlatPlates(Collection<FlatPlate> entities) {
        super(entities);
    }

    @Override
    public String toJSON() {
        StringBuilder rv = new StringBuilder();
        rv.append("[");

        int size = size();
        int cnt = 0;

        for (FlatPlate val : this) {

            cnt++;
            boolean lastOne = cnt == size;

            rv.append(val.toJSON());
            if (!lastOne) rv.append(",");
        }

        rv.append("]");

        return rv.toString();

    }

    @Override
    public FlatPlates clone() {
        return new FlatPlates(EntityDuplicator.cloneEntityList(this));
    }

}
