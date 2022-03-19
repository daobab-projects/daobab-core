package io.daobab.target.buffer.single;

import io.daobab.converter.JsonHandler;
import io.daobab.model.Entity;
import io.daobab.query.base.Query;
import io.daobab.result.EntitiesProvider;
import io.daobab.target.Target;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.multi.MultiEntityTarget;
import io.daobab.target.buffer.multi.SimpleMultiTarget;
import io.daobab.target.buffer.noheap.NoHeapEntities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides cached (in memory) entities
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface Entities<E extends Entity> extends EntitiesProvider<E>, Serializable, List<E>, Target, Cloneable, JsonHandler, BufferQueryTarget {

    default E findOne() {
        return findFirst().orElse(null);
    }

    Entities<E> calculateIndexes();

    Class<E> getEntityClass();

    @Override
    default NoHeapEntities<E> toNoHeap() {
        return new NoHeapEntities<>(this);
    }

    Entities<E> copy();

    Entities<E> clone();

    void refreshImmediately();

    List<E> filter(Query<E, ?, ?> query);

    default MultiEntityTarget joinEntities(Entities<? extends Entity>... targets) {
        List<Entities<? extends Entity>> list = new ArrayList<>();
        list.add(this);
        for (Entities<? extends Entity> e : targets) {
            list.add(e);
        }
        return new SimpleMultiTarget(list);
    }

}
