package io.daobab.result;

import io.daobab.error.NullFunction;
import io.daobab.model.Entity;
import io.daobab.target.buffer.nonheap.NonHeapEntities;
import io.daobab.target.buffer.single.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface EntitiesProvider<E extends Entity> {

    Class<E> getEntityClass();

    Entities<E> findMany();

    default NonHeapEntities<E> toNonHeap() {
        return new NonHeapEntities<>(findMany());
    }

    Optional<E> findFirst();

    default boolean exists() {
        return findFirst().isPresent();
    }

    default E findOne() {
        return findFirst().orElse(null);
    }

    default <M> FieldsProvider<M> map(Function<? super E, M> mapper) {
        if (mapper == null) throw new NullFunction();
        Entities<E> res = findMany();

        List<M> rv = new ArrayList<>();
        res.forEach(t -> rv.add(mapper.apply(t)));

        return new FieldsBuffer<>(rv);
    }

}
