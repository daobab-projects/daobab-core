package io.daobab.model;

import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryEntity;
import io.daobab.error.MandatoryTargetException;
import io.daobab.parser.ParserGeneral;
import io.daobab.statement.where.WhereAnd;
import io.daobab.target.database.QueryTarget;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Optimistic concurrency for an entity with a {@link PrimaryKey}: {@link #handleOCC} verifies no newer version of
 * the row exists (by comparing the {@link #getOCCColumn() version column}) and then bumps the version - a number
 * incremented by one, or the current timestamp/date for a temporal column. The {@code PrimaryKey.update} methods
 * call it automatically when the entity implements this interface.
 *
 * @param <E> the entity type (a {@link PrimaryKey})
 * @param <F> the version column type
 * @param <R> the relation type
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface OptimisticConcurrencyForPrimaryKey<E extends Entity & PrimaryKey<E, ?, ?>, F, R extends RelatedTo> extends OptimisticConcurrencyIndicator<E>, ParserGeneral {

    /**
     * Verifies no newer version of the row exists and bumps the version column, returning the entity with the
     * new version set.
     *
     * @throws DaobabException on a concurrency conflict, or when the version column is neither a number nor a date
     */
    @Override
    default E handleOCC(QueryTarget target, E entityToUpdate) {
        if (target == null) throw new MandatoryTargetException();
        if (entityToUpdate == null) throw new MandatoryEntity();
        Object dbval = entityToUpdate.findRelatedOne(target, entityToUpdate.colID(), new WhereAnd().greater(getOCCColumn(), (R) entityToUpdate));

        if (dbval != null) throw new DaobabException("Optimistic Concurrency Error");

        F cellval = entityToUpdate.findRelatedOne(target, getOCCColumn());

        Object val;

        Class<?> occFieldClass = getOCCColumn().getFieldClass();

        if (BigDecimal.class.isAssignableFrom(occFieldClass)) {
            if (cellval == null) {
                val = BigDecimal.ZERO;
            } else {
                BigDecimal bd = (BigDecimal) cellval;
                val = bd.add(BigDecimal.ONE);
            }

        } else if (Long.class.isAssignableFrom(occFieldClass)) {
            if (cellval == null) {
                val = 0L;
            } else {
                Long bd = (Long) cellval;
                val = bd + 1;
            }

        } else if (Integer.class.isAssignableFrom(occFieldClass)) {
            if (cellval == null) {
                val = 0;
            } else {
                Integer bd = (Integer) cellval;
                val = bd + 1;
            }

        } else if (Double.class.isAssignableFrom(occFieldClass)) {
            if (cellval == null) {
                val = 0D;
            } else {
                Double bd = (Double) cellval;
                val = bd + 1;
            }

            //the java.sql types are subclasses of java.util.Date, so they have to be matched before it
        } else if (Timestamp.class.isAssignableFrom(occFieldClass)) {
            val = toCurrentTimestampTimeZoneDefault();
        } else if (java.sql.Date.class.isAssignableFrom(occFieldClass)) {
            val = toCurrentDateSQL();
        } else if (Date.class.isAssignableFrom(occFieldClass)) {
            val = new Date();
        } else {
            throw new DaobabException("Optimistic Concurrency Control Exception for Entity " + target.getEntityName(entityToUpdate.entityClass()) + " pointed OCC column has to be either Number or Date related type ");
        }
        return (E) getOCCColumn().setValue((R) entityToUpdate, (F) val);
    }

    /**
     * The version (optimistic-concurrency) column.
     */
    Column<E, F, R> getOCCColumn();

}
