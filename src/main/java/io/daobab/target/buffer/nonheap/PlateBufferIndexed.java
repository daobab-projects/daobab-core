package io.daobab.target.buffer.nonheap;

import io.daobab.model.Entity;
import io.daobab.model.Plate;
import io.daobab.model.PrimaryKey;
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
import java.util.function.Predicate;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class PlateBufferIndexed {

    protected final List<Plate> plateList;
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    private boolean primaryKey = false;

    protected PlateBufferIndexed() {
        this(new ArrayList<>());
    }

    protected PlateBufferIndexed(List<Plate> entities) {
        if (!entities.isEmpty()) {
            Plate entity = entities.get(0);
            setPrimaryKey(entity instanceof PrimaryKey);
        }
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

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

}
