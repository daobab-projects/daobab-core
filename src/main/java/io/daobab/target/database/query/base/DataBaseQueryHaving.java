package io.daobab.target.database.query.base;

import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.query.base.QueryHaving;

import io.daobab.target.database.query.DataBaseQueryBase;
import io.daobab.target.database.query.frozen.DaoParam;

import java.util.Collection;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface DataBaseQueryHaving<Q extends DataBaseQueryBase> extends QueryHaving<Q> {


//    default Q havingAnd(UnaryOperator<DataBaseHavingAnd> condition) {
//        DataBaseHavingAnd and = new DataBaseHavingAnd();
//        and = condition.apply(and);
//        setHavingWrapper(and);
//        return (Q) this;
//    }

//    default Q havingOR(Supplier<HavingOr> wrapper) {
//        setHavingWrapper(wrapper.get());
//        return (Q) this;
//    }

//    @SuppressWarnings("java:S1845")
//    default Q havingOr(UnaryOperator<HavingOr> condition) {
//        HavingOr or = new HavingOr();
//        or = condition.apply(or);
//        setHavingWrapper(or);
//        return (Q) this;
//    }

//    default Q havingNOT(Supplier<HavingNot> wrapper) {
//        setHavingWrapper(wrapper.get());
//        return (Q) this;
//    }

//    @SuppressWarnings("java:S1845")
//    default Q havingNot(UnaryOperator<HavingNot> condition) {
//        HavingNot not = new HavingNot();
//        not = condition.apply(not);
//        setHavingWrapper(not);
//        return (Q) this;
//    }

    default <E extends Entity, F, R extends EntityRelation> Q havingEqual(Column<E, F, R> column, DaoParam val) {
        setHavingWrapper(new DataBaseHavingAnd().equal(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotEqual(Column<E, F, R> column, DaoParam val) {
        setHavingWrapper(new DataBaseHavingAnd().notEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingGreater(Column<E, F, R> column, DaoParam val) {
        setHavingWrapper(new DataBaseHavingAnd().greater(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingGreaterOrEqual(Column<E, F, R> column, DaoParam val) {
        setHavingWrapper(new DataBaseHavingAnd().greaterOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLess(Column<E, F, R> column, DaoParam val) {
        setHavingWrapper(new DataBaseHavingAnd().less(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLessOrEqual(Column<E, F, R> column, DaoParam val) {
        setHavingWrapper(new DataBaseHavingAnd().lessOrEqual(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingLike(Column<E, F, R> column, DaoParam val) {
        setHavingWrapper(new DataBaseHavingAnd().like(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingInCollectionParam(Column<E, F, R> column, Collection<DaoParam> val) {
        setHavingWrapper(new DataBaseHavingAnd().inFieldsParam(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotInCollectionParam(Column<E, F, R> column, Collection<DaoParam> val) {
        setHavingWrapper(new DataBaseHavingAnd().notInFieldsParam(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingInFieldsParam(Column<E, F, R> column, Collection<DaoParam> val) {
        setHavingWrapper(new DataBaseHavingAnd().inFieldsParam(column, val));
        return (Q) this;
    }

    default <E extends Entity, F, R extends EntityRelation> Q havingNotInFieldsParam(Column<E, F, R> column, Collection<DaoParam> val) {
        setHavingWrapper(new DataBaseHavingAnd().notInFieldsParam(column, val));
        return (Q) this;
    }

    default <F> Q havingBetween(Column<?, F, ?> column, DaoParam valueFrom, DaoParam valueTo) {
        setHavingWrapper(new DataBaseHavingAnd().between(column, valueFrom, valueTo));
        return (Q) this;
    }


}
