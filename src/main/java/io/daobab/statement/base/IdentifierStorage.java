package io.daobab.statement.base;

import io.daobab.error.DaobabException;
import io.daobab.error.NullEntityException;
import io.daobab.model.Column;
import io.daobab.model.ColumnHaving;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO identifier storage for sql querues
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public final class IdentifierStorage {

    static final String identifier = "ihs";
    private final AtomicInteger count = new AtomicInteger();

    private final Map<String, String> queryIdentifiers = new HashMap<>();

    private final Map<String, String> joinClauseIdentifiers = new HashMap<>();

    private final List<String> queryEntities = new ArrayList<>();

    public void registerIdentifiers(String... entities) {
        if (entities == null) throw new NullEntityException();

        for (String entityName : entities) {
            getIdentifierFor(entityName);
        }

    }

    public void registerIdentifiers(Collection<String> entityNames) {
        if (entityNames == null) return;

        for (String entityName : entityNames) {
            getIdentifierFor(entityName);
        }
    }

    public StringBuilder getIdentifierForColumn(Column<?, ?, ?> field) {
        StringBuilder sb = new StringBuilder();
        if (field == null) return sb;
        if (field instanceof ColumnHaving) {
            sb.append(field.getColumnName());
        } else {
            sb.append(getIdentifierFor(field.getEntityName()))
                    .append(".")
                    .append(field.getColumnName());
        }
        return sb;
    }

    public String getIdentifierFor(String entityName) {

        if (entityName == null) throw new DaobabException("Entity name must be provided");

        String entityIdentifier = queryIdentifiers.get(entityName);
        if (entityIdentifier == null) {
            entityIdentifier = identifier + count.incrementAndGet();
            queryIdentifiers.put(entityName, entityIdentifier);
            getQueryEntities().add(entityName);
        }

        return entityIdentifier;
    }

    public boolean isEntityInJoinClause(String entityName) {
        return joinClauseIdentifiers.containsKey(entityName);
    }

    public void registerIdentifierForJoinClause(String entityName) {

        if (entityName == null) throw new DaobabException("Entity name must be provided.");

        String entityIdentifier = queryIdentifiers.get(entityName);
        if (entityIdentifier == null) {
            entityIdentifier = identifier + count.incrementAndGet();
            queryIdentifiers.put(entityName, entityIdentifier);
            getQueryEntities().add(entityName);
        }
        joinClauseIdentifiers.put(entityName, entityName);
    }

    public List<String> getQueryEntities() {
        return queryEntities;
    }


}
