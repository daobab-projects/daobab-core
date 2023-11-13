package io.daobab.target.database.query.frozen;

import io.daobab.error.DaobabException;
import io.daobab.model.Entity;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.query.DataBaseQueryBase;
import org.slf4j.Logger;

import java.time.temporal.TemporalAmount;
import java.util.List;

public abstract class FrozenDataBaseQueryBase<E extends Entity, Q extends DataBaseQueryBase<E, ?>, B extends FrozenDataBaseQueryBase<E, Q, ?>> implements FrozenQueryProvider {

    private final String frozenQuery;

    protected final QueryTarget target;

    private String identifier;

    private boolean cacheUsed;

    protected TemporalAmount cachedPeriod;

    private String entityName;
    private String sentQuery;

    protected CacheManager cacheManager = new CacheManager();

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    public Q getOriginalQuery() {
        return originalQuery;
    }

    private final Q originalQuery;


    public List<ParameterInjectionPoint> getQueryParametersInjectionPoints() {
        return queryParametersInjectionPoints;
    }

    private final List<ParameterInjectionPoint> queryParametersInjectionPoints;

    protected FrozenDataBaseQueryBase(Q originalQuery) {
        this.target = originalQuery.getTarget();
        IdentifierStorage identifierStorage = new IdentifierStorage();
        this.frozenQuery = originalQuery.getTarget().toSqlQuery(originalQuery, identifierStorage);
        this.queryParametersInjectionPoints = identifierStorage.getQueryParameters();
        this.originalQuery = originalQuery;
    }

    public String getFrozenQuery() {
        return frozenQuery;
    }

    protected void validateEmptyParameters() {
        if (areParametersNeeded()) {
            throw new DaobabException("This query needs %s parameters and got none.", getQueryParametersInjectionPoints().size());
        }
    }

    protected boolean areParametersNeeded() {
        return !getQueryParametersInjectionPoints().isEmpty();
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getSentQuery() {
        return sentQuery;
    }

    public void setSentQuery(String sentQuery) {
        this.sentQuery = sentQuery;
    }

    @Override
    public Logger getLog() {
        return target.getLog();
    }


    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public boolean isCacheUsed() {
        return cacheUsed;
    }

    @SuppressWarnings("unchecked")
    public B cacheResultsForPeriod(TemporalAmount period) {
        if (areParametersNeeded()) {
            throw new DaobabException("Cache cannot be used for parameters needed query");
        }
        this.cachedPeriod = period;
        this.cacheUsed = true;
        return (B) this;
    }
}
