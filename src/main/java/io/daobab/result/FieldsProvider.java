package io.daobab.result;

import io.daobab.error.NullFunction;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface FieldsProvider<F> {

    List<F> findMany();

    default Optional<F> findFirst() {
        List<F> elements = findMany();
        return elements.isEmpty() ? Optional.empty() : Optional.of(elements.get(0));
    }

    default long countAny() {
        return findMany().size();
    }

//    default boolean exists() {
//        return findFirst().isPresent();
//    }

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
