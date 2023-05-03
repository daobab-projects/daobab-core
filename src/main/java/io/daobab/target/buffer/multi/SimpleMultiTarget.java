package io.daobab.target.buffer.multi;

import io.daobab.model.Entity;
import io.daobab.target.buffer.single.Entities;

import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class SimpleMultiTarget extends MultiEntityTarget {

    public SimpleMultiTarget() {
    }

    public SimpleMultiTarget(List<Entities<? extends Entity>> entitiesList) {
        for (Entities<?> e : entitiesList) {
            register(e);
        }
    }
}
