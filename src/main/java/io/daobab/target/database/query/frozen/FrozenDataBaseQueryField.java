package io.daobab.target.database.query.frozen;

import io.daobab.error.DaobabException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.query.base.QueryType;
import io.daobab.result.FieldsProvider;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.query.DataBaseQueryField;
import io.daobab.target.protection.OperationType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FrozenDataBaseQueryField<E extends Entity, F> extends FrozenDataBaseQueryBase<E, DataBaseQueryField<E, F>, FrozenDataBaseQueryField<E, F>> implements FieldsProvider<F> {

    private final DatabaseTypeConverter<?, ?> typeConverter;
    private final Column<?, ?, ?> column;

    public FrozenDataBaseQueryField(DataBaseQueryField<E, F> originalQuery) {
        super(originalQuery);
        target.getAccessProtector().removeViolatedInfoColumns3(originalQuery.getFields(), OperationType.READ);
        if (originalQuery.getFields() == null || originalQuery.getFields().isEmpty()) {
            throw new DaobabException("Field query has no column to return");
        }
        column = originalQuery.getFields().get(0).getColumn();
        typeConverter = originalQuery.getTarget().getConverterManager().getConverter(column).orElse(null);
    }

    public FieldsProvider<F> withParameters(Object... parameters) {
        return withParameters(Arrays.asList(parameters));
    }

    public FieldsProvider<F> withParameters(List<Object> parameters) {
        FrozenDataBaseQueryField<E, F> frozenDataBaseQueryField = this;
        return new FieldsProvider<F>() {

            @Override
            public List<F> findMany() {
                return target.readFieldList(frozenDataBaseQueryField, parameters, column, typeConverter);
            }

            @Override
            public Optional<F> findFirst() {
                return Optional.ofNullable(target.readField(frozenDataBaseQueryField, parameters, column, typeConverter));
            }
        };
    }


    @Override
    public List<F> findMany() {
        validateEmptyParameters();
        if (isCacheUsed()) {
            return cacheManager.getManyContent(getFrozenQuery(), cachedPeriod, () -> target.readFieldList(this, Collections.emptyList(), column, typeConverter));
        }
        return target.readFieldList(this, Collections.emptyList(), column, typeConverter);
    }

    @Override
    public Optional<F> findFirst() {
        validateEmptyParameters();
        if (isCacheUsed()) {
            return Optional.ofNullable(cacheManager.getSingleContent(getFrozenQuery(), cachedPeriod, () -> target.readField(this, Collections.emptyList(), column, typeConverter)));
        }
        return Optional.ofNullable(target.readField(this, Collections.emptyList(), column, typeConverter));
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.FIELD;
    }

}
