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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public final class IdentifierStorage {

    static final String identifier = "ihs";
    private final AtomicInteger count = new AtomicInteger();

    private final Map<String, String> storage = new HashMap<>();
    //Dao ktore zostana dodane w klauzurze join
    private final Map<String, String> joinClauseStorage = new HashMap<>();

    private final List<String> allDao = new LinkedList<>();

    public void registerIdentifiers(String... entities) {
        if (entities == null) throw new NullEntityException();

        for (String dao : entities) {
            createAndGetIdentifierFor(dao);
        }

    }

    public void registerIdentifiers(Collection<String> col) {
        if (col == null) return;

        for (String dao : col) {
            createAndGetIdentifierFor(dao);
        }
    }

    public String createAndGetIdentifierFor(String entityname) {
        return getIdentifierFor(entityname);
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

    public StringBuilder getIdentifierForColumnNoEntity(Column<?, ?, ?> field) {
        StringBuilder sb = new StringBuilder();
        if (field == null) return sb;
        if (field instanceof ColumnHaving) {
            sb.append(field.getColumnName());
        } else {
            sb.append(field.getColumnName());
        }
        return sb;
    }

    public String getIdentifierForColumnInJoinClause(Column<?, ?, ?> field) {
        if (field == null) return "";
        return getIdentifierForJoinClause(field.getEntityName()) + "." + field.getColumnName();
    }

//    public final String getIdentifierForEntity(Entity entity) {
//        getIdentifierFor(entity.getEntityName());
//    }

    public final String getIdentifierFor(String entityname) {

        if (entityname == null) throw new DaobabException("Entity name must be provided");

        String entityidentifier = storage.get(entityname);
        if (entityidentifier == null) {
            entityidentifier = identifier + count.incrementAndGet();
            storage.put(entityname, entityidentifier);
            getAllDao().add(entityname);
        }


        return entityidentifier;
    }

    public boolean isDaoInJoinClause(String entityname) {
        for (String key : joinClauseStorage.keySet()) {
            if (key.equals(entityname)) {
                return true;
            }
        }
        return false;
    }

    public final String getIdentifierForJoinClause(String entityname) {

        if (entityname == null) throw new DaobabException("Entity name must be provided.");

        String daoidentifier = storage.get(entityname);
        if (daoidentifier == null) {
            daoidentifier = identifier + count.incrementAndGet();
            storage.put(entityname, daoidentifier);
            getAllDao().add(entityname);
        }
        joinClauseStorage.put(entityname, entityname);


        return daoidentifier;
    }

    public List<String> getAllDao() {
        return allDao;
    }

    public List<String> getAllDaoButNotFromJoin() {
        List<String> rv = new LinkedList<>();
        for (Iterator<String> it = getAllDao().iterator(); it.hasNext(); ) {
            String d = it.next();

            if (!isDaoInJoinClause(d)) {
                rv.add(d);
            }
        }
        return rv;

    }

}
