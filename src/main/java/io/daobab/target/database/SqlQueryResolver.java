package io.daobab.target.database;

import io.daobab.dict.DictDatabaseType;
import io.daobab.model.IdGeneratorType;
import io.daobab.error.SqlInjectionDetected;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.model.EntityRelation;
import io.daobab.model.TableColumn;
import io.daobab.query.QueryDelete;
import io.daobab.query.QueryInsert;
import io.daobab.query.QueryUpdate;
import io.daobab.query.base.Query;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.result.EntitiesProvider;
import io.daobab.result.ManyCellsProvider;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.statement.condition.*;
import io.daobab.statement.function.base.*;
import io.daobab.statement.inner.InnerSelectManyCells;
import io.daobab.statement.inner.InnerSelectManyEntities;
import io.daobab.statement.join.JoinTracker;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.base.Where;
import io.daobab.target.Target;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public interface SqlQueryResolver extends QueryConsumer, DataBaseTargetLogic {

    String LINE_SEPARATOR = System.getProperty("line.separator");
    String LIMIT = " limit  ";
    String SPACE = " ";
    String APOSTROPHE = "'";
    String COMMA = ",";
    String DOT = ".";
    String COMMASPACE = ", ";
    String OPEN_BRACKET = "(";
    String SPACE_OPEN_BRACKET = " (";
    String CLOSED_BRACKET = ")";

    String getDataBaseProductName();

    default boolean useDeleteSyntaxWithTableIdentifier() {
        return false;
    }


    default <E extends Entity> String deleteQueryToExpression(QueryDelete<E> base) {

        IdentifierStorage storage = base.getIdentifierStorage();
        Target dbtarget = base.getTarget();
        StringBuilder sb = new StringBuilder();

        if (toNative(base, sb)) {
            return sb.toString();
        }

        if (base._calcJoins) {
            List<String> from = new ArrayList<>();
            from.add(base.getEntityName());
            base.setJoins(JoinTracker.calculateJoins(base.getTarget().getTables(), from, (base.getWhereWrapper() == null ? new HashSet<>() : base.getWhereWrapper().getAllDaoInWhereClause()), base.getJoins()));
        }

        sb.append("delete ");
        if (useDeleteSyntaxWithTableIdentifier()) {
            sb.append(storage.getIdentifierFor(base.getEntityName()));
            sb.append(SPACE);
        } else {
            storage.getIdentifierFor(base.getEntityName());
        }
        sb.append("from ");

        boolean thereWasAValue = false;
        for (String d : storage.getAllDao()) {
            if (!storage.isDaoInJoinClause(d)) {
                if (thereWasAValue) sb.append(COMMASPACE);
                thereWasAValue = true;
                sb.append(d);
                sb.append(SPACE);
                sb.append(storage.getIdentifierFor(d));
            }

        }
        sb.append(SPACE);

        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            sb.append(joinToExpression(joinWrapper, storage));
        }

        if (base.getWhereWrapper() != null) {
            sb.append(LINE_SEPARATOR);
            sb.append(" where ");
            sb.append(whereToExpression(base.getWhereWrapper(), storage));
        }


        if (base.getOrderBy() != null) {
            sb.append(LINE_SEPARATOR);
            sb.append(" order by ");
            sb.append(orderToExpression(base.getOrderBy(), storage));
        }

        String query = sb.toString();
        if (dbtarget.isLogQueriesEnabled()||base.isLogQueryEnabled()) {
            dbtarget.getLog().info(query);
        } else {
            dbtarget.getLog().debug(query);
        }
        return sb.toString();
    }


    default <E extends Entity> QuerySpecialParameters insertQueryToExpression(QueryInsert<E> base) {

        QuerySpecialParameters rv = new QuerySpecialParameters();

        StringBuilder sb = new StringBuilder();

        if (toNative(base, sb)) {
            rv.setQuery(sb);
            return rv;
        }

        sb.append("insert into ");
        sb.append(base.getEntityName());

        boolean select = base.getSelectQuery() != null;

        boolean putComa = false;

        if (base.getSetFields() != null) {

            StringBuilder values = new StringBuilder();

            sb.append(LINE_SEPARATOR);
            sb.append(OPEN_BRACKET);
            for (int i = 1; i < base.getSetFields().getCounter(); i++) {
                Column<?, ?, ?> cc = base.getSetFields().getFieldForPointer(i);
                Object val;

                if (base.isPkResolved() && cc.getColumnName().equals(base.getPkColumnName()) && base.getDictIdGenerator().equals(IdGeneratorType.SEQUENCE)) {
                    val = base.getPkNo();
                } else {
                    val = base.getSetFields().getValueForPointer(i);
//                    if (!select && val == null) continue;
                }

                if (select) {
                    sb.append(cc.getColumnName());
                    if (i < base.getSetFields().getCounter() - 1) {
                        sb.append(COMMA);
                    }
                } else if (cc != null) {
                    sb.append(cc.getColumnName());

                    if (val == null) {
                        values.append("NULL");
                    } else if (val instanceof Date) {
                        values.append(toDate(getDataBaseProductName(), (Date) val));
                    } else if (val instanceof byte[]) {
                        values.append("?");
                        rv.getSpecialParameters().put(rv.getCounter(), val);
                        rv.setCounter(rv.getCounter() + 1);
                    } else {
                        values.append(APOSTROPHE);
                        values.append(valueStringToSQL(val));
                        values.append(APOSTROPHE);
                    }
                    if (i < base.getSetFields().getCounter() - 1) {
                        sb.append(COMMA);
                        values.append(COMMA);
                    }
                }
            }
            sb.append(") ");


            if (!select) {
                sb.append(LINE_SEPARATOR);
                sb.append("values (");
                sb.append(LINE_SEPARATOR);
                sb.append(values.toString());
                sb.append(") ");
            }

        }

        if (select) {
            sb.append(toSqlQuery(base.getSelectQuery()));
        }


        rv.setQuery(sb);

        return rv;
    }

    default <E extends Entity> String toSqlQuery(Query<E, ?> base) {
        return toSqlQuery(base, base.getIdentifierStorage());
    }

    default <E extends Entity> boolean toNative(Query<E, ?> base, StringBuilder sb) {
        if (base.geNativeQuery() != null) {
            String query = base.geNativeQuery();

            if (base.getTarget().isLogQueriesEnabled() || base.isLogQueryEnabled()) {
                base.getTarget().getLog().info("[native query] " + query);
            } else {
                base.getTarget().getLog().debug("[native query] " + query);
            }

            sb.append(query);
            return true;
        }
        return false;
    }

    default <E extends Entity> String toSqlQuery(Query<E, ?> base, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
//        IdentifierStorage storage=base.getIdentifierStorage();

        if (toNative(base, sb)) {
            return sb.toString();
        }

        if (base.getWhereWrapper() != null) {
            storage.registerIdentifiers(base.getEntityName());
            storage.registerIdentifiers(base.getWhereWrapper().getAllDaoInWhereClause());
        }

        if (base._calcJoins) {
            List<String> from = new LinkedList<>();
            from.add(base.getEntityName());
            base.setJoins(JoinTracker.calculateJoins(getTables(), from, (base.getWhereWrapper() == null ? new HashSet<>() : base.getWhereWrapper().getAllDaoInWhereClause()), base.getJoins()));
        }


        boolean countInUse = base.getCount() != null;

        sb.append(LINE_SEPARATOR);
        sb.append("select ");
        if (countInUse) {
            sb.append("count(");
            if (base._unique) {
                sb.append("distinct ");
            }
            sb.append(countToExpression(base.getCount(), storage.getIdentifierFor(base.getEntityName())));
            sb.append(CLOSED_BRACKET);

        } else {
            if (base._unique) {
                sb.append("distinct ");
            }
            if (base.getFields().isEmpty()) {
                sb.append(storage.getIdentifierFor(base.getEntityName()));
            } else {
                for (Iterator<TableColumn> it = base.getFields().iterator(); it.hasNext(); ) {

                    Column<E, ?, ?> cc = it.next().getColumn();

                    if (cc instanceof ColumnFunction) {
                        ColumnFunction db = (ColumnFunction) cc;
                        sb.append(columnFunctionToExpression(db, storage, false));
                    } else {
                        sb.append(storage.getIdentifierForColumn(cc));
                    }

                    if (it.hasNext()) sb.append(COMMA);
                }
            }
        }


        for (Iterator<JoinWrapper> it = base.getJoins().iterator(); it.hasNext(); ) {
            JoinWrapper<?> joinWrapper = it.next();
            storage.getIdentifierForColumn(joinWrapper.getByColumn());
            storage.getIdentifierForJoinClause(joinWrapper.getTable().getEntityName());
        }

        sb.append(LINE_SEPARATOR);
        sb.append(" from ");
        boolean valueAlready = false;
        for (String d : storage.getAllDao()) {
            if (!storage.isDaoInJoinClause(d)) {
                if (valueAlready) sb.append(COMMASPACE);
                valueAlready = true;
                sb.append(d);
                sb.append(SPACE);
                sb.append(storage.getIdentifierFor(d));
            }
        }
        sb.append(SPACE);

        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            sb.append(LINE_SEPARATOR);
            sb.append(joinToExpression(joinWrapper, storage));
        }

        boolean limitandwhereprovided = base.getWhereWrapper() != null && (base.getLimit() != null && DictDatabaseType.ORACLE.equals(getDataBaseProductName()));

        if ((base.getWhereWrapper() != null && !base.getWhereWrapper().isEmpty()) || (base.getLimit() != null && DictDatabaseType.ORACLE.equals(getDataBaseProductName()))) {
            sb.append(LINE_SEPARATOR);
            sb.append(" where ");

            //Whole where clasuse should be separated from rownum pseudocolomn in case of OR/AND operator conflicts
            if (limitandwhereprovided) sb.append(OPEN_BRACKET);

            if (base.getWhereWrapper() != null) {
                sb.append(whereToExpression(base.getWhereWrapper(), storage));
            }

            if (limitandwhereprovided) sb.append(CLOSED_BRACKET);

            if (base.getLimit() != null) {

                if (DictDatabaseType.ORACLE.equals(getDataBaseProductName())) {
                    //oracle version
                    if (base.getWhereWrapper() != null) sb.append(" and ");
                    sb.append(limitToExpression(base.getLimit()));
                }
            }
        }

        if (base.getSetOperatorList() != null && !base.getSetOperatorList().isEmpty()) {
            sb.append(setOperatorsToExpression(base.getSetOperatorList()));
        }

        if (!base._groupBy.isEmpty()) {
            sb.append(LINE_SEPARATOR);
            sb.append(" group by ");
            for (Iterator<Column<?, ?, ?>> it = base._groupBy.iterator(); it.hasNext(); ) {
                Column<?, ?, ?> d = it.next();
                sb.append(storage.getIdentifierForColumn(d));
                if (it.hasNext()) sb.append(COMMASPACE);
            }
        }

        if (base.getHavingWrapper() != null) {
            sb.append(LINE_SEPARATOR);
            sb.append(" having ");
            sb.append(whereToExpression(base.getHavingWrapper(), storage));
        }

        if (base.getOrderBy() != null) {
            sb.append(LINE_SEPARATOR);
            sb.append(" order by ");
            sb.append(orderToExpression(base.getOrderBy(), storage));
        }

        if (base.getLimit() != null && !DictDatabaseType.ORACLE.equals(getDataBaseProductName())) {
            sb.append(LINE_SEPARATOR);
            sb.append(limitToExpression(base.getLimit()));
        }

        String query = sb.toString();
        if (base.getTarget().isLogQueriesEnabled() || base.isLogQueryEnabled()) {
            base.getTarget().getLog().info(query);
        } else {
            base.getTarget().getLog().debug(query);
        }
        return sb.toString();
    }

    default StringBuilder setOperatorsToExpression(List<SetOperator> setOperators) {
        StringBuilder sb = new StringBuilder();
        if (setOperators == null) return sb;
        setOperators.forEach(setOperator -> {
            sb.append(LINE_SEPARATOR);
            switch (setOperator.getType()) {
                case SetOperator.UNION: {
                    sb.append(" UNION ");
                    break;
                }
                case SetOperator.UNION_ALL: {
                    sb.append(" UNION ALL ");
                    break;
                }
                case SetOperator.EXCEPT: {
                    sb.append(" EXCEPT ");
                    break;
                }
                case SetOperator.EXCEPT_ALL: {
                    sb.append(" EXCEPT ALL ");
                    break;
                }
                case SetOperator.INTERSECT: {
                    sb.append(" INTERSECT ");
                    break;
                }
                case SetOperator.MINUS: {
                    sb.append(" MINUS ");
                    break;
                }
            }
            sb.append(OPEN_BRACKET);
            sb.append(toSqlQuery(setOperator.getQuery(), new IdentifierStorage()));
            sb.append(CLOSED_BRACKET);
        });

        return sb;
    }

    default <E extends Entity> QuerySpecialParameters toQueryUpdateExpression(QueryUpdate<E> base) {
        QuerySpecialParameters rv = new QuerySpecialParameters();

        StringBuilder sb = new StringBuilder();

        if (toNative(base, sb)) {
            rv.setQuery(sb);
            return rv;
        }
        IdentifierStorage storage=base.getIdentifierStorage();

        sb.append("update ");
        sb.append(base.getEntityName());
        sb.append(SPACE);
        sb.append(storage.getIdentifierFor(base.getEntityName()));
        sb.append(" set ");

        if (base.getSetFields() != null) {
            rv = toQuerySpecialParametersExpression(base.getSetFields(),storage);
            sb.append(rv.getQuery());
        }


        if (base.getWhereWrapper() != null) {
            sb.append(" where ");
            sb.append(whereToExpression(base.getWhereWrapper(), storage));
        }


        String query = sb.toString();
        if (base.getTarget().isLogQueriesEnabled()||base.isLogQueryEnabled()) {
            base.getTarget().getLog().info(query);
        } else {
            base.getTarget().getLog().debug(query);
        }

        rv.setQuery(sb);
        return rv;
    }




    default QuerySpecialParameters toQuerySpecialParametersExpression(SetFields setFields, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        QuerySpecialParameters rv = new QuerySpecialParameters();

        for (int i = 1; i < setFields.getCounter(); i++) {
            Column<?, ?, ?> field = setFields.getFieldForPointer(i);
            Object value = setFields.getValueForPointer(i);

            sb.append(storage.getIdentifierFor(field.getEntityName()));
            sb.append(DOT);
            sb.append(field.getColumnName());

            if (value != null) sb.append(" = ");

            if (value == null) {
                sb.append(" = NULL ");
            } else if (value instanceof Date) {
                sb.append(toDate(getDataBaseProductName(), (Date) value));
            } else if (value instanceof byte[]) {
                sb.append("?");
                rv.getSpecialParameters().put(rv.getCounter(), value);
                rv.setCounter(rv.getCounter() + 1);
            } else if (value instanceof String){
                sb.append(APOSTROPHE);
                sb.append(valueStringToSQL(value));
                sb.append(APOSTROPHE);
            }else{
                sb.append(APOSTROPHE);
                sb.append(valueStringToSQL(value));
                sb.append(APOSTROPHE);
            }

            if (i < setFields.getCounter() - 1) {
                sb.append(COMMASPACE);
            }
        }

        rv.setQuery(sb);

        return rv;
    }


    default StringBuilder limitToExpression(Limit limit) {

        String databaseengine = getDataBaseProductName();
        StringBuilder sb = new StringBuilder();

        if (DictDatabaseType.ORACLE.equals(databaseengine)) {
            sb.append(" ROWNUM <= '" + limit.getLimit() + "' " + (limit.getOffset() > 0 ? "and ROWNUM >" + limit.getOffset() : ""));
        } else if (DictDatabaseType.MYSQL.equals(databaseengine)) {
            sb.append(LIMIT + (limit.getOffset() > 0 ? limit.getOffset() + COMMA : "") + limit.getLimit());
        } else if (DictDatabaseType.PostgreSQL.equals(databaseengine)) {
            sb.append(LIMIT + limit.getLimit() + SPACE + (limit.getOffset() > 0 ? "offset " + limit.getOffset() : ""));
        } else if (DictDatabaseType.H2.equals(databaseengine)) {
            sb.append(LIMIT + limit.getLimit() + SPACE + (limit.getOffset() > 0 ? "offset " + limit.getOffset() : ""));
        }

        return sb;
    }

    default StringBuilder joinToExpression(JoinWrapper<?> joinWrapper, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        sb.append(joinWrapper.getType().toString());
        sb.append(SPACE);
        sb.append(joinWrapper.getTable().getEntityName());
        sb.append(SPACE);
        sb.append(storage.getIdentifierFor(joinWrapper.getTable().getEntityName()));
        sb.append(" on ");

        sb.append(whereToExpression(joinWrapper.getWhere(), storage));

        return sb;
    }

    default StringBuilder orderToExpression(Order order, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < order.getCounter(); i++) {
            Column<?, ?, ?> field = order.getFieldForPointer(i);

            sb.append(storage.getIdentifierFor(field.getEntityName()));
            sb.append(DOT);
            sb.append(field.getColumnName());
            sb.append(SPACE);
            sb.append(order.getOrderKindForPointer(i));

            if (i < order.getCounter() - 1) {
                sb.append(COMMASPACE);
            }
        }

        return sb;
    }


    default StringBuilder countToExpression(Count count, String daoidentifier) {
        StringBuilder sb = new StringBuilder();
        if (count.getCounter() == 1) {
            Column<?, ?, ?> field = count.getFieldForPointer(1);

            sb.append(field == null ? " *" : field.getColumnName());
            sb.append(SPACE);
        } else {
            for (int i = 1; i < count.getCounter(); i++) {
                Column<?, ?, ?> field = count.getFieldForPointer(i);
                boolean distinct = count.isDistinctForPointer(i) && field != null;

                if (distinct) {
                    sb.append("distinct ");
                }
                sb.append(field == null ? daoidentifier : (daoidentifier + DOT + field.getColumnName()));

                if (i < count.getCounter() - 1) {
                    sb.append(COMMASPACE);
                }
            }
        }
        return sb;
    }


    default StringBuilder whereToExpression(Where where, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        String databaseengine = getDataBaseProductName();
        String relationToNext = where.getRelationBetweenExpressions();

        for (int i = 1; i < where.getCounter(); i++) {
            Object value = where.getValueForPointer(i);

            Operator relation = null;
            if (!(value instanceof Where)) {
                relation = where.getRelationForPointer(i);
            }

            @SuppressWarnings("unchecked")
            Column<Entity, Object, EntityRelation> keyFromWrapper = (Column<Entity, Object, EntityRelation>) where.getKeyForPointer(i);

            if (keyFromWrapper != null && value != null) {
//                if (value instanceof OneCellProvider) {
//                    OneCellProvider<?, ?> wr = (OneCellProvider<?, ?>) value;
//                    if (wr.isResultCached()) value = wr.result();
                if (value instanceof ManyCellsProvider) {
                    ManyCellsProvider<?> wr = (ManyCellsProvider<?>) value;
                    if (wr.isResultCached()) value = wr.findMany();
                }
//                if (value instanceof OneEntityProvider) {
//                    OneEntityProvider<?> wr = (OneEntityProvider<?>) value;
//                    value = keyFromWrapper.getValueOf((EntityRelation) wr.result());
//                }
                if (value instanceof EntitiesProvider) {
                    EntitiesProvider<?> wr = (EntitiesProvider<?>) value;
                    value = wr.findMany().stream().map((e) -> keyFromWrapper.getValueOf((EntityRelation) e)).collect(Collectors.toList());
                }
            }

            if (value == null && (Operator.IS_NULL.equals(relation) || Operator.NOT_NULL.equals(relation))) {
                sb.append(appendKey(storage, keyFromWrapper, databaseengine,relation));
            } else if (value instanceof Column<?, ?, ?>) {
                sb.append(appendKey(storage, keyFromWrapper, databaseengine,relation));
                sb.append(storage.getIdentifierForColumn((Column<?, ?, ?>) value));
            } else if (value instanceof Where) {
                Where wr = (Where) value;
                sb.append(SPACE_OPEN_BRACKET + whereToExpression(wr, storage) + CLOSED_BRACKET);
            } else if (value instanceof InnerSelectManyEntities) {
                InnerSelectManyEntities<?> wr = (InnerSelectManyEntities<?>) value;
                sb.append(appendKey(storage, keyFromWrapper, databaseengine, relation));
                sb.append(SPACE_OPEN_BRACKET).append(toSqlQuery(wr.getSelect(), storage)).append(CLOSED_BRACKET);
            } else if (value instanceof InnerSelectManyCells) {
                InnerSelectManyCells wr = (InnerSelectManyCells) value;
                sb.append(appendKey(storage, keyFromWrapper, databaseengine, relation))
                        .append(toInnerQueryExpression(storage,this,wr));
            } else if (value instanceof Collection || relation.isRelationCollectionBased()) {
                if (value instanceof Collection) {
                    Collection<?> valueCollection = (Collection<?>) value;
                    sb.append(appendKey(storage, keyFromWrapper, databaseengine,relation));
                    sb.append(toChain(valueCollection));
                } else {
                    //w kolekcji moze sie znajdowac tylko jeden element wowczas typ obiektu nie bedzie collection
                    sb.append(appendKey(storage, keyFromWrapper, databaseengine, relation));
                    sb.append("('").append(valueStringToSQL(value)).append("')");
                }
            } else {
                sb.append(SPACE);
                sb.append(appendKey(storage, keyFromWrapper, databaseengine, relation));
                sb.append(valueToSQL(storage, databaseengine, relation, value));
            }

            if (relationToNext != null && i < where.getCounter() - 1) {
                sb.append(relationToNext);
            }
        }

        return sb;
    }


    default StringBuilder appendKey(IdentifierStorage storage, Column<Entity, Object, EntityRelation> keyFromWrapper, String databaseengine) {
        StringBuilder sb=new StringBuilder();
        if (keyFromWrapper instanceof ColumnFunction) {
            ColumnFunction function = (ColumnFunction) keyFromWrapper;
            sb.append(function.getMode())
                    .append(OPEN_BRACKET)
                    .append(storage.getIdentifierForColumn(keyFromWrapper))
                    .append(CLOSED_BRACKET);
        } else {
            sb.append(storage.getIdentifierForColumn(keyFromWrapper));
        }

        return sb;
    }

    default StringBuilder appendKey(IdentifierStorage storage, Column<Entity, Object, EntityRelation> keyFromWrapper, String databaseengine,Operator relation) {
        StringBuilder sb=appendKey(storage,keyFromWrapper,databaseengine);
        sb.append(relation);
        return sb;
    }

    default StringBuilder toChain(Collection<?> list) {
        StringBuilder sb = new StringBuilder();
        if (list == null || list.isEmpty()){
            sb.append("('')");
            return sb;
        }
        int counter = 0;
        sb.append(OPEN_BRACKET);
        boolean needApostrophe=list.iterator().next() instanceof String;
        for (Object o : list) {
            if (o != null){
                if (needApostrophe){
                    sb.append(APOSTROPHE)
                            .append(o)
                            .append(APOSTROPHE);
                }else{
                    sb.append(o.toString());
                }

            }
            counter++;
            if (counter < list.size()) {
                sb.append(COMMA);
            }
        }
        sb.append(CLOSED_BRACKET);

        return sb;
    }




    default <E extends Entity,F> StringBuilder toInnerQueryExpression(IdentifierStorage storage, Target target, InnerSelectManyCells innerQuery) {
        if (innerQuery.getSelect()!=null&&target.getClass().getName().equals(innerQuery.getSelect().getTarget().getClass().getName())){
            StringBuilder sb=new StringBuilder();
            sb.append(OPEN_BRACKET)
                    .append(toSqlQuery(innerQuery.getSelect(), storage))
                    .append(CLOSED_BRACKET);
            return sb;
        }else{
            return toChain(innerQuery.findMany());
        }
    }


    @SuppressWarnings({"java:S1872", "java:S3740"})
    default StringBuilder columnFunctionToExpression(ColumnFunction columnFunction, IdentifierStorage storage, boolean internalfunction) {
        if (columnFunction.getClass().getName().equals(SubstringColumnRelation.class.getName())) {
            SubstringColumnRelation sdb = (SubstringColumnRelation) columnFunction;
            return toSubstringColumnRelationQueryExpression(sdb, storage, sdb.getMode(), sdb.from, sdb.to, sdb.fromColumn, sdb.toColumn);
        }
        if (columnFunction.getClass().getName().equals(CastColumnRelation.class.getName())) {
            CastColumnRelation sdb = (CastColumnRelation) columnFunction;
            return toCastColumnRelationQueryExpression(sdb, storage, sdb.getMode(), sdb.type);
        }
        StringBuilder sb = new StringBuilder();
        boolean table = columnFunction.columns != null;
        if (table) {
            sb.append(columnFunction.getMode() + OPEN_BRACKET);
            int counter = 1;

            for (Column<?,?,?> col : columnFunction.columns) {
                if (col instanceof ColumnFunction) {
                    ColumnFunction<?,?,?,?> formerColumn = (ColumnFunction<?,?,?,?>) col;
                    sb.append(columnFunctionToExpression(formerColumn, storage, true));
                } else {
                    sb.append(storage.getIdentifierForColumn(col));
                }
                counter++;
                if (counter < columnFunction.columns.length + 1) {
                    sb.append(SPACE + columnFunction.mediator + SPACE);
                }
            }

            sb.append(CLOSED_BRACKET);
        } else if (columnFunction instanceof DummyColumnRelation) {
            DummyColumnRelation dummy = (DummyColumnRelation) columnFunction;
            sb.append(OPEN_BRACKET);
            sb.append(toSqlQuery(dummy.getQuery(), new IdentifierStorage()));
            sb.append(CLOSED_BRACKET);
//            if (!internalfunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
//                sb.append(" as ").append(columnFunction.identifier).append(SPACE);
//            }
        } else if (columnFunction.column == null) {
            sb.append(columnFunction.getMode() + OPEN_BRACKET);
            sb.append("*");
            sb.append(CLOSED_BRACKET);
            storage.getIdentifierFor(columnFunction.getEntityName());
//            if (!internalfunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
//                sb.append(" as ").append(columnFunction.identifier).append(SPACE);
//            }
        } else {
            sb.append(toColumnFunctionQueryExpression(columnFunction.column, storage, columnFunction.getMode()));
        }


        if (!internalfunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
            sb.append(" as ").append(columnFunction.identifier).append(SPACE);
        }
        return sb;
    }

    default <E extends Entity, F, R extends EntityRelation> StringBuilder toColumnFunctionQueryExpression(Column<E, F, R> column, IdentifierStorage storage, String mode) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);
        if (column instanceof ColumnFunction) {
            ColumnFunction<?,?,?,?> formerColumn = (ColumnFunction<?,?,?,?>) column;
            sb.append(columnFunctionToExpression(formerColumn, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(column));
        }
        sb.append(CLOSED_BRACKET);
        return sb;
    }

    default <E extends Entity, F, R extends EntityRelation> StringBuilder toSubstringColumnRelationQueryExpression(Column<E, F, R> column, IdentifierStorage storage,String mode,Integer from, Integer to,Column fromColumn,Column toColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);
        if (column instanceof ColumnFunction) {
            ColumnFunction formerColumn = (ColumnFunction) column;
            sb.append(columnFunctionToExpression(formerColumn, storage, true));
        } else {
            if (from != null && to != null) {
                sb.append(storage.getIdentifierForColumn(column))
                        .append(COMMA)
                        .append(from)
                        .append(COMMA)
                        .append(to);
            } else if (from == null && to != null) {
                sb.append(storage.getIdentifierForColumn(column))
                        .append(COMMA)
                        .append(toColumnFunctionQueryExpression(fromColumn, storage,mode))
                        .append(COMMA)
                        .append(to);
            }
        }
        sb.append(CLOSED_BRACKET);
        return sb;
    }

    default <E extends Entity, F, R extends EntityRelation> StringBuilder toCastColumnRelationQueryExpression(Column<E, F, R> column, IdentifierStorage storage, String mode, CastType type) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);
        if (column instanceof ColumnFunction) {
            ColumnFunction<?, ?, ?, ?> formerColumn = (ColumnFunction<?, ?, ?, ?>) column;
            sb.append(columnFunctionToExpression(formerColumn, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(column) + " as " + type.toString());
        }
        sb.append(CLOSED_BRACKET);
        return sb;
    }


    default String toDate(String databasetype, Date value) {
        if (value instanceof java.sql.Timestamp) {
            return toTimestampDate(databasetype, value);
        }
        return toSQLDate(databasetype, value);
    }

    default String toSQLDate(String databasetype, Date value) {
        String dateAsString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (DictDatabaseType.ORACLE.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return " TO_DATE('" + dateAsString + "','YYYY-MM-DD HH24:MI:SS') ";
        } else if (DictDatabaseType.MYSQL.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return " STR_TO_DATE('" + dateAsString + "', '%Y-%m-%d %H:%i:%s')";
        } else if (DictDatabaseType.H2.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return "'" + dateAsString + "'";
        } else if (DictDatabaseType.PostgreSQL.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return " to_date('" + dateAsString + "', 'YYYY-MM-DD HH24:MI:SS')";
        } else {
            dateAsString = dateFormat.format(value);
            return "'" + dateAsString + "'";
        }
    }

    default String toTimestampDate(String databasetype, Date value) {
        String dateAsString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (DictDatabaseType.ORACLE.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return " TO_DATE('" + dateAsString + "','YYYY-MM-DD HH24:MI:SS') ";
        } else if (DictDatabaseType.MYSQL.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return " STR_TO_DATE('" + dateAsString + "', '%Y-%m-%d %H:%i:%s')";
        } else if (DictDatabaseType.H2.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return "'" + dateAsString + "'";
        } else if (DictDatabaseType.PostgreSQL.equals(databasetype)) {
            dateAsString = dateFormat.format(value);
            return " to_date('" + dateAsString + "', 'YYYY-MM-DD HH24:MI:SS')";
        } else {
            dateAsString = dateFormat.format(value);
            return "'" + dateAsString + "'";
        }
    }


    default StringBuilder valueToSQL(IdentifierStorage storage, String databaseengine, Operator relation, Object value) {
        StringBuilder sb = new StringBuilder();

        if (value instanceof Boolean) {
            if (Boolean.TRUE.equals(value)) {
                value = new BigDecimal(1L);
            } else {
                value = new BigDecimal(0L);
            }
        }

        boolean valueIsDate = value instanceof Date;
        boolean valueIsNumeric = value instanceof Number;


        //TODO: mysle ze moze sie okazac ze nadmiarowo podaje apostrowy. sprawdz na okolicznosc warunku lub (||)
        if (!relation.isRelationCollectionBased() && !valueIsDate && !valueIsNumeric) {
            sb.append("'");
        }


        if (valueIsDate) {
            sb.append(toDate(databaseengine, (Date) value));
        } else {
            sb.append(valueStringToSQL(value));
        }

        if (!relation.isRelationCollectionBased() && !valueIsDate && !valueIsNumeric) {
            sb.append("'");
        }
        return sb;
    }

    default StringBuilder valueStringToSQL(Object value) {
        StringBuilder sb = new StringBuilder();
        String valStr = value.toString();
        String valStrLower = value.toString().toLowerCase();
        if (valStr.contains(";")
                && (valStr.contains("'") || valStr.contains("\""))
                && (valStrLower.contains("table") || valStrLower.contains("insert") || valStrLower.contains("update") || valStrLower.contains("delete"))) {
            throw new SqlInjectionDetected(valStr);
        }
        sb.append(value.toString().replace("'", "''"));
        return sb;
    }

}
