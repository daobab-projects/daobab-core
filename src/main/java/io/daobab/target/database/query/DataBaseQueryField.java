package io.daobab.target.database.query;

import io.daobab.error.MandatoryColumn;
import io.daobab.model.*;
import io.daobab.model.dummy.DummyColumnTemplate;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.query.base.QueryType;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.result.FieldsProvider;
import io.daobab.statement.function.type.DummyColumnRelation;
import io.daobab.statement.inner.InnerQueryFields;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.frozen.FrozenDataBaseQueryField;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public final class DataBaseQueryField<E extends Entity, F> extends DataBaseQueryBase<E, DataBaseQueryField<E, F>> implements InnerQueryFieldsProvider<E, F>, QueryExpressionProvider<E>, FieldsProvider<F>, ColumnOrQuery<E, F, RelatedTo> {


    @SuppressWarnings("unused")
    private DataBaseQueryField() {
    }

    public DataBaseQueryField(QueryTarget target, Column<E, F, ?> column) {
        if (column == null) throw new MandatoryColumn();
        init(target, column.getInstance());
        fields.add(getInfoColumn(column));
    }

    public DataBaseQueryField(QueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
    }

    @SuppressWarnings("rawtypes")
    public DummyColumnRelation<Dual, String, RelatedTo> as(String asName) {
        return new DummyColumnRelation<>(this, DummyColumnTemplate.dummyColumn(asName));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <F1> DummyColumnRelation<Dual, F1, RelatedTo> as(String asName, Class<F1> clazz) {
        return new DummyColumnRelation<>(this, DummyColumnTemplate.createDummyColumn(new Dual(), clazz, asName));
    }

    @Override
    public InnerQueryFields<E, F> innerResult() {
        return new InnerQueryFields<>(this);
    }

    @Override
    public List<F> findMany() {
        return getTarget().readFieldList(modifyQuery(this));
    }

    @Override
    public Optional<F> findFirst() {
        return Optional.ofNullable(getTarget().readField(modifyQuery(this)));
    }

    @Override
    public DataBaseQueryField<E, F> getInnerQuery() {
        return this;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.FIELD;
    }

    public TableColumn getSelectedColumn() {
        return fields.get(0);
    }

    public FrozenDataBaseQueryField<E,F> freezeQuery() {
        return new FrozenDataBaseQueryField<>(this);
    }

}
