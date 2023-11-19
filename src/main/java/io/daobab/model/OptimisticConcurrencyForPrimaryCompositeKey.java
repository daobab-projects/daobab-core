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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface OptimisticConcurrencyForPrimaryCompositeKey<E extends PrimaryCompositeKey<E, K>, F, R extends RelatedTo, K extends Composite> extends OptimisticConcurrencyIndicator<E>, ParserGeneral {

    @Override
    default E handleOCC(QueryTarget target, E entityToUpdate) {
        if (target == null) throw new MandatoryTargetException();
        if (entityToUpdate == null) throw new MandatoryEntity();
        Object dbval = entityToUpdate.findRelatedOne(target, entityToUpdate, new WhereAnd().greater(getOCCColumn(), (R) entityToUpdate));

        if (dbval != null) throw new DaobabException("Optimistic Concurrency Error");

        F cellval = getOCCColumn().getValue((R) dbval);

        Object val;

        if (getOCCColumn().getFieldClass().isAssignableFrom(BigDecimal.class)) {
            if (cellval == null) {
                val = new BigDecimal(0);
            } else {
                BigDecimal bd = (BigDecimal) cellval;
                val = bd.add(BigDecimal.ONE);
            }

        } else if (getOCCColumn().getFieldClass().isAssignableFrom(Long.class)) {
            if (cellval == null) {
                val = 0L;
            } else {
                Long bd = (Long) cellval;
                val = bd + 1;
            }

        } else if (getOCCColumn().getFieldClass().isAssignableFrom(Integer.class)) {
            if (cellval == null) {
                val = 0;
            } else {
                Integer bd = (Integer) cellval;
                val = bd + 1;
            }

        } else if (getOCCColumn().getFieldClass().isAssignableFrom(Double.class)) {
            if (cellval == null) {
                val = 0D;
            } else {
                Double bd = (Double) cellval;
                val = bd + 1;
            }

        } else if (getOCCColumn().getFieldClass().isAssignableFrom(Timestamp.class)) {
            val = toCurrentTimestampTimeZoneDefault();
        } else if (getOCCColumn().getFieldClass().isAssignableFrom(Date.class)) {
            val = new Date();
        } else if (getOCCColumn().getFieldClass().isAssignableFrom(java.sql.Date.class)) {
            val = toCurrentDateSQL();
        } else {
            throw new DaobabException("Optimistic Concurrency Control Exception for Entity " + target.getEntityName(entityToUpdate.entityClass()) + " pointed OCC column has to be either Number or Date related type ");
        }
        return (E) getOCCColumn().setValue((R) entityToUpdate, (F) val);
    }

    Column<E, F, R> getOCCColumn();

}
