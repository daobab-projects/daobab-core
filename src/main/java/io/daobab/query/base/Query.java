package io.daobab.query.base;

import io.daobab.error.AttemptToSetWhereClaseSecondTimeException;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.NullEntityException;
import io.daobab.error.TargetMandatoryException;
import io.daobab.generator.DictRemoteKey;
import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.Column;
import io.daobab.model.EnhancedEntity;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;
import io.daobab.query.marschal.Marschaller;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.statement.condition.*;
import io.daobab.statement.function.base.ColumnFunction;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;
import io.daobab.statement.where.base.WhereBase;
import io.daobab.target.QueryTarget;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.UnaryOperator;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked","rawtypes"})
public abstract class Query<E extends Entity, Q extends Query> implements QueryJoin<Q>, QueryWhere<Q>, QueryOrder<Q>, QueryLimit<Q>, QueryHaving<Q>, QuerySetOperator<Q>, RemoteQuery<Q>, ILoggerBean {

    public List<Column<?, ?, ?>> _groupBy = new LinkedList<>();
    public boolean _unique = false;
    public boolean _calcJoins = false;
    private boolean logQueryEnabled = false;
    protected Order orderBy;
    protected boolean refreshCasheWhenNoResult = false;
    protected String _nativeQuery;
    private List<TableColumn> fields = new ArrayList<>();
    private List<JoinWrapper> joins = new ArrayList<>();
    private IdentifierStorage identifierStorage;
    private QueryTarget target;
    private String entityName;
    private Class entityClass;
    private Where whereWrapper;
    private Having havingWrapper;
    private Count _count;
    private Limit limit;
    private String identifier;


    private String sentQuery;

    private List<SetOperator> setOperatorList;

    public List<SetOperator> getSetOperatorList() {
        return setOperatorList;
    }

    public void addSetOperator(SetOperator union) {
        if (setOperatorList == null) {
            setOperatorList = new LinkedList<>();
        }
        this.setOperatorList.add(union);
    }


    protected Query() {
    }

    public static <E extends Entity, RV extends Query> RV clone(Query<E, RV> from, Query<E, RV> to) {
        if (from == null || to == null) return null;
        to.fields.addAll(from.fields);
        to.joins.addAll(from.joins);
        to._groupBy.addAll(from._groupBy);
        to.identifierStorage = from.identifierStorage;
        to.target = from.target;
        to.entityName = from.entityName;
        to.entityClass = from.entityClass;
        to.whereWrapper = from.whereWrapper; //TODO
        to.havingWrapper = from.havingWrapper; //TODO
        to._count = from._count;
        to.limit = from.limit;
        to._unique = from._unique;
        to._calcJoins = from._calcJoins;
        to.refreshCasheWhenNoResult = from.refreshCasheWhenNoResult;
        to._nativeQuery = from._nativeQuery;
        to.identifier = from.identifier;
       // to.logQueryConsumer = from.logQueryConsumer;

        return (RV) to;
    }


    public Q doIfElse(boolean active, UnaryOperator<Q> logicIf, UnaryOperator<Q> logicElse) {
        if (active) {
            return logicIf.apply((Q) this);
        } else {
            return logicElse.apply((Q) this);
        }
    }

    public Q doIf(boolean active, UnaryOperator<Q> logic) {
        if (active) return logic.apply((Q) this);
        return (Q) this;
    }




    public Q groupBy(Column<?, ?, ?>... columns) {
        if (columns == null || columns.length == 0) return (Q) this;
        _groupBy.addAll(Arrays.asList(columns));
        return (Q) this;
    }

    public List<Column<?,?,?>> getGroupBy() {
        return _groupBy;
    }

    protected void init(QueryTarget target, String entityName) {
        if (target == null) throw new TargetMandatoryException();
        setTarget(target);
        setEntityName(entityName);
        IdentifierStorage storage = new IdentifierStorage();
        storage.registerIdentifiers(getEntityName());
        setIdentifierStorage(storage);
    }


    protected void init(QueryTarget target, Entity entity) {
        if (target == null) throw new TargetMandatoryException();
        if (entity == null) throw new NullEntityException();
        setTarget(target);
        setEntityName(entity.getEntityName());
        setEntityClass(entity.getEntityClass());
        IdentifierStorage storage = new IdentifierStorage();
        storage.registerIdentifiers(getEntityName());
        setIdentifierStorage(storage);
    }



    @Override
    public Where getWhereWrapper() {
        return whereWrapper;
    }

    public void setWhereWrapper(Where whereWrapper) {
        if (this.whereWrapper != null) {
            throw new AttemptToSetWhereClaseSecondTimeException();
        }
        this.whereWrapper = whereWrapper;
    }

    public List<TableColumn> getFields() {
        return fields;
    }

    protected void setFields(List<TableColumn> fields) {
        this.fields = fields;
    }

    /**
     * Get all DAO related withoutTransactionTo this statement
     *
     */
    public Set<String> getAllEntitiesRelated() {
        Set<String> rv = new HashSet<>();
        if (getEntityName() != null) rv.add(getEntityName());
        if (getWhereWrapper() != null) {
            rv.addAll(getWhereWrapper().getAllDaoInWhereClause());
        }

        if (getFields() == null) return rv;

        for (TableColumn df : getFields()) {
            String ndao = df.getColumn().getEntityName();
            if (ndao != null) {
                rv.add(ndao);
            }
        }
        return rv;
    }

    public IdentifierStorage getIdentifierStorage() {
        return identifierStorage;
    }

    protected void setIdentifierStorage(IdentifierStorage identifierStorage) {
        this.identifierStorage = identifierStorage;
    }

    @Override
    public Q where(Where whereWrapper) {
        if (whereWrapper == null) return (Q) this;
        if (this.whereWrapper == null) {
            whereWrapper.optimize();
            setWhereWrapper(whereWrapper);
            return (Q) this;
        }
        WhereAnd merged = new WhereAnd();
        if (this.whereWrapper instanceof WhereAnd) {
            merged = (WhereAnd) this.whereWrapper;
            merged.optimize();
            merged.and(whereWrapper);
        } else {
            merged.and(this.whereWrapper).and(whereWrapper);
        }
        this.whereWrapper = merged;
        return (Q) this;
    }

    public QueryTarget getTarget() {
        return target;
    }

    protected void setTarget(QueryTarget target) {
        if (target == null) throw new TargetMandatoryException();
        this.target = target;
    }

    @Override
    public Q having(Having having) {
        having.optimize();
        setHavingWrapper(having);
        return (Q) this;
    }

    public Order getOrderBy() {
        return orderBy;
    }

    public Q orderBy(Order orderBy) {
        this.orderBy = orderBy;
        return (Q) this;
    }


    public Count getCount() {
        return _count;
    }

    protected void setTempCount(Count c) {
        this._count = c;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setTempLimit(Limit limit) {
        this.limit = limit;
    }



    public void handleException(String statement, Throwable t) {
        if (getTarget() == null) {
            t.printStackTrace();
        }
        getTarget().getLog().error("Query: " + statement + " produces error. ", t);
    }


    public Q logQuery() {
        logQueryEnabled=true;
        return (Q) this;
    }

    public boolean isLogQueryEnabled(){
        return logQueryEnabled;
    }


    public Q distinct() {
        _unique = true;
        return (Q) this;
    }


    public Q smartJoins() {
        _calcJoins = true;
        return (Q) this;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public Having getHavingWrapper() {
        return havingWrapper;
    }

    public void setHavingWrapper(Having havingWrapper) {
        this.havingWrapper = havingWrapper;
    }

    public List<JoinWrapper> getJoins() {
        return joins;
    }

    public void setJoins(List<JoinWrapper> joins) {
        this.joins = joins;
    }

    @Override
    public Map<String, Object> toRemote(boolean singleResult) {
        Map<String, Object> rv = new HashMap<>();
        rv.put(DictRemoteKey.QUERY_CLASS, this.getClass().getName());
        rv.put(DictRemoteKey.SINGLE_RESULT, singleResult);
        if (fields != null && !fields.isEmpty())
            rv.put(DictRemoteKey.FIELDS, Marschaller.marschallColumnList(fields));
        if (entityName != null) rv.put(DictRemoteKey.ENTITY_NAME, entityName);
        if (entityClass != null) rv.put(DictRemoteKey.ENTITY_CLASS, entityClass.getName());
        if (_unique) rv.put(DictRemoteKey.UNIQUE, _unique);
        if (_calcJoins) rv.put(DictRemoteKey.SMART_JOINS, _calcJoins);
        if (getWhereWrapper() != null && !getWhereWrapper().isEmpty())
            rv.put(DictRemoteKey.WHERE, getWhereWrapper().toMap());
        if (getHavingWrapper() != null && !getHavingWrapper().isEmpty())
            rv.put(DictRemoteKey.HAVING, getHavingWrapper().toMap());
        if (getCount() != null) rv.put(DictRemoteKey.COUNT, getCount().getCountMap());
        if (getLimit() != null) rv.put(DictRemoteKey.LIMIT, getLimit().getLimitMap());
        if (getOrderBy() != null) rv.put(DictRemoteKey.ORDER, getOrderBy().toMap());
        if (_groupBy != null && !_groupBy.isEmpty()) rv.put(DictRemoteKey.GROUP_BY, _groupBy);
        if (joins != null && !joins.isEmpty()) {
            Map<String, Object> joinMap = new HashMap<>();
            for (int i = 0; i < joins.size(); i++) {
                joinMap.put("" + i, joins.get(i).toMap());
            }
            rv.put(DictRemoteKey.JOINS, joinMap);
        }
        if (identifier != null) rv.put(DictRemoteKey.IDENTIFIER, identifier);
        return rv;
    }


    public void fromRemote(QueryTarget target, Map<String, Object> rv) {
        setTarget(target);
        if (!this.getClass().getName().equals(rv.get(DictRemoteKey.QUERY_CLASS))) {
            System.out.println("classname is wrong"); //TODO: exception
        }

        Object where = rv.get(DictRemoteKey.WHERE);
        Object fields = rv.get(DictRemoteKey.FIELDS);
        Object entityName = rv.get(DictRemoteKey.ENTITY_NAME);
        Object entityClass = rv.get(DictRemoteKey.ENTITY_CLASS);
        Object unique = rv.get(DictRemoteKey.UNIQUE);
        Object cache = rv.get(DictRemoteKey.CACHE);

        Entity ent = Marschaller.fromRemote(target, (String) entityName);

        if (ent!= null) setEntityClass(ent.getEntityClass());
        setEntityName((String) entityName);

        if (unique!= null) _unique = "true".equals(unique);


        if (where != null) {
            setWhereWrapper(WhereBase.fromRemote(target, (Map<String, Object>) where));
        }
        if (fields != null) {
            setFields(Marschaller.unMarschallColumns((Map<String, Object>) fields, target));
        }


        IdentifierStorage storage = new IdentifierStorage();
        storage.registerIdentifiers(getEntityName());
        setIdentifierStorage(storage);

        setIdentifier((String) rv.get(DictRemoteKey.IDENTIFIER));
    }

    public String geNativeQuery() {
        return _nativeQuery;
    }

    protected Q modifyQuery() {
        setIdentifier(UUID.randomUUID().toString());
        if (getEntityClass() == null) return (Q) this;

        if (EnhancedEntity.class.isAssignableFrom(getEntityClass())) {
            try {
                EnhancedEntity emb = (EnhancedEntity) getEntityClass().getDeclaredConstructor().newInstance();
                if (emb.joinedColumns() != null) {
                    for (Column<?, ?, ?> c : emb.joinedColumns()) {
                        for (TableColumn col : emb.columns()) {
                            if (col.getColumn().equalsColumn(c)) {
                                getFields().add(col);
                            }
                        }
                    }
                }
                return (Q) emb.enhanceQuery(this);
            } catch (Exception e) {
                throw new DaobabEntityCreationException(getEntityClass(), e);
            }
        }
        return (Q) this;
    }

    @Override
    public Logger getLog() {
        return getTarget().getLog();
    }

    public String getIdentifier() {
        return identifier;
    }

    public Q setIdentifier(String identifier) {
        this.identifier = identifier;
        return (Q) this;
    }

    public abstract QueryType getQueryType();

    protected TableColumn getInfoColumn(ColumnFunction column) {
        if (column == null) return null;
        return new TableColumn(column);
    }

    protected TableColumn getInfoColumn(Column column) {
        if (column == null) return null;
        if (column instanceof ColumnFunction) {
            return new TableColumn(column);
        }

        for (TableColumn ic : column.getInstance().columns()) {
            if (ic.getColumn().equalsColumn(column)) {
                return ic;
            }
        }

        return null;
    }

    public abstract String toSqlQuery();


    public String getSentQuery() {
        return sentQuery;
    }

    public void setSentQuery(String sentQuery) {
        this.sentQuery = sentQuery;
    }
}
