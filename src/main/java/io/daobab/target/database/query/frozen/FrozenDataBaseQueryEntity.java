package io.daobab.target.database.query.frozen;

import io.daobab.converter.json.conversion.EntityJsonConversion;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;
import io.daobab.query.base.QueryType;
import io.daobab.result.EntitiesProvider;
import io.daobab.target.buffer.single.Entities;
import io.daobab.target.database.converter.TypeConverterPrimaryKeyToManyCache;
import io.daobab.target.database.converter.TypeConverterPrimaryKeyToOneCache;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.converter.type.TypeConverterPKBased;
import io.daobab.target.database.converter.type.TypeConverterPKBasedList;
import io.daobab.target.database.query.DataBaseQueryEntity;
import io.daobab.target.protection.OperationType;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FrozenDataBaseQueryEntity<E extends Entity> extends FrozenDataBaseQueryBase<E, DataBaseQueryEntity<E>, FrozenDataBaseQueryEntity<E>> implements EntitiesProvider<E> {

    private final Class<E> entityClass;

    private final DatabaseTypeConverter<?, ?>[] typeConverters;

    private final EntityJsonConversion<E> entityJsonConversion;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public FrozenDataBaseQueryEntity(DataBaseQueryEntity<E> originalQuery) {
        super(originalQuery);
        this.entityClass = originalQuery.getEntityClass();
        target.getAccessProtector().validateEntityAllowedFor(originalQuery.getEntityName(), OperationType.READ);
        target.getAccessProtector().removeViolatedInfoColumns3(originalQuery.getFields(), OperationType.READ);

        List<TableColumn> columns = getOriginalQuery().getFields();
        Column[] columnsArray = new Column[columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            columnsArray[i] = columns.get(i).getColumn();
        }
        typeConverters = new DatabaseTypeConverter<?, ?>[columns.size()];

        E entityInstance;
        try {
            entityInstance = originalQuery.getEntityClass().getDeclaredConstructor().newInstance();

            for (int i = 0; i < columns.size(); i++) {
                DatabaseTypeConverter<?, ?> typeConverter = target.getConverterManager().getConverter(columnsArray[i]).orElse(null);

                if (typeConverter instanceof TypeConverterPKBased) {
                    typeConverters[i] = new TypeConverterPrimaryKeyToOneCache((TypeConverterPKBased) typeConverter);
                } else if (typeConverter instanceof TypeConverterPKBasedList) {
                    typeConverters[i] = new TypeConverterPrimaryKeyToManyCache(getOriginalQuery().getTarget(), (TypeConverterPKBasedList) typeConverter, entityInstance, (Entity) columnsArray[i].getInnerTypeClass().getDeclaredConstructor().newInstance());
                } else {
                    typeConverters[i] = typeConverter;
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new DaobabEntityCreationException(this.entityClass, e);
        }
        entityJsonConversion=target.getJsonConverterManager().getEntityJsonConverter(entityInstance);
    }

    public EntitiesProvider<E> withParameters(Object... parameters) {
        return withParameters(Arrays.asList(parameters));
    }

    public EntitiesProvider<E> withParameters(List<Object> parameters) {
        FrozenDataBaseQueryEntity<E> frozenDataBaseQueryEntity = this;
        return new EntitiesProvider<E>() {

            @Override
            public Class<E> getEntityClass() {
                return entityClass;
            }

            @Override
            public Entities<E> findMany() {
                return target.readEntityList(frozenDataBaseQueryEntity, parameters, typeConverters);
            }

            @Override
            public Optional<E> findFirst() {
                return Optional.ofNullable(target.readEntity(frozenDataBaseQueryEntity, parameters, typeConverters));
            }

        };
    }

    @Override
    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public Entities<E> findMany() {
        validateEmptyParameters();
        if (isCacheUsed()) {
            return cacheManager.getManyContent(getFrozenQuery(), cachedPeriod, () -> target.readEntityList(this, Collections.emptyList(), typeConverters));
        }
        return target.readEntityList(this, Collections.emptyList(), typeConverters);
    }

    @Override
    public Optional<E> findFirst() {
        validateEmptyParameters();
        if (isCacheUsed()) {
            return cacheManager.getSingleContent(getFrozenQuery(), cachedPeriod, () -> Optional.ofNullable(target.readEntity(this, Collections.emptyList(), typeConverters)));
        }
        return Optional.ofNullable(target.readEntity(this, Collections.emptyList(), typeConverters));
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ENTITY;
    }


    public String findOneAsJson() {
        StringBuilder sb = new StringBuilder();
        entityJsonConversion.toJson(sb, findOne());
        return sb.toString();
    }

    public String findManyAsJson() {
        StringBuilder sb = new StringBuilder();
        entityJsonConversion.toJson(sb, "",findMany());
        return sb.toString();
    }

}
