package io.daobab.target.database.connection;

import io.daobab.converter.TypeConverter;
import io.daobab.dict.DictDatabaseType;
import io.daobab.error.DaobabException;
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
import io.daobab.target.database.converter.standard.StandardTypeConverterInteger;
import io.daobab.target.database.converter.standard.StandardTypeConverterString;
import io.daobab.target.database.converter.type.DatabaseTypeConverter;
import io.daobab.target.database.query.DataBaseQueryBase;
import io.daobab.target.database.query.DataBaseQueryDelete;
import io.daobab.target.database.query.DataBaseQueryInsert;
import io.daobab.target.database.query.DataBaseQueryUpdate;
import io.daobab.target.database.query.frozen.DaoParam;
import io.daobab.target.database.query.frozen.FrozenQueryProvider;
import io.daobab.target.database.query.frozen.ParameterInjectionPoint;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
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

        if (base._calcJoins) {
            List<String> from = new ArrayList<>();
            from.add(base.getEntityName());
            base.setJoins(JoinTracker.calculateJoins(base.getTarget(), base.getTarget().getTables(), from, (base.getWhereWrapper() == null ? new HashSet<>() : base.getWhereWrapper().getAllDaoInWhereClause(dataBaseTarget)), base.getJoins()));
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
                    .append(orderToExpression(dataBaseTarget, base.getOrderBy(), storage));
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

        sb.append(base.isReplaceInto() ? "replace into " : "insert into ")
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
                }

                if (select) {
                    sb.append(columnName);
                    if (i < base.getSetFields().getCounter() - 1) {
                        sb.append(COMMA).append(SPACE);
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


        if (base.getOnDuplicateKeyUpdate() != null) {

            sb.append(LINE_SEPARATOR).append("on duplicate key update ");
            for (int i = 1; i < base.getOnDuplicateKeyUpdate().getCounter(); i++) {
                Object field = base.getOnDuplicateKeyUpdate().getFieldForPointer(i);

                Object value = base.getOnDuplicateKeyUpdate().getValueForPointer(i);

                if (field instanceof Column) {

                    Column<?, ?, ?> column = (Column) field;
                    String columnName = column.getColumnName();
                    DatabaseTypeConverter typeConverter = base.getTarget().getConverterManager().getConverter(column).orElse(null);

                    if (columnName != null) {
                        sb.append(columnName);
                        sb.append(" = ");

                        if (value == null) {
                            sb.append(NULL);
                        } else if (value instanceof ColumnFunction) {
                            ColumnFunction db = (ColumnFunction) value;
                            sb.append(columnFunctionToExpression(base.getTarget(), db, new IdentifierStorage(), false));
                        } else {
                            sb.append(typeConverter.convertWritingTarget(value));
                            if (typeConverter.needParameterConversion()) {
                                rv.getSpecialParameters().put(rv.getCounter(), value);
                                rv.setCounter(rv.getCounter() + 1);
                            }
                        }
                    }

                    if (i < base.getOnDuplicateKeyUpdate().getCounter() - 1) {
                        sb.append(COMMA);
                    }
                }
            }
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
                    .append(StandardTypeConverterString.valueStringToSQL(val))
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

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776"})
    default <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> base, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        QueryTarget target = base.getTarget();

        if (base.getWhereWrapper() != null) {
            storage.registerIdentifiers(base.getEntityName());
            storage.registerIdentifiers(base.getWhereWrapper().getAllDaoInWhereClause(target));
        }

        if (base.isJoin()) {
            List<String> from = new ArrayList<>();
            from.add(base.getEntityName());
            base.setJoins(JoinTracker.calculateJoins(base.getTarget(), getTables(), from, (base.getWhereWrapper() == null ? new HashSet<>() : base.getWhereWrapper().getAllDaoInWhereClause(target)), base.getJoins()));
        }


        sb.append(LINE_SEPARATOR);
        sb.append("select ");

        if (base.getLimit() != null && base.getLimit().getOffset() == 0 && getDataBaseProductName().startsWith(DictDatabaseType.MicrosoftSQL)) {
            sb.append("top(").append(base.getLimit().getLimit()).append(") ");
        }

        if (base.getFields().isEmpty()) {
            sb.append(storage.getIdentifierFor(base.getEntityName()));
        } else {
            for (Iterator<TableColumn> it = base.getFields().iterator(); it.hasNext(); ) {

                Column<E, ?, ?> column = it.next().getColumn();
                boolean fakeColumn = column.getColumnName() == null;

                if (column instanceof ColumnFunction) {
                    ColumnFunction db = (ColumnFunction) column;
                    sb.append(columnFunctionToExpression(base.getTarget(), db, storage, false));
                } else {
                    if (!fakeColumn) {
                        sb.append(storage.getIdentifierForColumn(base.getTarget(), column));
                    }
                }

                if (it.hasNext() && !fakeColumn) sb.append(COMMA);
            }
        }


        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            storage.getIdentifierForColumn(base.getTarget(), joinWrapper.getByColumn());
            storage.registerIdentifierForJoinClause(getEntityName(joinWrapper.getTable().entityClass()));
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

            //Whole where clause should be separated from rownum pseudocolomn in case of OR/AND operator conflicts
            if (limitAndWhereProvided) sb.append(OPEN_BRACKET);

            if (base.getWhereWrapper() != null) {
                sb.append(whereToExpression(base.getTarget(), base.getWhereWrapper(), storage));
            }

            if (limitAndWhereProvided) sb.append(CLOSED_BRACKET);

            if (base.getLimit() != null) {

                if (DictDatabaseType.ORACLE.equals(getDataBaseProductName())) {
                    //oracle version
                    if (base.getWhereWrapper() != null) sb.append(" and ");
                    sb.append(limitToExpression(base.getLimit(), storage));
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
                    sb.append(storage.getIdentifierForColumn(base.getTarget(), it.next()));
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
                    .append(orderToExpression(base.getTarget(), base.getOrderBy(), storage));
        }

        if (base.getLimit() != null && !DictDatabaseType.ORACLE.equals(getDataBaseProductName())) {
            sb.append(LINE_SEPARATOR)
                    .append(limitToExpression(base.getLimit(), storage));
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
            sb.append(OPEN_BRACKET)
                    .append(toSqlQuery((DataBaseQueryBase<?, ?>) setOperator.getQuery(), new IdentifierStorage()))
                    .append(CLOSED_BRACKET);
        });

        return sb;
    }

    default <E extends Entity> QuerySpecialParameters toUpdateSqlQuery(DataBaseQueryUpdate<E> base) {
        QuerySpecialParameters rv = new QuerySpecialParameters();

        StringBuilder sb = new StringBuilder();

        IdentifierStorage storage = base.getIdentifierStorage();

        sb.append("update ")
                .append(base.getEntityName())
                .append(SPACE)
                .append(storage.getIdentifierFor(base.getEntityName()))
                .append(" set ");

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

            sb.append(storage.getIdentifierFor(target.getEntityName(field.entityClass())))
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

    default StringBuilder limitToExpression(Limit limit, IdentifierStorage storage) {
        String databaseEngine = getDataBaseProductName();
        StringBuilder sb = new StringBuilder();

        if (DictDatabaseType.ORACLE.equals(databaseEngine)) {
            sb.append(" ROWNUM <= '");

            if (limit.isDaoParamInUse()) {
                DaoParam daoParam = limit.getLimitDaoParam();
                toSql(daoParam, new StandardTypeConverterInteger(), sb, storage);
            } else {
                sb.append(limit.getLimit());
            }

            sb.append("' ");
            sb.append(limit.getOffset() > 0 ? "and ROWNUM >" + limit.getOffset() : "");
        } else if (DictDatabaseType.MYSQL.equals(databaseEngine)) {
            if (limit.isDaoParamInUse()) {
                DaoParam daoParam = limit.getLimitDaoParam();
                toSql(daoParam, new StandardTypeConverterInteger(), sb, storage);
            } else {
                sb.append(LIMIT).append(limit.getOffset() > 0 ? limit.getOffset() + COMMA : "").append(limit.getLimit());
            }
        } else if (DictDatabaseType.PostgreSQL.equals(databaseEngine)) {
            sb.append(LIMIT);
            if (limit.isDaoParamInUse()) {
                DaoParam daoParam = limit.getLimitDaoParam();
                toSql(daoParam, new StandardTypeConverterInteger(), sb, storage);
            } else {
                sb.append(limit.getLimit());
            }
            sb.append(SPACE).append(limit.getOffset() > 0 ? "offset " + limit.getOffset() : "");
        } else if (DictDatabaseType.H2.equals(databaseEngine)) {
            sb.append(LIMIT);
            if (limit.isDaoParamInUse()) {
                DaoParam daoParam = limit.getLimitDaoParam();
                toSql(daoParam, new StandardTypeConverterInteger(), sb, storage);
            } else {
                sb.append(limit.getLimit());
            }
            sb.append(SPACE).append(limit.getOffset() > 0 ? "offset " + limit.getOffset() : "");
        } else if (limit.getOffset() > 0 && databaseEngine.startsWith(DictDatabaseType.MicrosoftSQL)) {
            sb.append("offset ").append(limit.getOffset()).append(" rows ").append("fetch next ");
            if (limit.isDaoParamInUse()) {
                DaoParam daoParam = limit.getLimitDaoParam();
                toSql(daoParam, new StandardTypeConverterInteger(), sb, storage);
            } else {
                sb.append(limit.getLimit());
            }
            sb.append(" rows only ");
        }

        return sb;
    }

    default StringBuilder joinToExpression(QueryTarget target, JoinWrapper<?> joinWrapper, IdentifierStorage storage) {
        return new StringBuilder()
                .append(joinWrapper.getType().toString())
                .append(SPACE)
                .append(getEntityName(joinWrapper.getTable().entityClass()))
                .append(SPACE)
                .append(storage.getIdentifierFor(getEntityName(joinWrapper.getTable().entityClass())))
                .append(" on ")
                .append(whereToExpression(target, joinWrapper.getWhere(), storage));
    }

    default StringBuilder orderToExpression(QueryTarget dataBaseTarget, Order order, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < order.getCounter(); i++) {
            Object orderedField = order.getObjectForPointer(i);

            if (orderedField instanceof String) {
                sb.append(orderedField)
                        .append(SPACE)
                        .append(order.getOrderKindForPointer(i));
            } else {
                Column<?, ?, ?> field = (Column<?, ?, ?>) orderedField;
                sb.append(storage.getIdentifierFor(dataBaseTarget.getEntityName(field.entityClass())));
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

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776"})
    default StringBuilder whereToExpression(QueryTarget target, Where where, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        String relationToNext = where.getRelationBetweenExpressions();

        for (int i = 1; i < where.getCounter(); i++) {
            Object value = where.getValueForPointer(i);

            Operator relation = null;
            if (!(value instanceof Where)) {
                relation = where.getRelationForPointer(i);
            }

            @SuppressWarnings("unchecked")
            Column<Entity, Object, RelatedTo> keyFromWrapper = (Column<Entity, Object, RelatedTo>) where.getKeyForPointer(i);

            if (keyFromWrapper != null && value != null) {

                boolean isDBQuery;
                if (value instanceof InnerQueryFields) {
                    InnerQueryFields inner = (InnerQueryFields) value;
                    isDBQuery = inner.isDatabaseQuery();
                } else {
                    isDBQuery = (value instanceof QueryExpressionProvider);
                }

                if (isDBQuery) {
                    appendKey(target, sb, storage, keyFromWrapper, relation);
                    QueryExpressionProvider<?> queryExpressionProvider = (QueryExpressionProvider<?>) value;
                    sb.append(OPEN_BRACKET).append(toSqlQuery((DataBaseQueryBase<? extends Entity, ?>) queryExpressionProvider.getInnerQuery())).append(CLOSED_BRACKET);
                    continue;
                }
                if (value instanceof FieldsProvider) {
                    FieldsProvider fieldsProvider = (FieldsProvider) value;
                    value = fieldsProvider.findMany();
                } else if (value instanceof EntitiesProvider) {
                    EntitiesProvider<?> wr = (EntitiesProvider<?>) value;
                    value = wr.findMany().stream().map(e -> keyFromWrapper.getValueOf((RelatedTo) e)).collect(Collectors.toList());
                }
            }

            if (value == null && (Operator.IS_NULL.equals(relation) || Operator.NOT_NULL.equals(relation))) {
                appendKey(target, sb, storage, keyFromWrapper, relation);
            } else if (value instanceof DaoParam) {
                appendKey(target, sb, storage, keyFromWrapper, relation);
                DaoParam daoParam = (DaoParam) value;
                DatabaseTypeConverter typeConverter = target.getConverterManager().getConverter(keyFromWrapper).orElse(null);
                toSql(daoParam, typeConverter, sb, storage);

            } else if (value instanceof ColumnFunction<?, ?, ?, ?>) {
                appendKey(target, sb, storage, keyFromWrapper, relation);
                sb.append(columnFunctionToExpression(target, (ColumnFunction<?, ?, ?, ?>) value, storage, false));
            } else if (value instanceof Column<?, ?, ?>) {
                appendKey(target, sb, storage, keyFromWrapper, relation);
                sb.append(storage.getIdentifierForColumn(target, (Column<?, ?, ?>) value));
            } else if (value instanceof Where) {
                Where wr = (Where) value;
                sb.append(SPACE_OPEN_BRACKET).append(whereToExpression(target, wr, storage)).append(CLOSED_BRACKET);
            } else if (value instanceof InnerQueryFields) {
                InnerQueryFields wr = (InnerQueryFields) value;
                appendKey(target, sb, storage, keyFromWrapper, relation);
                sb.append(toInnerQueryExpression(storage, this, wr));
            } else if (value instanceof Collection || (relation != null && relation.isRelationCollectionBased())) {

                DatabaseTypeConverter typeConverter = target.getConverterManager().getConverter(keyFromWrapper).orElse(null);

                if (value instanceof Collection) {
                    Collection<?> valueCollection = (Collection<?>) value;
                    appendKey(target, sb, storage, keyFromWrapper, relation);
                    sb.append(convertCollection(valueCollection, typeConverter));
                } else {
                    //w kolekcji moze sie znajdowac tylko jeden element wowczas typ obiektu nie bedzie collection
                    appendKey(target, sb, storage, keyFromWrapper, relation);
                    sb.append("(").append(typeConverter.convertWritingTarget(value)).append(")");
                }
            } else {
                Column<Entity, Object, RelatedTo> keyFromWrapper2;

                if (keyFromWrapper instanceof ColumnHaving && ((ColumnHaving) keyFromWrapper).isIdentifiedAs()) {
                    ColumnHaving columnHaving = (ColumnHaving) keyFromWrapper;
                    Column<Entity, Object, RelatedTo> relevantColumnTakenFromStorage = storage.getColumnByIdentifier(columnHaving.getColumnName());
                    keyFromWrapper2 = relevantColumnTakenFromStorage == null ? keyFromWrapper : relevantColumnTakenFromStorage;
                } else {
                    keyFromWrapper2 = keyFromWrapper;
                }
                DatabaseTypeConverter typeConverter = target.getConverterManager().getConverter(keyFromWrapper2).orElse(null);
                sb.append(SPACE);
                appendKey(target, sb, storage, keyFromWrapper, relation);
                sb.append(typeConverter.convertWritingTarget(value));
            }

            if (relationToNext != null && i < where.getCounter() - 1) {
                sb.append(relationToNext);
            }
        }
        return sb;
    }

    default void toSql(DaoParam daoParam, TypeConverter<?, ?> typeConverter, StringBuilder sb, IdentifierStorage storage) {
        sb.append("~~~")
                .append(daoParam.getKey())
                .append("~~~");
        storage.registerParameter(daoParam, typeConverter);
    }


    /**
     * Puts a key into the query
     */
    @SuppressWarnings("rawtypes")
    default void appendKey(QueryTarget dataBaseTarget, final StringBuilder sb, IdentifierStorage storage, Column<Entity, Object, RelatedTo> keyFromWrapper, Operator relation) {
        if (keyFromWrapper instanceof ColumnFunction) {
            ColumnFunction<?, ?, ?, ?> function = (ColumnFunction<?, ?, ?, ?>) keyFromWrapper;
            sb.append(columnFunctionToExpression(dataBaseTarget, function, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(dataBaseTarget, keyFromWrapper));
        }
        sb.append(relation);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
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
    default StringBuilder columnFunctionToExpression(QueryTarget dataBaseTarget, ColumnFunction columnFunction, IdentifierStorage storage, boolean internalFunction) {

        if (columnFunction.getClass().getName().equals(CastColumnRelation.class.getName())) {
            CastColumnRelation function = (CastColumnRelation) columnFunction;
            return toCastColumnRelationQueryExpression(dataBaseTarget, function, storage, function.getMode(), function.type);
        }

        if (columnFunction.getClass().getName().equals(ManyArgumentsFunction.class.getName())) {
            ManyArgumentsFunction function = (ManyArgumentsFunction) columnFunction;
            StringBuilder sb = toManyArgumentsFunctionQueryExpression(dataBaseTarget, function, storage, columnFunction.getMode());

            if (!internalFunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
                sb.append(" as ").append(columnFunction.identifier).append(SPACE);
                storage.addColumnIdentifiedAsKey(columnFunction.identifier, columnFunction.getFinalColumn());
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
                    sb.append(columnFunctionToExpression(dataBaseTarget, formerColumn, storage, true));
                } else {
                    sb.append(storage.getIdentifierForColumn(dataBaseTarget, col));
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
            storage.getIdentifierFor(dataBaseTarget.getEntityName(columnFunction.entityClass()));
        } else {
            sb.append(toColumnFunctionQueryExpression(dataBaseTarget, columnFunction.column, columnFunction.identifier, storage, columnFunction.getMode(), columnFunction.getFunctionMap()));
        }

        if (!internalFunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
            sb.append(" as ").append(columnFunction.identifier).append(SPACE);
            storage.addColumnIdentifiedAsKey(columnFunction.identifier, columnFunction.getFinalColumn());
        }
        return sb;
    }

    @SuppressWarnings({"rawtypes", "java:S3776"})
    default <E extends Entity, F, R extends RelatedTo> StringBuilder toColumnFunctionQueryExpression(QueryTarget dataBaseTarget, Column<E, F, R> column, String stringIdentifier, IdentifierStorage storage, String mode, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);

        if (column instanceof ColumnFunction) {

            Object obj = params.get(ColumnFunction.BEFORE_COL3);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL2);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage))
                        .append(COMMA);
            }

            ColumnFunction<?, ?, ?, ?> formerColumn = (ColumnFunction<?, ?, ?, ?>) column;
            sb.append(columnFunctionToExpression(dataBaseTarget, formerColumn, storage, true));

            obj = params.get(ColumnFunction.AFTER_COL);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL2);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL3);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL4);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }
        } else {
            if (column == null && stringIdentifier != null) {
                sb.append(stringIdentifier);
            }

            Object obj = params.get(ColumnFunction.BEFORE_COL3);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL2);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage))
                        .append(COMMA);
            }
            obj = params.get(ColumnFunction.BEFORE_COL);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage))
                        .append(COMMA);
            }

            sb.append(storage.getIdentifierForColumn(dataBaseTarget, column));

            obj = params.get(ColumnFunction.AFTER_COL);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL2);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL3);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }

            obj = params.get(ColumnFunction.AFTER_COL4);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
            }
        }

        sb.append(CLOSED_BRACKET);
        return sb;
    }

    @SuppressWarnings("rawtypes")
    default StringBuilder objectToSomeInFunctions(QueryTarget dataBaseTarget, Object secondColumn, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        if (secondColumn != null) {
            sb.append(SPACE);
            if (secondColumn instanceof ColumnFunction) {
                sb.append(columnFunctionToExpression(dataBaseTarget, (ColumnFunction) secondColumn, storage, true));
            } else if (secondColumn instanceof Column) {
                Column col = (Column) secondColumn;

                sb.append(storage.getIdentifierForColumn(dataBaseTarget, col));
            } else if (secondColumn instanceof String) {
                sb.append(APOSTROPHE)
                        .append(StandardTypeConverterString.valueStringToSQL(secondColumn))
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
    default <E extends Entity, F, R extends RelatedTo> StringBuilder toCastColumnRelationQueryExpression(QueryTarget dataBaseTarget, Column<E, F, R> column, IdentifierStorage storage, String mode, CastType type) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);
        if (column instanceof ColumnFunction) {
            ColumnFunction<?, ?, ?, ?> formerColumn = (ColumnFunction<?, ?, ?, ?>) column;
            sb.append(columnFunctionToExpression(dataBaseTarget, formerColumn, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(dataBaseTarget, column)).append(" as ").append(type.toString());
//            storage.addColumnIdentifiedAsKey(columnFunction.identifier,columnFunction.getFinalColumn());
        }
        sb.append(CLOSED_BRACKET);
        return sb;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    default <E extends Entity, F, R extends RelatedTo> StringBuilder toManyArgumentsFunctionQueryExpression(QueryTarget dataBaseTarget, Column<E, F, R> column, IdentifierStorage storage, String mode) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);

        ManyArgumentsFunction sdb = (ManyArgumentsFunction) column;
        String separator = (String) sdb.getKeyValue(ColumnFunction.KEY_ARGUMENT);
        List<Object> objects = (List<Object>) sdb.getKeyValue(ColumnFunction.KEY_VALUES);

        int objSize = objects.size();

        String mediator = sdb.getMediator();
        if (mediator == null) {
            for (int i = 0; i < objSize; i++) {
                Object obj = objects.get(i);

                if (obj instanceof ColumnFunction) {
                    sb.append(columnFunctionToExpression(dataBaseTarget, (ColumnFunction) obj, storage, true));
                } else if (obj instanceof Column) {
                    sb.append(storage.getIdentifierForColumn(dataBaseTarget, (Column) obj));
                } else {
                    sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
                }
                if (i < objSize - 1) {
                    sb.append(separator);
                }
            }
        } else {
            for (int i = 0; i < objSize; i++) {
                Object obj = objects.get(i);

                if (obj instanceof ColumnFunction) {
                    sb.append(columnFunctionToExpression(dataBaseTarget, (ColumnFunction) obj, storage, true));
                } else if (obj instanceof Column) {
                    sb.append(storage.getIdentifierForColumn(dataBaseTarget, (Column) obj));
                } else {
                    sb.append(objectToSomeInFunctions(dataBaseTarget, obj, storage));
                }
                if (i < objSize - 1) {
                    sb.append(mediator);
                }
            }
        }


        sb.append(CLOSED_BRACKET).append(SPACE);

        System.out.println(sb.toString());
        return sb;
    }

    @Override
    //TODO: check null
    default String toCallProcedureSqlQuery(String procedureName, ProcedureParameters input, QueryTarget target) {
        return "call " + procedureName + SPACE + OPEN_BRACKET +
                input.getValues().stream().map(o -> toProcedureSQL(o, new StringBuilder(), target).toString()).collect(Collectors.joining(",")) +
                CLOSED_BRACKET;
    }

    @SuppressWarnings("unchecked")
    default String withParameters(FrozenQueryProvider frozenQueryProvider, List<Object> parameters) {

        List<ParameterInjectionPoint> injectionPoints = frozenQueryProvider.getQueryParametersInjectionPoints();

        if (injectionPoints.size() != parameters.size()) {
            throw new DaobabException("The number of parameters (%s) doesn't match with the query parameters count (%s)", injectionPoints.size(), parameters.size());
        }

        String sqlQuery = frozenQueryProvider.getFrozenQuery();

        for (int i = 0; i < parameters.size(); i++) {
            ParameterInjectionPoint parameterInjectionPoint = injectionPoints.get(i);
            DaoParam daoParam = parameterInjectionPoint.getParam();
            Object valueToPut = parameters.get(i);
            String convertedValueToPut;
            if (daoParam.isCollection()) {
                Collection<Object> collection = (Collection<Object>) valueToPut;
                convertedValueToPut = "(" + collection.stream()
                        .map(v -> parameterInjectionPoint.getTypeConverter().convertWritingTarget(v))
                        .collect(Collectors.joining(",")) + ")";
            } else {
                convertedValueToPut = parameterInjectionPoint.getTypeConverter().convertWritingTarget(valueToPut);
            }
            sqlQuery = sqlQuery.replace("~~~" + daoParam.getKey() + "~~~", convertedValueToPut);
        }

        DataBaseQueryBase<?, ?> query = frozenQueryProvider.getOriginalQuery();
        QueryTarget target = query.getTarget();
        if (target.getShowSql() || query.isLogQueryEnabled()) {
            target.getLog().info(sqlQuery);
        } else {
            target.getLog().debug(sqlQuery);
        }
        return sqlQuery;
    }

}
