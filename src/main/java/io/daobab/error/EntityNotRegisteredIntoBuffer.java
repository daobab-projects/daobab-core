package io.daobab.error;

import io.daobab.model.Entity;
import io.daobab.target.buffer.multi.MultiEntity;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class EntityNotRegisteredIntoBuffer extends DaobabException {

    private static final long serialVersionUID = 1L;

    public EntityNotRegisteredIntoBuffer(Class<? extends Entity> clazz, Class<? extends MultiEntity> bufferClass) {
        super("Entity " + clazz.getName() + " is not registered into buffer " + bufferClass.getName());
    }

}
