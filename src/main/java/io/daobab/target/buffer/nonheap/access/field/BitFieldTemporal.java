package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.result.predicate.WherePredicate;
import io.daobab.result.predicate.comparable.MatchNotNullComparable;
import io.daobab.result.predicate.time.*;
import io.daobab.statement.condition.Operator;

import java.time.temporal.Temporal;
import java.util.function.Function;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class BitFieldTemporal<V extends Temporal & Comparable<? super V>> implements BitField<V> {


    public Function<V, WherePredicate<V>> getPredicate(Operator operator) {
        switch (operator) {
            case EQ:
                return MatchEQTemporal::new;
            case IS_NULL:
                return v -> new MatchIsNullTemporal();
            case GT:
                return MatchGTTemporal::new;
            case LT:
                return MatchLTTemporal::new;
            case LTEQ:
                return MatchLTEQTemporal::new;
            case GTEQ:
                return MatchGTEQTemporal::new;
            case NOT_NULL:
                return v -> new MatchNotNullComparable();
            case IN:
                return MatchINTemporal::new;
            case NOT_EQ:
                return MatchNotEQTemporal::new;
            case NOT_IN:
                return MatchNotINTemporal::new;
            case LIKE:
                return getPredicateLike();
            case NOT_LIKE:
                return getPredicateNotLike();
            default:
                return MatchFalseTemporal::new;
        }
    }

    public Function<V, WherePredicate<V>> getPredicateLike() {
        return MatchFalseTemporal::new;
    }

    public Function<V, WherePredicate<V>> getPredicateNotLike() {
        return MatchFalseTemporal::new;
    }


}
