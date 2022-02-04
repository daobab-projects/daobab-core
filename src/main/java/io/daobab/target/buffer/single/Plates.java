package io.daobab.target.buffer.single;

import io.daobab.converter.JsonListHandler;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.Plate;
import io.daobab.target.buffer.query.BufferQueryPlate;
import io.daobab.query.base.Query;
import io.daobab.target.buffer.BufferQueryTarget;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides cached (in memory) entities
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface Plates extends Serializable, List<Plate>, Cloneable, JsonListHandler, BufferQueryTarget {

    Plates findMany();

    Optional<Plate> findFirst();

    Plates calculateIndexes();

    long countAny();

    default boolean exists() {
        return findFirst().isPresent();
    }

    default Plate findOne() {
        return findFirst().orElse(null);
    }

    Plates copy();

    Plates clone();

    Plates orderAndLimit(Query<?,?,?> query);

    Plates limit(BufferQueryPlate query);

    default <F, R extends EntityRelation> List<F> getFieldList(Column<?, F, R> col) {
        return stream().map(entity -> col.getValueOf((R) entity)).collect(Collectors.toList());
    }

    <E extends Entity> List<Plate> filter(Query<E, ?,?> query);


    default Plates filterBySize(int from, int to) {
        if (to == 0) return new PlateBuffer(Collections.emptyList());

        int colsize = this.size();
        if (to > colsize) {
            to = colsize;
        }

        return new PlateBuffer(this.findMany().subList(from, to));
    }


    default <F, R extends EntityRelation> List<F> getUniqueFieldList(Column<?, F, R> col) {
        if (col == null) return Collections.emptyList();

        //TODO: null check
        Set<F> set = stream().map(m -> col.getValueOf((R) m)).collect(Collectors.toSet());
        return new LinkedList<>(set);
    }

}
