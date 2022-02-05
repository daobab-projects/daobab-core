package io.daobab.target.buffer.query;

import io.daobab.error.ColumnMandatory;
import io.daobab.model.Column;
import io.daobab.model.Dual;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.dummy.DummyColumnTemplate;
import io.daobab.query.base.QueryType;
import io.daobab.query.marker.ColumnOrQuery;
import io.daobab.result.FieldsProvider;
import io.daobab.statement.condition.Count;
import io.daobab.statement.function.type.DummyColumnRelation;
import io.daobab.statement.inner.InnerQueryFieldsProvider;
import io.daobab.statement.inner.InnerQueryFields;
import io.daobab.target.buffer.BufferQueryTarget;

import java.util.List;
import java.util.Map;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public final class BufferQueryField<E extends Entity, F> extends BufferQueryBase<E, BufferQueryField<E, F>> implements InnerQueryFieldsProvider<E, F>, FieldsProvider<F>, ColumnOrQuery<E, F, EntityRelation> {

    @SuppressWarnings("unused")
    private BufferQueryField() {
    }

    public BufferQueryField(BufferQueryTarget target, Column<E, F, ?> column) {
        if (column == null) throw new ColumnMandatory();
        init(target, column.getInstance());
        fields.add(getInfoColumn(column));
    }

    public BufferQueryField(BufferQueryTarget target, Map<String, Object> remote) {
        fromRemote(target, remote);
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
        return new InnerQueryFields<>(findMany());
    }

    public long countBy(Count cnt) {
        setTempCount(cnt);
        if (cnt.countEntities()) {
            return findMany().size();
        } else {
            //TODO: czy tu ma byc _unique??
//                return resultFieldUniqueSetFromCache((Column<E, F, ?>) cnt.getFieldForPointer(1)).size();
            return 0;
        }
    }

    @Override
    public long countAny() {
        return countBy(Count.field(getFields().get(0).getColumn()));
    }

    @Override
    public List<F> findMany() {
        return getTarget().readFieldList(modifyQuery(this));
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.FIELD;
    }

}
