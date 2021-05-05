package io.daobab.result;

import io.daobab.clone.EntityDuplicator;
import io.daobab.converter.JsonListHandler;
import io.daobab.error.DaobabException;
import io.daobab.model.EntityMap;
import io.daobab.model.FlatPlate;

import java.io.Serializable;
import java.util.Collection;

/**
 * FlatBuffer collection.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class FlatPlates extends FieldsBuffer<FlatPlate> implements Serializable, JsonListHandler, Cloneable {


    private static final long serialVersionUID = 2291798166104201910L;

    public FlatPlates() {
        super();
    }

    public FlatPlates(Collection<FlatPlate> entities) {
        super(entities);
    }


    public String toJSON() {
        StringBuilder rv = new StringBuilder();
        rv.append("[");

        int size = size();
        int cnt = 0;

        for (FlatPlate val : this) {

            if (!(val instanceof EntityMap)) {
                throw new DaobabException("this method can be used for EntityMap implementations only");
            }

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
