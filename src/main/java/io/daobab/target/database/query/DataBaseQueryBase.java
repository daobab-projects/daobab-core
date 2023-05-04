package io.daobab.target.database.query;

import io.daobab.error.AttemptToSetWhereClauseSecondTimeException;
import io.daobab.error.DaobabEntityCreationException;
import io.daobab.error.MandatoryTargetException;
import io.daobab.error.NullEntityException;
import io.daobab.generator.DictRemoteKey;
import io.daobab.internallogger.ILoggerBean;
import io.daobab.model.Column;
import io.daobab.model.EnhancedEntity;
import io.daobab.model.Entity;
import io.daobab.model.TableColumn;
import io.daobab.query.base.*;
import io.daobab.query.marschal.Marshaller;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.statement.condition.*;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.base.Where;
import io.daobab.statement.where.base.WhereBase;
import io.daobab.target.database.QueryTarget;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.UnaryOperator;


/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class DataBaseQueryBase<E extends Entity, Q extends DataBaseQueryBase> implements Query<E, QueryTarget, Q>, QueryJoin<Q>, QueryWhere<Q>, QueryOrder<Q>, QueryLimit<Q>, QueryHaving<Q>, QuerySetOperator<Q>, QueryGroupBy<Q>, RemoteQuery<Q>, ILoggerBean {

    public List<Column<?, ?, ?>> _groupBy = new ArrayList<>();
    public String _groupByAlias = null;
    public boolean _unique = false;
    public boolean _calcJoins = false;
    protected Order orderBy;
    protected String _nativeQuery;
    protected List<TableColumn> fields = new ArrayList<>();
    private boolean logQueryEnabled = false;
    private List<JoinWrapper> joins = new ArrayList<>();
    private IdentifierStorage identifierStorage;
    private QueryTarget target;
    private String entityName;
    private Class<E> entityClass;
    private Where whereWrapper;
    private Having havingWrapper;
    private Count _count;
    private Limit limit;
    private String identifier;
    private String sentQuery;
    private List<SetOperator> setOperatorList;
    private int deep = 1;

    protected DataBaseQueryBase() {
    }

    public static <E extends Entity, RV extends DataBaseQueryBase> RV clone(DataBaseQueryBase<E, RV> from, DataBaseQueryBase<E, RV> to) {
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
        to._nativeQuery = from._nativeQuery;
        to.identifier = from.identifier;
        // to.logQueryConsumer = from.logQueryConsumer;

        return (RV) to;
    }

    public List<SetOperator> getSetOperatorList() {
        return setOperatorList;
    }

    public void addSetOperator(SetOperator union) {
        if (setOperatorList == null) {
            setOperatorList = new LinkedList<>();
        }
        this.setOperatorList.add(union);
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

    @Override
    public Q groupBy(String alias) {
        this._groupByAlias = alias;
        return (Q) this;
    }

    @Override
    public Q groupBy(Column<?, ?, ?>... columns) {
        if (columns == null || columns.length == 0) return (Q) this;
        getGroupBy().clear();
        getGroupBy().addAll(Arrays.asList(columns));
        return (Q) this;
    }

    public List<Column<?, ?, ?>> getGroupBy() {
        return _groupBy;
    }

    protected void init(QueryTarget target, String entityName) {
        if (target == null) throw new MandatoryTargetException();
        setTarget(target);
        setEntityName(entityName);
        IdentifierStorage storage = new IdentifierStorage();
        storage.registerIdentifiers(getEntityName());
        setIdentifierStorage(storage);
    }


    protected void init(QueryTarget target, Entity entity) {
        if (target == null) throw new MandatoryTargetException();
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
            throw new AttemptToSetWhereClauseSecondTimeException();
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
        if (target == null) throw new MandatoryTargetException();
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
        logQueryEnabled = true;
        return (Q) this;
    }

    public boolean isLogQueryEnabled() {
        return logQueryEnabled;
    }


    public Q distinct() {
        _unique = true;
        return (Q) this;
    }

    @Override
    public boolean isUnique() {
        return _unique;
    }

    @Override
    public boolean isJoin() {
        return _calcJoins;
    }

    @Override
    public String getGroupByAlias() {
        return _groupByAlias;
    }

    public boolean isGroupBy() {
        return !_groupBy.isEmpty() && getGroupByAlias() != null;
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

    public Class<E> getEntityClass() {
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
            rv.put(DictRemoteKey.FIELDS, Marshaller.marshalColumnList(fields));
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
                joinMap.put(String.valueOf(i), joins.get(i).toMap());
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
        Object remoteEntityName = rv.get(DictRemoteKey.ENTITY_NAME);
        Object remoteEntityClass = rv.get(DictRemoteKey.ENTITY_CLASS);
        Object unique = rv.get(DictRemoteKey.UNIQUE);
        Object cache = rv.get(DictRemoteKey.CACHE);

        Entity ent = Marshaller.fromRemote(target, (String) remoteEntityName);

        if (ent != null) setEntityClass(ent.getEntityClass());
        setEntityName((String) remoteEntityName);

        if (unique != null) _unique = "true".equals(unique);


        if (where != null) {
            setWhereWrapper(WhereBase.fromRemote(target, (Map<String, Object>) where));
        }
        if (fields != null) {
            setFields(Marshaller.unMarshallColumns((Map<String, Object>) fields, target));
        }


        IdentifierStorage storage = new IdentifierStorage();
        storage.registerIdentifiers(getEntityName());
        setIdentifierStorage(storage);

        setIdentifier((String) rv.get(DictRemoteKey.IDENTIFIER));
    }

    public String geNativeQuery() {
        return _nativeQuery;
    }

    protected <Q1 extends Query> Q1 modifyQuery(Q1 query) {
        query.setIdentifier(UUID.randomUUID().toString());
        if (query.getEntityClass() == null) return query;

        if (EnhancedEntity.class.isAssignableFrom(query.getEntityClass())) {
            try {
                EnhancedEntity emb = (EnhancedEntity) query.getEntityClass().getDeclaredConstructor().newInstance();
                if (emb.joinedColumns() != null) {
                    for (Column<?, ?, ?> joinedColumn : emb.joinedColumns()) {
                        for (TableColumn col : emb.columns()) {
                            if (col.getColumn().equalsColumn(joinedColumn)) {
                                query.getFields().add(col);
                            }
                        }
                    }
                }
                return (Q1) emb.enhanceQuery((Q) query);
            } catch (Exception e) {
                throw new DaobabEntityCreationException(query.getEntityClass(), e);
            }
        }
        return query;
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

        return target.getColumnsForTable(column.getInstance())
                .stream()
                .filter(tableColumn -> tableColumn.getColumn().equalsColumn(column))
                .findFirst()
                .orElse(null);

    }

    public String getSentQuery() {
        return sentQuery;
    }

    public void setSentQuery(String sentQuery) {
        this.sentQuery = sentQuery;
    }

    public <E1 extends Entity> Q from(E1 entity) {
        if (entity == null) throw new NullEntityException();
        setEntityName(entity.getEntityName());
        setEntityClass(entity.getEntityClass());
        setIdentifierStorage(new IdentifierStorage());
        getIdentifierStorage().registerIdentifiers(getEntityName());
        return (Q) this;
    }

    @SuppressWarnings("unused")
    public String toSqlQuery() {
        return getTarget().toSqlQuery(this);
    }

    public Q deepness(int deep) {
        this.deep = deep;
        return (Q) this;
    }

    public int getDeepness() {
        return deep;
    }

}
