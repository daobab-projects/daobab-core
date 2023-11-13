package io.daobab.target.buffer.nonheap;

import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.query.base.Query;
import io.daobab.result.predicate.AlwaysTrue;
import io.daobab.result.predicate.GeneralWhereAndPlate;
import io.daobab.result.predicate.GeneralWhereOrPlate;
import io.daobab.statement.where.base.Where;
import io.daobab.statement.where.base.WhereBase;
import io.daobab.target.buffer.single.Entities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class PlateBufferIndexed {

    protected final List<Plate> plateList;
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected PlateBufferIndexed() {
        this(new ArrayList<>());
    }

    protected PlateBufferIndexed(List<Plate> entities) {
        plateList = entities;
    }

    protected <E extends Entity> PlateBufferIndexed(Entities<E> entities) {
        plateList = new ArrayList<>(entities.size());
        for (E entity : entities) {
            Plate plate = new Plate(entity);
            plateList.add(plate);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S135", "java:S3358", "java:S3776"})
    public static List<Plate> finalFilter(List<Plate> entities, Query<?, ?, ?> query) {
        int counter = 0;

        List<Plate> rv = new ArrayList<>();
        Where<?> wrapper = query.getWhereWrapper();
        boolean useWhere = wrapper != null;

        boolean useLimit = query.getOrderBy() == null && query.getLimit() != null;

        int offset = !useLimit ? 0 : query.getLimit().getOffset();
        int limit = !useLimit ? 0 : query.getLimit().getLimit() > 0 ? (offset + query.getLimit().getLimit()) : Integer.MAX_VALUE;

        Predicate<Object> generalPredicate;
        if (useWhere) {
            if (WhereBase.OR.equals(wrapper.getRelationBetweenExpressions())) {
                GeneralWhereOrPlate genPred = new GeneralWhereOrPlate<>(wrapper);
                generalPredicate = genPred.isEmpty() ? new AlwaysTrue() : genPred;
            } else {
                GeneralWhereAndPlate genPred = new GeneralWhereAndPlate<>(wrapper);
                generalPredicate = genPred.isEmpty() ? new AlwaysTrue() : genPred;
            }
        } else {
            generalPredicate = new AlwaysTrue();
        }

        if (useLimit) {
            for (Plate entity : entities) {
                if (generalPredicate.test(entity)) {
                    counter++;
                    if (counter < offset) {
                        continue;
                    } else if (counter >= limit) {
                        break;
                    }
                    rv.add(entity);
                }
            }
        } else {
            entities.stream().filter(generalPredicate).forEach(rv::add);
        }
        return rv;
    }

    public <E extends Entity> List<Plate> filter(Query<E, ?, ?> query) {
        return finalFilter(plateList, query);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlateBufferIndexed that = (PlateBufferIndexed) o;
        if (!Objects.equals(plateList.size(), that.plateList.size())) return false;
        if (!plateList.isEmpty()) {
            Plate plate = plateList.get(0);
            Plate thatPlate = that.plateList.get(0);
            return plate.equals(thatPlate);
        }
        return Objects.equals(plateList, that.plateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateList, plateList.size(), plateList.isEmpty() ? 0 : plateList.get(0).hashCode());
    }
}
