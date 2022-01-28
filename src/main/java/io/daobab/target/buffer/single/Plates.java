package io.daobab.target.buffer.single;

import io.daobab.converter.JsonHandler;
import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.model.TableColumn;
import io.daobab.query.base.Query;
import io.daobab.target.buffer.BufferQueryTarget;
import io.daobab.target.buffer.query.BufferQueryPlate;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Provides cached (in memory) entities
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public interface Plates extends Serializable, List<Plate>, Cloneable, JsonHandler, BufferQueryTarget {

    Plates findMany();

    Optional<Plate> findFirst();

    Plates calculateIndexes();

    Plates sanitise(List<TableColumn> tableColumns);

    long countAny();

    default boolean exists() {
        return findFirst().isPresent();
    }

    default Plate findOne() {
        return findFirst().orElse(null);
    }

    Plates copy();

    Plates clone();

    Plates orderAndLimit(Query<?, ?, ?> query);

    Plates limit(BufferQueryPlate query);

//    @SuppressWarnings("rawtypes")
//    default <F, R extends EntityRelation> List<F> getFieldList(Column<?, F, R> column) {
//        return stream().map(m -> m.getValue(column)).filter(Objects::nonNull).collect(Collectors.toList());
//    }

    <E extends Entity> List<Plate> filter(Query<E, ?, ?> query);

//    default Plates filterBySize(int from, int to) {
//        if (to == 0) return new PlateBuffer(Collections.emptyList());
//
//        int colSize = this.size();
//        if (to > colSize) {
//            to = colSize;
//        }
//        return new PlateBuffer(this.findMany().subList(from, to));
//    }

//    @SuppressWarnings("rawtypes")
//    default <F, R extends EntityRelation> List<F> getUniqueFieldList(Column<?, F, R> column) {
//        if (column == null) return Collections.emptyList();
//
//        Set<F> set = stream().map(m -> m.getValue(column)).filter(Objects::nonNull).collect(Collectors.toSet());
//        return new ArrayList<>(set);
//    }

}
