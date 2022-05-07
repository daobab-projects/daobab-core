package io.daobab.result;

import io.daobab.error.NullFunction;
import io.daobab.model.Plate;
import io.daobab.target.buffer.single.Plates;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface PlateProvider {

    Plates findMany();

    Optional<Plate> findFirst();

    default boolean exists() {
        return findFirst().isPresent();
    }

    default Plate findOne() {
        return findFirst().orElse(null);
    }

    long countAny();


    default void forEach(Consumer<Plate> consumer) {
        findMany().forEach(consumer);
    }


    default FieldsProvider<Plate> filter(Predicate<Plate> predicate) {
        if (predicate == null) throw new NullFunction();
        List<Plate> rv = new ArrayList<>();
        findMany().forEach(r -> {
            if (predicate.test(r)) {
                rv.add(r);
            }
        });
        return new FieldsBuffer<>(rv);
    }


    default <M> FieldsProvider<M> map(Function<Plate, M> mapper) {
        List<Plate> res = findMany();
        if (mapper == null) return null;

        List<M> rv = new ArrayList<>();
        res.forEach(t -> rv.add(mapper.apply(t)));

        return new FieldsBuffer<>(rv);
    }
}
