package io.daobab.target.buffer.multi;

import io.daobab.model.Entity;
import io.daobab.target.buffer.single.Entities;

import java.util.List;

public class SimpleMulti extends MultiEntityTarget{

    public SimpleMulti(List<Entities<? extends Entity>> entitiesList){
        for (Entities<?> e:entitiesList){
            register(e);
        }
    }
}
