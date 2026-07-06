package io.daobab.statement.base;

import io.daobab.converter.TypeConverter;
import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryEntity;
import io.daobab.model.Column;
import io.daobab.model.ColumnHaving;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.frozen.DaoParam;
import io.daobab.target.database.query.frozen.ParameterInjectionPoint;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Identifier storage for sql queries
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public final class IdentifierStorage {

    static final String IDENTIFIER = "ihs";
    private final AtomicInteger count = new AtomicInteger();
    private final Map<String, String> queryIdentifiers = new HashMap<>();
    private final Map<String, String> joinIdentifiers = new HashMap<>();
    private final List<String> queryEntities = new ArrayList<>();
    private final List<ParameterInjectionPoint> queryParameters = new ArrayList<>();

    private final Map<String, Column> identifiedColumnByAsKey = new HashMap<>();

    private List<Object> boundParameters = new ArrayList<>();
    private boolean parametersShared = false;
    private boolean inlineParameters = false;

    public void registerIdentifiers(String... entities) {
        if (entities == null) throw new MandatoryEntity();

        for (String entityName : entities) {
            getIdentifierFor(entityName);
        }
    }

    public void registerIdentifiers(Collection<String> entityNames) {
        if (entityNames == null) return;
        entityNames.forEach(this::getIdentifierFor);
    }

    public void registerParameter(DaoParam param, TypeConverter typeConverter) {
        if (param == null) return;
        queryParameters.add(new ParameterInjectionPoint(param,typeConverter));
    }

    public StringBuilder getIdentifierForColumn(QueryTarget target, Column<?, ?, ?> field) {
        StringBuilder sb = new StringBuilder();
        if (field == null) return sb;
        if (field instanceof ColumnHaving) {
            sb.append(field.getColumnName());
        } else {
            sb.append(getIdentifierFor(target.getEntityName(field.entityClass())))
                    .append(".")
                    .append(field.getColumnName());
        }
        return sb;
    }

    public StringBuilder getIdentifierForColumn(QueryTarget target, Column<?, ?, ?> field, boolean useAliases) {
        if (useAliases) {
            return getIdentifierForColumn(target, field);
        }
        StringBuilder sb = new StringBuilder();
        if (field == null) return sb;
        sb.append(field.getColumnName());
        return sb;
    }


    public String getIdentifierFor(String entityName) {
        if (entityName == null) throw new DaobabException("Entity name must be provided");

        String entityIdentifier = queryIdentifiers.get(entityName);
        if (entityIdentifier == null) {
            entityIdentifier = IDENTIFIER + count.incrementAndGet();
            queryIdentifiers.put(entityName, entityIdentifier);
            getQueryEntities().add(entityName);
        }

        return entityIdentifier;
    }

    public boolean isEntityInJoinClause(String entityName) {
        return joinIdentifiers.containsKey(entityName);
    }

    public void registerIdentifierForJoinClause(String entityName) {
        if (entityName == null) throw new DaobabException("Entity name must be provided.");

        String entityIdentifier = queryIdentifiers.get(entityName);
        if (entityIdentifier == null) {
            entityIdentifier = IDENTIFIER + count.incrementAndGet();
            queryIdentifiers.put(entityName, entityIdentifier);
            queryEntities.add(entityName);
        }

        joinIdentifiers.put(entityName, entityIdentifier);
    }

    public List<String> getQueryEntities() {
        return queryEntities;
    }


    public List<ParameterInjectionPoint> getQueryParameters() {
        return queryParameters;
    }

    public void addBoundParameter(Object value) {
        boundParameters.add(value);
    }

    public List<Object> getBoundParameters() {
        return boundParameters;
    }

    /**
     * Clears the bound parameters before a top level query generation.
     * Does nothing if this storage shares the parameter list of an outer query,
     * so that an inner query generation never wipes already collected parameters.
     */
    public void clearBoundParameters() {
        if (!parametersShared) {
            boundParameters.clear();
        }
    }

    public boolean isInlineParameters() {
        return inlineParameters;
    }

    /**
     * When enabled, values are rendered as SQL literals instead of '?' placeholders.
     * Used for frozen queries which keep the whole SQL as text.
     */
    public void setInlineParameters(boolean inlineParameters) {
        this.inlineParameters = inlineParameters;
    }

    /**
     * Makes the given sub query storage collect bound parameters into this storage list,
     * keeping the parameter order consistent with the placeholders order in the final SQL.
     */
    public void shareParametersWith(IdentifierStorage subQueryStorage) {
        subQueryStorage.boundParameters = this.boundParameters;
        subQueryStorage.parametersShared = true;
        subQueryStorage.inlineParameters = this.inlineParameters;
    }

    public void addColumnIdentifiedAsKey(String identifier, Column column) {
        identifiedColumnByAsKey.put(identifier, column);
    }

    public Column getColumnByIdentifier(String identifier) {
        return identifiedColumnByAsKey.get(identifier);
    }

}
