package io.daobab.result;

import io.daobab.error.NullConsumer;
import io.daobab.error.NullFunction;
import io.daobab.model.Entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface EntitiesProvider<E extends Entity> {

    Class<E> getEntityClass();

    Entities<E> findMany();

    default EntityByteBuffer<E> createByteBuffer() {
        return new EntityByteBuffer<>(findMany());
    }

    Optional<E> findFirst();

    long countAny();

    default boolean exists() {
        return findFirst().isPresent();
    }

    default E findOne() {
        return findFirst().orElse(null);
    }

    default void forEach(Consumer<? super E> consumer) {
        if (consumer == null) throw new NullConsumer();
        findMany().forEach(consumer);
    }

    default <M> FieldsProvider<M> map(Function<? super E, M> mapper) {
        if (mapper == null) throw new NullFunction();
        Entities<E> res = findMany();

        List<M> rv = new LinkedList<>();
        res.forEach(t -> rv.add(mapper.apply(t)));

        return new FieldsBuffer<>(rv);
    }

}
