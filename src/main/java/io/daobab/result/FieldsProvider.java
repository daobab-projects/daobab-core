package io.daobab.result;

import io.daobab.error.NullConsumer;
import io.daobab.error.NullFunction;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface FieldsProvider<F> {

    List<F> findMany();

    Optional<F> findFirst();

    long countAny();

    default boolean exists() {
        return findFirst().isPresent();
    }

    default F findOne() {
        return findFirst().orElse(null);
    }

    default FieldsProvider<F> filter(Predicate<? super F> predicate) {
        if (predicate == null) throw new NullFunction();
        List<F> rv = findMany()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
        return new FieldsBuffer<>(rv);
    }

    default <M> FieldsBuffer<M> map(Function<? super F, M> mapper) {
        if (mapper == null) throw new NullFunction();
        return new FieldsBuffer<>(findMany()
                .stream()
                .map(mapper)
                .collect(Collectors.toList()));
    }

}
