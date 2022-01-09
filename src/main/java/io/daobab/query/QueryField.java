package io.daobab.query;

import io.daobab.error.ColumnMandatory;
import io.daobab.error.TargetNoCacheNoEntityManagerException;
import io.daobab.model.*;
import io.daobab.model.dummy.DummyColumnTemplate;
import io.daobab.query.base.Query;
import io.daobab.query.base.QueryJoin;
import io.daobab.query.base.QueryType;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.query.marker.ManyCellsProvider;
import io.daobab.statement.condition.Count;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.function.type.DummyColumnRelation;
import io.daobab.statement.inner.InnerQueryField;
import io.daobab.statement.inner.InnerSelectManyCells;
import io.daobab.target.QueryTarget;
import io.daobab.target.database.DataBaseTarget;

import java.util.*;

//TODO: czy count nie powinien byc ograniczony do jednego wejsciowego pola? I czy wogóle powinien tu byc

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public final class QueryField<E extends Entity, F> extends Query<E, QueryField<E, F>> implements InnerQueryField<E, F>, ManyCellsProvider<F>, QueryJoin<QueryField<E, F>>, ColumnOrQuery<E,F,EntityRelation> {


    @SuppressWarnings("unused")
    private QueryField() {
    }

    public QueryField(QueryTarget target, Column<E, F, ?> column) {
        if (column == null) throw new ColumnMandatory();
        init(target, column.getInstance());
        fields.add(getInfoColumn(column));
    }

    public QueryField(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    @SuppressWarnings("unchecked")
    public QueryField(String nativeQuery, QueryTarget target, Column<E, ?, ?> column) {
        this(target,(Column<E, F, ?>)column);
        this._nativeQuery = nativeQuery;
    }

    @SuppressWarnings("rawtypes")
    public DummyColumnRelation<Dual, String, EntityRelation> as(String asName) {
        return new DummyColumnRelation<>(this, DummyColumnTemplate.dummyColumn(asName));
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public <F1> DummyColumnRelation<Dual, F1, EntityRelation> as(String asName, Class<F1> clazz) {
        return new DummyColumnRelation<>(this, DummyColumnTemplate.createDummyColumn(new Dual(), clazz, asName));
    }


    @Override
    public InnerSelectManyCells<E, F> innerResult() {
        if (getFields() == null) {
            //TODO: Decide what there: exception or null?
        }

        if (this.getTarget().isBuffer()) {
            return new InnerSelectManyCells<>(findMany());
        } else {
            return new InnerSelectManyCells<>(this);
        }
    }

    public long countBy(Count cnt) {
        setTempCount(cnt);
        if (getTarget().isBuffer()) {

            if (cnt.countEntities()) {
                return findMany().size();
            } else {
                //TODO: czy tu ma byc _unique??
//                return resultFieldUniqueSetFromCache((Column<E, F, ?>) cnt.getFieldForPointer(1)).size();
                return 0;
            }

        }
        if (getTarget().isConnectedToDatabase()) {
            DataBaseTarget emprov = (DataBaseTarget) getTarget();
            return emprov.count(this);

        }
        throw new TargetNoCacheNoEntityManagerException(getTarget());
    }

    /**
     * Count all record
     * Equivalent of count(*)
     *
     * @return
     */
    @Override
    public long countAny() {
        return countBy(Count.field(getFields().get(0).getColumn()));
    }

    @Override
    public List<F> findMany() {
        return getTarget().readFieldList(modifyQuery());
    }

    @Override
    public Optional<F> findFirst() {
        return Optional.ofNullable(getTarget().readField(modifyQuery()));
    }

    @Override
    public Query getSelect() {
        return this;
    }

    @Override
    public boolean isResultCached() {
        return getTarget().isBuffer();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.FIELD;
    }

    @Override
    public String toSqlQuery() {
        return getTarget().toSqlQuery(this);
    }
}
