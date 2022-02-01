package io.daobab.target.multi;

import io.daobab.model.Entity;
import io.daobab.result.Entities;

import java.util.List;

public class SimpleMulti extends MultiEntityTarget{

    public SimpleMulti(List<Entities<? extends Entity>> entitiesList){
        for (Entities<?> e:entitiesList){
            register(e);
        }
    }
}
