package io.daobab.result.predicate;

import io.daobab.error.UnhandledOperator;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.RelatedTo;
import io.daobab.statement.condition.Operator;
import io.daobab.statement.where.base.Where;
import io.daobab.target.buffer.nonheap.NonHeapBuffer;
import io.daobab.target.buffer.nonheap.access.field.BitField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;

import static io.daobab.statement.where.base.WhereBase.OR;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class GeneralBitFieldWhereAnd<E> implements WherePredicate<Integer> {

    protected final NonHeapBuffer<E> nonHeapBuffer;
    protected transient Logger log = LoggerFactory.getLogger(this.getClass());
    protected Where wrapperWhere;
    protected List<Integer> skipSteps;
    protected List<WherePredicate<Object>> predicates = new ArrayList<>();
    //column order
    protected List<Integer> columnsBufferPositionInWhere = new ArrayList<>();
    protected List<Integer> columnsEntityPositionInWhere = new ArrayList<>();

    @SuppressWarnings({"rawtypes", "java:S135"})
    public GeneralBitFieldWhereAnd(NonHeapBuffer<E> nonHeapBuffer, Where wrapperWhere, List<Integer> skipSteps) {
        this.wrapperWhere = wrapperWhere;
        this.skipSteps = skipSteps == null ? new ArrayList<>() : skipSteps;
        this.nonHeapBuffer = nonHeapBuffer;

        for (int i = 1; i < wrapperWhere.getCounter(); i++) {

            if (skipSteps != null && skipSteps.contains(i)) {
                log.debug("Skipped step: " + i);
                continue;
            }
            predicates.add(matchPredicate(wrapperWhere, i));

            Where innerWhere = wrapperWhere.getInnerWhere(i);
            if (innerWhere != null) {
//                skipSteps.add(i);
                columnsBufferPositionInWhere.add(-1);
                columnsEntityPositionInWhere.add(-1);
                continue;
            }
//            wrapperWhere.getKeyForPointer(i);
            Column<Entity, Object, RelatedTo> keyFromWrapper = (Column<Entity, Object, RelatedTo>) wrapperWhere.getKeyForPointer(i);
            int columnOrder = nonHeapBuffer.getBufferPositionOfColumn(keyFromWrapper);
            columnsBufferPositionInWhere.add(columnOrder);
            columnsEntityPositionInWhere.add(nonHeapBuffer.getColumnIntoEntityPosition(keyFromWrapper));
        }

    }

    public boolean isEmpty() {
        return predicates.isEmpty();
    }

    @Override
    public boolean test(Integer entityPointer) {

        for (int i = 0; i < predicates.size(); i++) {

            if (skipSteps.contains(i)) {
                continue;
            }

            //if at least one record into AND clause if false, entity doesn't match
            if (!predicates.get(i).bitTest(nonHeapBuffer, entityPointer, columnsEntityPositionInWhere.get(i), columnsBufferPositionInWhere.get(i)))
                return false;
        }

        return true;
    }

    @Override
    public boolean bitTest(NonHeapBuffer buffer, int entityPosition, int columnPositionIntoEntity, int colPosition) {
        for (int i = 0; i < predicates.size(); i++) {

            if (skipSteps.contains(i)) {
                continue;
            }

            if (!predicates.get(i).bitTest(buffer, entityPosition, columnsEntityPositionInWhere.get(i), columnsBufferPositionInWhere.get(i)))
                return false;
        }

        return true;
    }

    @SuppressWarnings({"rawtypes", "java:S3776"})
    private WherePredicate matchPredicate(Where wrapper, int i) {

        Object valueFromWrapper = wrapper.getValueForPointer(i);
        Operator relation = wrapper.getRelationForPointer(i);

        //if Where has a second Where inside...
        Where where = wrapper.getInnerWhere(i);
        if (where != null) {
            Where wrval = (Where) valueFromWrapper;
            String innerRelation = wrval.getRelationBetweenExpressions();
            if (OR.equals(innerRelation)) {
                return new GeneralBitFieldWhereOr<>(nonHeapBuffer, wrval, null);
            }
            //for AND and NOT
            return new GeneralBitFieldWhereAnd<>(nonHeapBuffer, wrval, null);
        }

        Object keyFromWrapper = wrapper.getKeyForPointer(i);

        if (keyFromWrapper != null) {
            BitField<?> bitField = nonHeapBuffer.getBifFieldForColumn((Column) keyFromWrapper);
            Function function = bitField.getPredicate(relation);
            return (WherePredicate) function.apply(valueFromWrapper);
        }

        boolean numeric = (valueFromWrapper instanceof Number);
        boolean datetime = false;
        boolean timestamptime = false;
        Date dateValueFromWrapper = null;
        Timestamp timeStampValueFromWrapper = null;


        if (valueFromWrapper instanceof Collection) {
            return checkCollectionPredicate(relation, (Collection<Object>) valueFromWrapper);
        }

        if (!numeric) {
            if (valueFromWrapper instanceof Timestamp) {
                timestamptime = true;
                timeStampValueFromWrapper = (Timestamp) valueFromWrapper;
            } else if (valueFromWrapper instanceof Date) {
                datetime = true;
                dateValueFromWrapper = (Date) valueFromWrapper;
            }

        }
        if (datetime) {
            return matchDateComparationPredicate(relation, dateValueFromWrapper);
        } else if (timestamptime) {
            return matchTimestampComparationPredicate(relation, timeStampValueFromWrapper);
        } else if (numeric) {
            return matchNumberComparationPredicate((Number) valueFromWrapper, relation);


        } else if (Operator.EQ.equals(relation)) {
            if (valueFromWrapper == null) return new AlwaysFalse();
            return new MatchEQ(valueFromWrapper);//valueFromWrapper.equals(valueFromEntity);
        } else if (Operator.NOT_NULL.equals(relation)) {
            return new MatchNotNull();
        } else if (Operator.IS_NULL.equals(relation)) {
            return new MatchIsNull();
        } else if (Operator.NOT_EQ.equals(relation)) {
            if (valueFromWrapper == null) return new AlwaysFalse();
            return new MatchNotEQ(valueFromWrapper);
        } else if (Operator.LIKE.equals(relation)) {
            return new MatchLike(valueFromWrapper);
        }


        return new AlwaysFalse();
    }


    private WherePredicate<Object> matchDateComparationPredicate(Operator operator, Date dateValueFromWrapper) {

        switch (operator) {
            case EQ: {
                return new DateMatchEQ(dateValueFromWrapper);//dateValueFromWrapper.equals(dateValueFromEntity);
            }
            case NOT_EQ: {
                return new DateMatchNotEQ(dateValueFromWrapper);
            }
            case NOT_NULL: {
                return new MatchNotNull();
            }
            case IS_NULL: {
                return new MatchIsNull();
            }
            case GT: {
                return new DateMatchGT(dateValueFromWrapper);
            }
            case GTEQ: {
                return new DateMatchGTEQ(dateValueFromWrapper);
            }
            case LT: {
                return new DateMatchLT(dateValueFromWrapper);
            }
            case LTEQ: {
                return new DateMatchGTEQ(dateValueFromWrapper);
            }
            default: {
                throw new UnhandledOperator(operator);
            }
        }
    }


    private Timestamp toTimeZone(Timestamp timestamp, TimeZone timeZone) {
        if (timestamp == null) return null;
        return new Timestamp(timestamp.getTime() - timeZone.getOffset(timestamp.getTime()));
    }


    private WherePredicate<Object> matchNumberComparationPredicate(Number numericValueFromWrapper, Operator operator) {

        switch (operator) {
            case EQ:
                return new NumberMatchEQ(numericValueFromWrapper);
            case NOT_EQ:
                return new NumberMatchNotEQ(numericValueFromWrapper);
            case NOT_NULL:
                return new MatchNotNull();
            case IS_NULL:
                return new MatchIsNull();
            case GT:
                return new NumberMatchGT(numericValueFromWrapper);
            case GTEQ:
                return new NumberMatchGTEQ(numericValueFromWrapper);
            case LT:
                return new NumberMatchLT(numericValueFromWrapper);
            case LTEQ:
                return new NumberMatchLTEQ(numericValueFromWrapper);
            default:
                throw new UnhandledOperator(operator);
        }

    }

    private Date toTimeZone(Date date, TimeZone timeZone) {
        if (date == null) return null;
        if (timeZone == null) return date;
        return new Date(date.getTime() - timeZone.getOffset(date.getTime()));
    }


    private WherePredicate<Object> matchTimestampComparationPredicate(Operator operator, Timestamp dateValueFromWrapper) {
//		dateValueFromWrapper=toTimeZone(dateValueFromWrapper,TimeZone.getTimeZone("UTC"));


        switch (operator) {
            case EQ: {
                return new TimestampMatchEQ(dateValueFromWrapper);
            }
            case NOT_EQ: {
                return new TimestampMatchNotEQ(dateValueFromWrapper);
            }
            case NOT_NULL: {
                return new MatchNotNull();
            }
            case IS_NULL: {
                return new MatchIsNull();
            }
            case GT: {
                return new TimestampMatchGT(dateValueFromWrapper);
            }
            case GTEQ: {
                return new TimestampMatchGTEQ(dateValueFromWrapper);
            }
            case LT: {
                return new TimestampMatchLT(dateValueFromWrapper);
            }
            case LTEQ: {
                return new TimestampMatchLTEQ(dateValueFromWrapper);
            }
            default: {
                throw new UnhandledOperator(operator);
            }
        }
    }


    private WherePredicate<Object> checkCollectionPredicate(Operator operator, Collection<Object> values) {
        if (Operator.IN.equals(operator)) {
            if (values == null) return new AlwaysFalse();

            return new MatchIN(values);
        } else if (Operator.NOT_IN.equals(operator)) {
//            if (valueFromEntity == null) return false;
//			if (values==null) return true;

            return new MatchNotIN(values);
        }

        return new AlwaysFalse();
    }


}
