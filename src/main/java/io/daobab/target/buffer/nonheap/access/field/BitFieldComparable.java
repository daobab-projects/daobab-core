package io.daobab.target.buffer.nonheap.access.field;

import io.daobab.result.predicate.WherePredicate;
import io.daobab.result.predicate.comparable.*;
import io.daobab.statement.condition.Operator;

import java.util.function.Function;

public abstract class BitFieldComparable<V extends Comparable<V>> implements BitField<V> {


    public Function<V, WherePredicate<V>> getPredicate(Operator operator) {
        switch (operator) {
            case EQ:
                return MatchEQComparable::new;
            case IS_NULL:
                return v -> new MatchIsNullComparable<>();
            case GT:
                return MatchGTComparable::new;
            case LT:
                return MatchLTComparable::new;
            case LTEQ:
                return MatchLTEQComparable::new;
            case GTEQ:
                return MatchGTEQComparable::new;
            case NOT_NULL:
                return v -> new MatchNotNullComparable<>();
            case IN:
                return MatchINComparable::new;
            case NOT_EQ:
                return MatchNotEQComparable::new;
            case NOT_IN:
                return MatchNotINComparable::new;
            case LIKE:
                return getPredicateLike();
            case NOT_LIKE:
                return getPredicateNotLike();
            default:
                return MatchFalseComparable::new;
        }
    }

    public Function<V, WherePredicate<V>> getPredicateLike() {
        return MatchFalseComparable::new;
    }

    public Function<V, WherePredicate<V>> getPredicateNotLike() {
        return MatchFalseComparable::new;
    }


}
