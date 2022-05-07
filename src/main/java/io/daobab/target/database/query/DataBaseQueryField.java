package io.daobab.target.database.query;

import io.daobab.error.ColumnMandatory;
import io.daobab.model.Column;
import io.daobab.model.Dual;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.dummy.DummyColumnTemplate;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.query.base.QueryType;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.result.FieldsProvider;
import io.daobab.statement.condition.Count;
import io.daobab.statement.function.type.DummyColumnRelation;
import io.daobab.statement.inner.InnerQueryFields;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.target.database.QueryTarget;

import java.util.List;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public final class DataBaseQueryField<E extends Entity, F> extends DataBaseQueryBase<E, DataBaseQueryField<E, F>> implements InnerQueryFieldsProvider<E, F>, QueryExpressionProvider<E>, FieldsProvider<F>, ColumnOrQuery<E, F, EntityRelation> {


    @SuppressWarnings("unused")
    private DataBaseQueryField() {
    }

    public DataBaseQueryField(QueryTarget target, Column<E, F, ?> column) {
        if (column == null) throw new ColumnMandatory();
        init(target, column.getInstance());
        fields.add(getInfoColumn(column));
    }

    public DataBaseQueryField(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    @SuppressWarnings("unchecked")
    public DataBaseQueryField(String nativeQuery, QueryTarget target, Column<E, ?, ?> column) {
        this(target, (Column<E, F, ?>) column);
        this._nativeQuery = nativeQuery;
    }

    @SuppressWarnings("rawtypes")
    public DummyColumnRelation<Dual, String, EntityRelation> as(String asName) {
        return new DummyColumnRelation<>(this, DummyColumnTemplate.dummyColumn(asName));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F1> DummyColumnRelation<Dual, F1, EntityRelation> as(String asName, Class<F1> clazz) {
        return new DummyColumnRelation<>(this, DummyColumnTemplate.createDummyColumn(new Dual(), clazz, asName));
    }


    @Override
    public InnerQueryFields<E, F> innerResult() {
        return new InnerQueryFields<>(this);
    }

    @Override
    public long countAny() {
        setTempCount(Count.field(getFields().get(0).getColumn()));
        return getTarget().count(this);
    }

    @Override
    public List<F> findMany() {
        return getTarget().readFieldList(modifyQuery(this));
    }

    @Override
    public DataBaseQueryField<E, F> getInnerQuery() {
        return this;
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
