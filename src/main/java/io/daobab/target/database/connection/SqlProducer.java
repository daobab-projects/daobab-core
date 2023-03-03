package io.daobab.target.database.connection;

import io.daobab.dict.DictDatabaseType;
import io.daobab.error.SqlInjectionDetected;
import io.daobab.model.*;
import io.daobab.query.base.QueryExpressionProvider;
import io.daobab.query.base.QuerySpecialParameters;
import io.daobab.result.EntitiesProvider;
import io.daobab.result.FieldsProvider;
import io.daobab.statement.base.IdentifierStorage;
import io.daobab.statement.condition.*;
import io.daobab.statement.function.base.CastType;
import io.daobab.statement.function.base.FunctionKey;
import io.daobab.statement.function.type.CastColumnRelation;
import io.daobab.statement.function.type.ColumnFunction;
import io.daobab.statement.function.type.DummyColumnRelation;
import io.daobab.statement.function.type.ManyArgumentsFunction;
import io.daobab.statement.inner.InnerQueryFields;
import io.daobab.statement.join.JoinTracker;
import io.daobab.statement.join.JoinWrapper;
import io.daobab.statement.where.base.Where;
import io.daobab.target.database.DataBaseTargetLogic;
import io.daobab.target.database.QueryTarget;
import io.daobab.target.database.converter.dateformat.DatabaseDateConverter;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.query.DataBaseQueryBase;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.query.DataBaseQueryInsert;
import io.daobab.target.database.query.DataBaseQueryUpdate;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public interface SqlProducer extends QueryResolverTransmitter, DataBaseTargetLogic {

    String LINE_SEPARATOR = System.getProperty("line.separator");
    String LIMIT = " limit  ";
    String SPACE = " ";
    String APOSTROPHE = "'";
    String NULL = "null";
    String COMMA = ",";
    String DOT = ".";
    String COMMA_SPACE = ", ";
    String OPEN_BRACKET = "(";
    String SPACE_OPEN_BRACKET = " (";
    String CLOSED_BRACKET = ")";
    String SPACE_COMMA = SPACE + COMMA;

    String getDataBaseProductName();

    default boolean useDeleteSyntaxWithTableIdentifier() {
        return false;
    }


    @SuppressWarnings("java:S3776")
    default <E extends Entity> String toDeleteSqlQuery(DataBaseQueryDelete<E> base) {

        IdentifierStorage storage = base.getIdentifierStorage();
        QueryTarget dataBaseTarget = base.getTarget();
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
        for (String d : storage.getQueryEntities()) {
            if (!storage.isEntityInJoinClause(d)) {
                if (thereWasAValue) sb.append(COMMA_SPACE);
                thereWasAValue = true;
                sb.append(d);
                sb.append(SPACE);
                sb.append(storage.getIdentifierFor(d));
            }

        }
        sb.append(SPACE);

        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            sb.append(joinToExpression(base.getTarget(), joinWrapper, storage));
        }

        if (base.getWhereWrapper() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" where ")
                    .append(whereToExpression(base.getTarget(), base.getWhereWrapper(), storage));
        }

        if (base.getOrderBy() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" order by ")
                    .append(orderToExpression(base.getOrderBy(), storage));
        }

        String query = sb.toString();
        if (dataBaseTarget.getShowSql() || base.isLogQueryEnabled()) {
            dataBaseTarget.getLog().info(query);
        } else {
            dataBaseTarget.getLog().debug(query);
        }
        return sb.toString();
    }

    @SuppressWarnings("java:S3776")
    default <E extends Entity> QuerySpecialParameters toInsertSqlQuery(DataBaseQueryInsert<E> base) {

        QuerySpecialParameters rv = new QuerySpecialParameters();
        StringBuilder sb = new StringBuilder();

        if (toNative(base, sb)) {
            rv.setQuery(sb);
            return rv;
        }

        sb.append("insert into ")
                .append(base.getEntityName());

        boolean select = base.getSelectQuery() != null;

        if (base.getSetFields() != null) {

            StringBuilder values = new StringBuilder();

            sb.append(LINE_SEPARATOR).append(OPEN_BRACKET);
            for (int i = 1; i < base.getSetFields().getCounter(); i++) {
                Column<?, ?, ?> column = base.getSetFields().getFieldForPointer(i);
                String columnName = column.getColumnName();

                DatabaseTypeConverter typeConverter = base.getTarget().getConverterManager().getConverter(column).orElse(null);
                Object value;

                if (base.isPkResolved() && columnName != null && columnName.equals(base.getPkColumnName()) && base.getDictIdGenerator().equals(IdGeneratorType.SEQUENCE)) {
                    value = base.getPkNo();
                } else {
                    value = base.getSetFields().getValueForPointer(i);
//                    if (!select && val == null) continue;
                }

                if (select) {
                    sb.append(columnName);
                    if (i < base.getSetFields().getCounter() - 1) {
                        sb.append(COMMA);
                    }
                } else if (column != null) {
                    if (columnName != null) {
                        sb.append(columnName);

                        if (value == null) {
                            values.append(NULL);
                        } else {
                            values.append(typeConverter.convertWritingTarget(value));
                            if (typeConverter.needParameterConversion()) {
                                rv.getSpecialParameters().put(rv.getCounter(), value);
                                rv.setCounter(rv.getCounter() + 1);
                            }
                        }
                    }

                    if (i < base.getSetFields().getCounter() - 1) {
                        sb.append(COMMA);
                        values.append(COMMA);
                    }
                }
            }
            sb.append(") ");

            if (!select) {
                sb.append(LINE_SEPARATOR)
                        .append("values (")
                        .append(LINE_SEPARATOR)
                        .append(values)
                        .append(") ");
            }
        }

        if (select) {
            sb.append(toSqlQuery((DataBaseQueryBase<?, ?>) base.getSelectQuery()));
        }

        rv.setQuery(sb);
        return rv;
    }

    //no need TypeConverter here
    default StringBuilder toProcedureSQL(Object val, StringBuilder values, QueryTarget target) {
        if (val == null) {
            values.append(NULL);
        } else if (val instanceof Timestamp) {
            values.append(target.getDatabaseDateConverter().toDatabaseTimestamp((Timestamp) val));
        } else if (val instanceof Date) {
            values.append(target.getDatabaseDateConverter().toDatabaseDate((Date) val));
        } else if (val instanceof byte[]) {
            values.append("?");
//            rv.getSpecialParameters().put(rv.getCounter(), val);
//            rv.setCounter(rv.getCounter() + 1);
        } else if (val instanceof String) {
            values.append(APOSTROPHE)
                    .append(valueStringToSQL(val))
                    .append(APOSTROPHE);
        } else if (val instanceof Time) {
            values.append(target.getDatabaseDateConverter().toDatabaseTimestamp((Time) val));
        } else {
            values.append(val);
        }

        return values;
    }

    default <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> base) {
        return toSqlQuery(base, base.getIdentifierStorage());
    }

    default <E extends Entity> boolean toNative(DataBaseQueryBase<E, ?> base, StringBuilder sb) {
        if (base.geNativeQuery() != null) {
            String query = base.geNativeQuery();

            if (base.getTarget().getShowSql() || base.isLogQueryEnabled()) {
                base.getTarget().getLog().info("[native query] {}", query);
            } else {
                base.getTarget().getLog().debug("[native query] {}", query);
            }

            sb.append(query);
            return true;
        }
        return false;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776"})
    default <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> base, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        if (toNative(base, sb)) {
            return sb.toString();
        }

        if (base.getWhereWrapper() != null) {
            storage.registerIdentifiers(base.getEntityName());
            storage.registerIdentifiers(base.getWhereWrapper().getAllDaoInWhereClause());
        }

        if (base.isJoin()) {
            List<String> from = new ArrayList<>();
            from.add(base.getEntityName());
            base.setJoins(JoinTracker.calculateJoins(getTables(), from, (base.getWhereWrapper() == null ? new HashSet<>() : base.getWhereWrapper().getAllDaoInWhereClause()), base.getJoins()));
        }

        boolean countInUse = base.getCount() != null;

        sb.append(LINE_SEPARATOR);
        sb.append("select ");
        if (countInUse) {
            sb.append("count(");
            if (base.isUnique()) {
                sb.append("distinct ");
            }
            sb.append(countToExpression(base.getCount(), storage.getIdentifierFor(base.getEntityName())))
                    .append(CLOSED_BRACKET);

        } else {
            if (base.getLimit() != null && base.getLimit().getOffset() == 0 && getDataBaseProductName().startsWith(DictDatabaseType.MicrosoftSQL)) {
                sb.append("top(").append(base.getLimit().getLimit()).append(") ");
            }

            if (base.isUnique()) {
                sb.append("distinct ");
            }
            if (base.getFields().isEmpty()) {
                sb.append(storage.getIdentifierFor(base.getEntityName()));
            } else {
                for (Iterator<TableColumn> it = base.getFields().iterator(); it.hasNext(); ) {

                    Column<E, ?, ?> column = it.next().getColumn();
                    boolean fakeColumn = column.getColumnName() == null;

                    if (column instanceof ColumnFunction) {
                        ColumnFunction db = (ColumnFunction) column;
                        sb.append(columnFunctionToExpression(db, storage, false));
                    } else {
                        if (!fakeColumn) {
                            sb.append(storage.getIdentifierForColumn(column));
                        }
                    }

                    if (it.hasNext() && !fakeColumn) sb.append(COMMA);
                }
            }
        }

        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            storage.getIdentifierForColumn(joinWrapper.getByColumn());
            storage.registerIdentifierForJoinClause(joinWrapper.getTable().getEntityName());
        }

        sb.append(LINE_SEPARATOR);
        sb.append(" from ");
        boolean valueAlready = false;
        for (String d : storage.getQueryEntities()) {
            if (!storage.isEntityInJoinClause(d)) {
                if (valueAlready) sb.append(COMMA_SPACE);
                valueAlready = true;
                sb.append(d)
                        .append(SPACE)
                        .append(storage.getIdentifierFor(d));
            }
        }
        sb.append(SPACE);

        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            sb.append(LINE_SEPARATOR)
                    .append(joinToExpression(base.getTarget(), joinWrapper, storage));
        }

        boolean limitAndWhereProvided = base.getWhereWrapper() != null && (base.getLimit() != null && DictDatabaseType.ORACLE.equals(getDataBaseProductName()));

        if ((base.getWhereWrapper() != null && !base.getWhereWrapper().isEmpty()) || (base.getLimit() != null && DictDatabaseType.ORACLE.equals(getDataBaseProductName()))) {
            sb.append(LINE_SEPARATOR);
            sb.append(" where ");

            //Whole where clasuse should be separated from rownum pseudocolomn in case of OR/AND operator conflicts
            if (limitAndWhereProvided) sb.append(OPEN_BRACKET);

            if (base.getWhereWrapper() != null) {
                sb.append(whereToExpression(base.getTarget(), base.getWhereWrapper(), storage));
            }

            if (limitAndWhereProvided) sb.append(CLOSED_BRACKET);

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

        if (!base.getGroupBy().isEmpty() || base.getGroupByAlias() != null) {
            sb.append(LINE_SEPARATOR);
            sb.append(" group by ");
            if (base.getGroupByAlias() != null) {
                sb.append(base.getGroupByAlias()).append(SPACE);
            } else {
                for (Iterator<Column<?, ?, ?>> it = base.getGroupBy().iterator(); it.hasNext(); ) {
                    sb.append(storage.getIdentifierForColumn(it.next()));
                    if (it.hasNext()) sb.append(COMMA_SPACE);
                }
            }
        }

        if (base.getHavingWrapper() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" having ")
                    .append(whereToExpression(base.getTarget(), base.getHavingWrapper(), storage));
        }

        if (base.getOrderBy() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" order by ")
                    .append(orderToExpression(base.getOrderBy(), storage));
        }

        if (base.getLimit() != null && !DictDatabaseType.ORACLE.equals(getDataBaseProductName())) {
            sb.append(LINE_SEPARATOR)
                    .append(limitToExpression(base.getLimit()));
        }

        String query = sb.toString();
        if (base.getTarget().getShowSql() || base.isLogQueryEnabled()) {
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
                    sb.append(" union ");
                    break;
                }
                case SetOperator.UNION_ALL: {
                    sb.append(" union all ");
                    break;
                }
                case SetOperator.EXCEPT: {
                    sb.append(" except ");
                    break;
                }
                case SetOperator.EXCEPT_ALL: {
                    sb.append(" except all ");
                    break;
                }
                case SetOperator.INTERSECT: {
                    sb.append(" intersect ");
                    break;
                }
                case SetOperator.MINUS: {
                    sb.append(" minus ");
                    break;
                }
                default:
                    break;
            }
            sb.append(OPEN_BRACKET);
            sb.append(toSqlQuery((DataBaseQueryBase<?, ?>) setOperator.getQuery(), new IdentifierStorage()));
            sb.append(CLOSED_BRACKET);
        });

        return sb;
    }

    default <E extends Entity> QuerySpecialParameters toUpdateSqlQuery(DataBaseQueryUpdate<E> base) {
        QuerySpecialParameters rv = new QuerySpecialParameters();

        StringBuilder sb = new StringBuilder();

        if (toNative(base, sb)) {
            rv.setQuery(sb);
            return rv;
        }
        IdentifierStorage storage = base.getIdentifierStorage();

        sb.append("update ");
        sb.append(base.getEntityName());
        sb.append(SPACE);
        sb.append(storage.getIdentifierFor(base.getEntityName()));
        sb.append(" set ");

        if (base.getSetFields() != null) {
            rv = toQuerySpecialParametersExpression(base.getTarget(), base.getSetFields(), storage);
            sb.append(rv.getQuery());
        }

        if (base.getWhereWrapper() != null) {
            sb.append(" where ");
            sb.append(whereToExpression(base.getTarget(), base.getWhereWrapper(), storage));
        }

        String query = sb.toString();
        if (base.getTarget().getShowSql() || base.isLogQueryEnabled()) {
            base.getTarget().getLog().info(query);
        } else {
            base.getTarget().getLog().debug(query);
        }

        rv.setQuery(sb);
        return rv;
    }

    default QuerySpecialParameters toQuerySpecialParametersExpression(QueryTarget target, SetFields setFields, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        QuerySpecialParameters rv = new QuerySpecialParameters();

        for (int i = 1; i < setFields.getCounter(); i++) {
            Column<?, ?, ?> field = setFields.getFieldForPointer(i);
            Object value = setFields.getValueForPointer(i);

            DatabaseTypeConverter typeConverter = target.getConverterManager().getConverter(field).orElse(null);

            sb.append(storage.getIdentifierFor(field.getEntityName()))
                    .append(DOT)
                    .append(field.getColumnName())
                    .append(" = ");

            if (value == null) {
                sb.append(NULL).append(" ");
            } else {
                sb.append(typeConverter.convertWritingTarget(value));
                if (typeConverter.needParameterConversion()) {
                    rv.getSpecialParameters().put(rv.getCounter(), value);
                    rv.setCounter(rv.getCounter() + 1);
                }
            }

            if (i < setFields.getCounter() - 1) {
                sb.append(COMMA_SPACE);
            }
        }

        rv.setQuery(sb);
        return rv;
    }

    default StringBuilder limitToExpression(Limit limit) {
        String databaseEngine = getDataBaseProductName();
        StringBuilder sb = new StringBuilder();

        if (DictDatabaseType.ORACLE.equals(databaseEngine)) {
            sb.append(" ROWNUM <= '")
                    .append(limit.getLimit()).append("' ")
                    .append(limit.getOffset() > 0 ? "and ROWNUM >" + limit.getOffset() : "");
        } else if (DictDatabaseType.MYSQL.equals(databaseEngine)) {
            sb.append(LIMIT).append(limit.getOffset() > 0 ? limit.getOffset() + COMMA : "").append(limit.getLimit());
        } else if (DictDatabaseType.PostgreSQL.equals(databaseEngine)) {
            sb.append(LIMIT).append(limit.getLimit()).append(SPACE).append(limit.getOffset() > 0 ? "offset " + limit.getOffset() : "");
        } else if (DictDatabaseType.H2.equals(databaseEngine)) {
            sb.append(LIMIT).append(limit.getLimit()).append(SPACE).append(limit.getOffset() > 0 ? "offset " + limit.getOffset() : "");
        } else if (limit.getOffset() > 0 && databaseEngine.startsWith(DictDatabaseType.MicrosoftSQL)) {
            sb.append("offset ").append(limit.getOffset()).append(" rows ").append("fetch next ").append(limit.getLimit()).append(" rows only ");
        }

        return sb;
    }

    default StringBuilder joinToExpression(QueryTarget target, JoinWrapper<?> joinWrapper, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        sb.append(joinWrapper.getType().toString());
        sb.append(SPACE);
        sb.append(joinWrapper.getTable().getEntityName());
        sb.append(SPACE);
        sb.append(storage.getIdentifierFor(joinWrapper.getTable().getEntityName()));
        sb.append(" on ");

        sb.append(whereToExpression(target, joinWrapper.getWhere(), storage));

        return sb;
    }

    default StringBuilder orderToExpression(Order order, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < order.getCounter(); i++) {
            Object orderedField = order.getObjectForPointer(i);

            if (orderedField instanceof String) {
                sb.append(orderedField);
                sb.append(SPACE);
                sb.append(order.getOrderKindForPointer(i));
            } else {
                Column<?, ?, ?> field = (Column<?, ?, ?>) orderedField;
                sb.append(storage.getIdentifierFor(field.getEntityName()));
                sb.append(DOT);
                sb.append(field.getColumnName());
                sb.append(SPACE);
                sb.append(order.getOrderKindForPointer(i));
            }

            if (i < order.getCounter() - 1) {
                sb.append(COMMA_SPACE);
            }
        }
        return sb;
    }

    @SuppressWarnings({"rawtypes", "java:S3776"})
    default StringBuilder countToExpression(Count count, String daoIdentifier) {
        StringBuilder sb = new StringBuilder();
        if (count.getCounter() == 1) {
            Object field = count.getObjectForPointer(1);

            if (field == null) {
                sb.append(" *");
            } else if (field instanceof Column) {
                sb.append(((Column) field).getColumnName());
            } else {
                sb.append(field);
            }

            sb.append(SPACE);
        } else {
            for (int i = 1; i < count.getCounter(); i++) {
                Object field = count.getObjectForPointer(i);
                boolean distinct = count.isDistinctForPointer(i) && field != null;

                if (distinct) {
                    sb.append("distinct ");
                }

                if (field == null) {
                    sb.append(daoIdentifier);
                } else if (field instanceof Column) {
                    sb.append(daoIdentifier).append(DOT);
                    sb.append(((Column) field).getColumnName());
                } else {
                    sb.append(field);
                }

                if (i < count.getCounter() - 1) {
                    sb.append(COMMA_SPACE);
                }
            }
        }
        return sb;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776"})
    default StringBuilder whereToExpression(QueryTarget target, Where where, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        String dataBaseEngine = getDataBaseProductName();
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

                boolean isDBQuery;
                if (value instanceof InnerQueryFields) {
                    InnerQueryFields inner = (InnerQueryFields) value;
                    isDBQuery = inner.isDatabaseQuery();
                } else {
                    isDBQuery = (value instanceof QueryExpressionProvider);
                }

                if (isDBQuery) {
                    appendKey(sb, storage, keyFromWrapper, relation);
                    QueryExpressionProvider<?> queryExpressionProvider = (QueryExpressionProvider<?>) value;
                    sb.append(OPEN_BRACKET).append(toSqlQuery((DataBaseQueryBase<? extends Entity, ?>) queryExpressionProvider.getInnerQuery())).append(CLOSED_BRACKET);
                    continue;
                }
                if (value instanceof FieldsProvider) {
                    FieldsProvider fieldsProvider = (FieldsProvider) value;
                    value = fieldsProvider.findMany();
                } else if (value instanceof EntitiesProvider) {
                    EntitiesProvider<?> wr = (EntitiesProvider<?>) value;
                    value = wr.findMany().stream().map(e -> keyFromWrapper.getValueOf((EntityRelation) e)).collect(Collectors.toList());
                }
            }

            if (value == null && (Operator.IS_NULL.equals(relation) || Operator.NOT_NULL.equals(relation))) {
                appendKey(sb, storage, keyFromWrapper, relation);
            } else if (value instanceof ColumnFunction<?, ?, ?, ?>) {
                appendKey(sb, storage, keyFromWrapper, relation);
                sb.append(columnFunctionToExpression((ColumnFunction<?, ?, ?, ?>) value, storage, false));
            } else if (value instanceof Column<?, ?, ?>) {
                appendKey(sb, storage, keyFromWrapper, relation);
                sb.append(storage.getIdentifierForColumn((Column<?, ?, ?>) value));
            } else if (value instanceof Where) {
                Where wr = (Where) value;
                sb.append(SPACE_OPEN_BRACKET).append(whereToExpression(target, wr, storage)).append(CLOSED_BRACKET);
            } else if (value instanceof InnerQueryFields) {
                InnerQueryFields wr = (InnerQueryFields) value;
                appendKey(sb, storage, keyFromWrapper, relation);
                sb.append(toInnerQueryExpression(storage, this, wr));
            } else if (value instanceof Collection || (relation != null && relation.isRelationCollectionBased())) {

                DatabaseTypeConverter typeConverter = target.getConverterManager().getConverter(keyFromWrapper).orElse(null);

                if (value instanceof Collection) {
                    Collection<?> valueCollection = (Collection<?>) value;
                    appendKey(sb, storage, keyFromWrapper, relation);
                    sb.append(convertCollection(valueCollection, typeConverter));
                } else {
                    //w kolekcji moze sie znajdowac tylko jeden element wowczas typ obiektu nie bedzie collection
                    appendKey(sb, storage, keyFromWrapper, relation);
                    sb.append("(").append(typeConverter.convertWritingTarget(value)).append(")");
                }
            } else {
                DatabaseTypeConverter typeConverter = target.getConverterManager().getConverter(keyFromWrapper).orElse(null);
                sb.append(SPACE);
                appendKey(sb, storage, keyFromWrapper, relation);
                valueToSQL(typeConverter, sb, value, target.getDatabaseDateConverter());
            }

            if (relationToNext != null && i < where.getCounter() - 1) {
                sb.append(relationToNext);
            }
        }
        return sb;
    }


    /**
     * Puts a key into the query
     */
    @SuppressWarnings("rawtypes")
    default void appendKey(final StringBuilder sb, IdentifierStorage storage, Column<Entity, Object, EntityRelation> keyFromWrapper, Operator relation) {
        if (keyFromWrapper instanceof ColumnFunction) {
            ColumnFunction<?, ?, ?, ?> function = (ColumnFunction<?, ?, ?, ?>) keyFromWrapper;
            sb.append(columnFunctionToExpression(function, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(keyFromWrapper));
        }

        sb.append(relation);
    }

    default StringBuilder convertCollection(Collection<?> list, DatabaseTypeConverter databaseTypeConverter) {
        StringBuilder sb = new StringBuilder();
        if (list == null || list.isEmpty()) {
            sb.append("('')");
            return sb;
        }
        int counter = 0;
        sb.append(OPEN_BRACKET);

        for (Object value : list) {
            if (value != null) {
                sb.append(databaseTypeConverter.convertWritingTarget(value));
            }
            counter++;
            if (counter < list.size()) {
                sb.append(COMMA);
            }
        }
        sb.append(CLOSED_BRACKET);

        return sb;
    }

    default <E extends Entity, F> StringBuilder toInnerQueryExpression(IdentifierStorage storage, QueryTarget target, InnerQueryFields<E, F> innerQuery) {
        if (innerQuery.getInnerQuery() != null && target.getClass().getName().equals(innerQuery.getInnerQuery().getTarget().getClass().getName())) {
            StringBuilder sb = new StringBuilder();
            sb.append(OPEN_BRACKET)
                    .append(toSqlQuery(innerQuery.getInnerQuery(), storage))
                    .append(CLOSED_BRACKET);
            return sb;
        } else {
            return convertCollection(innerQuery.findMany(), target.getConverterManager().getConverter(innerQuery.getInnerQuery().getSelectedColumn().getColumn()).orElse(null));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S1872", "java:S3740", "java:S3776"})
    default StringBuilder columnFunctionToExpression(ColumnFunction columnFunction, IdentifierStorage storage, boolean internalFunction) {

        if (columnFunction.getClass().getName().equals(CastColumnRelation.class.getName())) {
            CastColumnRelation function = (CastColumnRelation) columnFunction;
            return toCastColumnRelationQueryExpression(function, storage, function.getMode(), function.type);
        }

        if (columnFunction.getClass().getName().equals(ManyArgumentsFunction.class.getName())) {
            ManyArgumentsFunction function = (ManyArgumentsFunction) columnFunction;
            StringBuilder sb = toManyArgumentsFunctionQueryExpression(function, storage, columnFunction.getMode());

            if (!internalFunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
                sb.append(" as ").append(columnFunction.identifier).append(SPACE);
            }
            return sb;
        }
        StringBuilder sb = new StringBuilder();
        boolean table = columnFunction.columns != null;
        if (table) {
            sb.append(columnFunction.getMode()).append(OPEN_BRACKET);
            int counter = 1;

            for (Column<?, ?, ?> col : columnFunction.columns) {
                if (col instanceof ColumnFunction) {
                    ColumnFunction<?, ?, ?, ?> formerColumn = (ColumnFunction<?, ?, ?, ?>) col;
                    sb.append(columnFunctionToExpression(formerColumn, storage, true));
                } else {
                    sb.append(storage.getIdentifierForColumn(col));
                }
                if (counter < columnFunction.columns.length) {
                    sb.append(SPACE).append(columnFunction.mediator).append(SPACE);
                }
                counter++;
            }

            sb.append(CLOSED_BRACKET);
        } else if (columnFunction instanceof DummyColumnRelation) {
            DummyColumnRelation dummy = (DummyColumnRelation) columnFunction;
            sb.append(OPEN_BRACKET)
                    .append(toSqlQuery((DataBaseQueryBase<?, ?>) dummy.getQuery(), new IdentifierStorage()))
                    .append(CLOSED_BRACKET);
        } else if (columnFunction.column == null) {
            sb.append(columnFunction.getMode())
                    .append(OPEN_BRACKET);
            if (columnFunction.query != null) {
                sb.append(toSqlQuery((DataBaseQueryBase<?, ?>) columnFunction.query, new IdentifierStorage()));
            } else {
                sb.append(columnFunction.isNoParameter() ? "" : "*");
            }

            sb.append(CLOSED_BRACKET);
            storage.getIdentifierFor(columnFunction.getEntityName());
        } else {
            sb.append(toColumnFunctionQueryExpression(columnFunction.column, columnFunction.identifier, storage, columnFunction.getMode(), columnFunction.getFunctionMap()));
        }

        if (!internalFunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
            sb.append(" as ").append(columnFunction.identifier).append(SPACE);
        }
        return sb;
    }

    @SuppressWarnings({"rawtypes", "java:S3776"})
    default <E extends Entity, F, R extends EntityRelation> StringBuilder toColumnFunctionQueryExpression(Column<E, F, R> column, String stringIdentifier, IdentifierStorage storage, String mode, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);

        if (column instanceof ColumnFunction) {

            Object obj = params.get(ColumnFunction.BEFORE_COL3);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL2);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(obj, storage))
                        .append(COMMA);
            }

            ColumnFunction<?, ?, ?, ?> formerColumn = (ColumnFunction<?, ?, ?, ?>) column;
            sb.append(columnFunctionToExpression(formerColumn, storage, true));

            obj = params.get(ColumnFunction.AFTER_COL);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL2);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL3);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL4);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }
        } else {
            if (column == null && stringIdentifier != null) {
                sb.append(stringIdentifier);
            }

            Object obj = params.get(ColumnFunction.BEFORE_COL3);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL2);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(obj, storage))
                        .append(COMMA);
            }

            sb.append(storage.getIdentifierForColumn(column));

            obj = params.get(ColumnFunction.AFTER_COL);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL2);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL3);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL4);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }
        }

        sb.append(CLOSED_BRACKET);
        return sb;
    }

    @SuppressWarnings("rawtypes")
    default StringBuilder objectToSomeInFunctions(Object secondColumn, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        if (secondColumn != null) {
            sb.append(SPACE);
            if (secondColumn instanceof ColumnFunction) {
                sb.append(columnFunctionToExpression((ColumnFunction) secondColumn, storage, true));
            } else if (secondColumn instanceof Column) {
                Column col = (Column) secondColumn;

                sb.append(storage.getIdentifierForColumn(col));
            } else if (secondColumn instanceof String) {
                sb.append(APOSTROPHE)
                        .append(valueStringToSQL(secondColumn))
                        .append(APOSTROPHE);
//            }else if (secondColumn instanceof Number){
//                sb.append(secondColumn);
            } else if (secondColumn instanceof FunctionKey) {
                sb.append(((FunctionKey) secondColumn).getKey());
            } else {
                sb.append(secondColumn);
            }
            sb.append(SPACE);
        }
        return sb;
    }

    @SuppressWarnings("rawtypes")
    default <E extends Entity, F, R extends EntityRelation> StringBuilder toCastColumnRelationQueryExpression(Column<E, F, R> column, IdentifierStorage storage, String mode, CastType type) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);
        if (column instanceof ColumnFunction) {
            ColumnFunction<?, ?, ?, ?> formerColumn = (ColumnFunction<?, ?, ?, ?>) column;
            sb.append(columnFunctionToExpression(formerColumn, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(column)).append(" as ").append(type.toString());
        }
        sb.append(CLOSED_BRACKET);
        return sb;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    default <E extends Entity, F, R extends EntityRelation> StringBuilder toManyArgumentsFunctionQueryExpression(Column<E, F, R> column, IdentifierStorage storage, String mode) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);

        ManyArgumentsFunction sdb = (ManyArgumentsFunction) column;
        String separator = (String) sdb.getKeyValue(ColumnFunction.KEY_ARGUMENT);
        List<Object> objects = (List<Object>) sdb.getKeyValue(ColumnFunction.KEY_VALUES);

        int objsize = objects.size();
        for (int i = 0; i < objsize; i++) {
            Object obj = objects.get(i);
            if (obj instanceof ColumnFunction) {
                sb.append(columnFunctionToExpression((ColumnFunction) obj, storage, true));
            } else if (obj instanceof Column) {
                sb.append(storage.getIdentifierForColumn((Column) obj));
            } else {
                sb.append(objectToSomeInFunctions(obj, storage));
            }
            if (i < objsize - 1) {
                sb.append(separator);
            }
        }

        sb.append(CLOSED_BRACKET).append(SPACE);
        return sb;
    }

    @SuppressWarnings("rawtypes")
    default void valueToSQL(DatabaseTypeConverter typeConverter, StringBuilder sb, Object value, DatabaseDateConverter databaseDateConverter) {

        sb.append(typeConverter.convertWritingTarget(value));

    }

    default StringBuilder valueStringToSQL(Object value) {
        StringBuilder sb = new StringBuilder();
        if (value == null) {
            return sb;// do sth??
        }
        String valStr = value.toString();
        String valStrLower = value.toString().toLowerCase();
        if (valStr.contains(";")
                && (valStr.contains(APOSTROPHE) || valStr.contains("\""))
                && (valStrLower.contains("table") || valStrLower.contains("insert") || valStrLower.contains("update") || valStrLower.contains("delete"))) {
            throw new SqlInjectionDetected(valStr);
        }
        sb.append(value.toString().replace(APOSTROPHE, "''"));
        return sb;
    }

    @Override
    //TODO: check null
    default String toCallProcedureSqlQuery(String procedureName, ProcedureParameters input, QueryTarget target) {
        return "call " + procedureName + SPACE + OPEN_BRACKET +
                input.getValues().stream().map(o -> toProcedureSQL(o, new StringBuilder(), target).toString()).collect(Collectors.joining(",")) +
                CLOSED_BRACKET;
    }

}
