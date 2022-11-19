package io.daobab.model;

import io.daobab.error.DaobabException;

import java.util.HashMap;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@SuppressWarnings("unused")
public class FlatPlate extends HashMap<String, Object> implements EntityMap {

    @Override
    public String getEntityName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public List<TableColumn> columns() {
        throw new DaobabException("FlatPlate has no columns");
    }

    public void fromPlate(Plate plate) {
        plate.toFlatPlate(this);
    }
}
