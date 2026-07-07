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

    String LINE_SEPARATOR = System.lineSeparator();
    String LIMIT = " limit  ";
    String QUESTION_MARK = "?";
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

    default void logQuery(String query, boolean queryLogEnabled) {
        if (getShowSql() || queryLogEnabled) {
            getLog().info(query);
        } else {
            getLog().debug(query);
        }
    }

    /**
     * Registers the next PreparedStatement parameter into the special parameters container.
     */
    default void addSpecialParameter(QuerySpecialParameters parameters, Object value) {
        parameters.getSpecialParameters().put(parameters.getCounter(), value);
        parameters.setCounter(parameters.getCounter() + 1);
    }


    @SuppressWarnings("java:S3776")
    default <E extends Entity> String toDeleteSqlQuery(DataBaseQueryDelete<E> base) {

        IdentifierStorage storage = base.getIdentifierStorage();
        storage.clearBoundParameters();
        StringBuilder sb = new StringBuilder();
        boolean useAliases = !DictDatabaseType.MYSQL.equals(this.getDataBaseProductName());

        if (base._calcJoins) {
            List<String> from = new ArrayList<>();
            from.add(base.getEntityName());
            base.setJoins(JoinTracker.calculateJoins(this, getTables(), from, (base.getWhereWrapper() == null ? new HashSet<>() : base.getWhereWrapper().getAllDaoInWhereClause(this)), base.getJoins()));
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
                if (useAliases) {
                    sb.append(storage.getIdentifierFor(d));
                }
            }

        }
        sb.append(SPACE);

        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            sb.append(joinToExpression(joinWrapper, storage));
        }

        if (base.getWhereWrapper() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" where ")
                    .append(whereToExpression(base.getWhereWrapper(), storage, useAliases));
        }

        if (base.getOrderBy() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" order by ")
                    .append(orderToExpression(base.getOrderBy(), storage));
        }

        String query = sb.toString();
        logQuery(query, base.isLogQueryEnabled());
        return query;
    }

    @SuppressWarnings("java:S3776")
    default <E extends Entity> QuerySpecialParameters toInsertSqlQuery(DataBaseQueryInsert<E> base) {

        QuerySpecialParameters rv = new QuerySpecialParameters();
        StringBuilder sb = new StringBuilder();

        sb.append(base.isReplaceInto() ? "replace into " : "insert into ")
                .append(base.getEntityName());

        boolean select = base.getSelectQuery() != null;
        SetFields setFields = base.getSetFields();

        if (setFields != null) {

            StringBuilder values = new StringBuilder();

            sb.append(LINE_SEPARATOR).append(OPEN_BRACKET);
            for (int i = 1; i < setFields.getCounter(); i++) {
                Column<?, ?, ?> column = setFields.getFieldForPointer(i);
                String columnName = column.getColumnName();

                DatabaseTypeConverter typeConverter = getConverterManager().getConverter(column).orElse(null);
                Object value;

                if (base.isPkResolved() && columnName != null && columnName.equals(base.getPkColumnName()) && base.getDictIdGenerator().equals(IdGeneratorType.SEQUENCE)) {
                    value = base.getPkNo();
                } else {
                    value = setFields.getValueForPointer(i);
                }

                if (select) {
                    sb.append(columnName);
                    if (i < setFields.getCounter() - 1) {
                        sb.append(COMMA).append(SPACE);
                    }
                } else {
                    if (columnName != null) {
                        sb.append(columnName);

                        if (value == null) {
                            values.append(NULL);
                        } else {
                            values.append(QUESTION_MARK);
                            addSpecialParameter(rv, typeConverter == null ? value : typeConverter.convertWritingParameter(value));
                        }
                    }

                    if (i < setFields.getCounter() - 1) {
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
            DataBaseQueryBase<?, ?> selectQuery = (DataBaseQueryBase<?, ?>) base.getSelectQuery();
            sb.append(toSqlQuery(selectQuery));

            //parameters collected during the inner select generation become PreparedStatement parameters
            for (Object boundParameter : selectQuery.getIdentifierStorage().getBoundParameters()) {
                addSpecialParameter(rv, boundParameter);
            }
        }


        SetFields onDuplicateKeyUpdate = base.getOnDuplicateKeyUpdate();
        if (onDuplicateKeyUpdate != null) {

            sb.append(LINE_SEPARATOR).append("on duplicate key update ");
            for (int i = 1; i < onDuplicateKeyUpdate.getCounter(); i++) {
                Object field = onDuplicateKeyUpdate.getFieldForPointer(i);

                Object value = onDuplicateKeyUpdate.getValueForPointer(i);

                if (field instanceof Column) {

                    Column<?, ?, ?> column = (Column) field;
                    String columnName = column.getColumnName();
                    DatabaseTypeConverter typeConverter = getConverterManager().getConverter(column).orElse(null);

                    if (columnName != null) {
                        sb.append(columnName);
                        sb.append(" = ");

                        if (value == null) {
                            sb.append(NULL);
                        } else if (value instanceof ColumnFunction db) {
                            sb.append(columnFunctionToExpression(db, new IdentifierStorage(), false));
                        } else {
                            sb.append(QUESTION_MARK);
                            addSpecialParameter(rv, typeConverter == null ? value : typeConverter.convertWritingParameter(value));
                        }
                    }

                    if (i < onDuplicateKeyUpdate.getCounter() - 1) {
                        sb.append(COMMA);
                    }
                }
            }
        }

        rv.setQuery(sb);
        return rv;
    }

    //TODO: JavaTime
    //no need TypeConverter here
    default StringBuilder toProcedureSQL(Object val, StringBuilder values, QueryTarget target) {
        if (val == null) {
            values.append(NULL);
        } else if (val instanceof Timestamp valts) {
            values.append(target.getDatabaseDateConverter().toDatabaseTimestamp(valts));
        } else if (val instanceof Time valTime) {
            //Time extends java.util.Date, so it has to be checked before the Date case
            values.append(target.getDatabaseDateConverter().toDatabaseTimestamp(valTime));
        } else if (val instanceof Date valDate) {
            values.append(target.getDatabaseDateConverter().toDatabaseDate(valDate));
        } else if (val instanceof byte[]) {
            values.append("?");
        } else if (val instanceof String) {
            //valueStringToSQL already returns a quoted literal
            values.append(StandardTypeConverterString.valueStringToSQL(val));
        } else {
            values.append(val);
        }
        return values;
    }

    default <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> base) {
        base.getIdentifierStorage().clearBoundParameters();
        return toSqlQuery(base, base.getIdentifierStorage());
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776"})
    default <E extends Entity> String toSqlQuery(DataBaseQueryBase<E, ?> base, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        String databaseEngine = getDataBaseProductName();
        boolean oracle = DictDatabaseType.ORACLE.equals(databaseEngine);
        boolean oracleLimit = oracle && base.getLimit() != null;

        if (base.getWhereWrapper() != null) {
            storage.registerIdentifiers(base.getEntityName());
            storage.registerIdentifiers(base.getWhereWrapper().getAllDaoInWhereClause(this));
        }

        if (base.isJoin()) {
            List<String> from = new ArrayList<>();
            from.add(base.getEntityName());
            base.setJoins(JoinTracker.calculateJoins(this, getTables(), from, (base.getWhereWrapper() == null ? new HashSet<>() : base.getWhereWrapper().getAllDaoInWhereClause(this)), base.getJoins()));
        }


        sb.append(LINE_SEPARATOR);
        sb.append("select ");

        if (base.getLimit() != null && base.getLimit().getOffset() == 0 && databaseEngine.startsWith(DictDatabaseType.MicrosoftSQL)) {
            sb.append("top(").append(base.getLimit().getLimit()).append(") ");
        }

        if (base.getFields().isEmpty()) {
            sb.append(storage.getIdentifierFor(base.getEntityName()));
        } else {
            for (Iterator<TableColumn> it = base.getFields().iterator(); it.hasNext(); ) {

                Column<E, ?, ?> column = it.next().getColumn();
                boolean fakeColumn = column.getColumnName() == null;

                if (column instanceof ColumnFunction db) {
                    sb.append(columnFunctionToExpression(db, storage, false));
                } else {
                    if (!fakeColumn) {
                        sb.append(storage.getIdentifierForColumn(this, column));
                    }
                }

                if (it.hasNext() && !fakeColumn) sb.append(COMMA);
            }
        }


        for (JoinWrapper<?> joinWrapper : base.getJoins()) {
            storage.getIdentifierForColumn(this, joinWrapper.getByColumn());
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
                    .append(joinToExpression(joinWrapper, storage));
        }

        boolean limitAndWhereProvided = base.getWhereWrapper() != null && oracleLimit;

        if ((base.getWhereWrapper() != null && !base.getWhereWrapper().isEmpty()) || oracleLimit) {
            sb.append(LINE_SEPARATOR);
            sb.append(" where ");

            //Whole where clause should be separated from rownum pseudocolomn in case of OR/AND operator conflicts
            if (limitAndWhereProvided) sb.append(OPEN_BRACKET);

            if (base.getWhereWrapper() != null) {
                sb.append(whereToExpression(base.getWhereWrapper(), storage));
            }

            if (limitAndWhereProvided) sb.append(CLOSED_BRACKET);

            if (oracleLimit) {
                if (base.getWhereWrapper() != null) sb.append(" and ");
                sb.append(limitToExpression(base.getLimit(), storage));
            }
        }

        if (base.getSetOperatorList() != null && !base.getSetOperatorList().isEmpty()) {
            sb.append(setOperatorsToExpression(base.getSetOperatorList(), storage));
        }

        if (!base.getGroupBy().isEmpty() || base.getGroupByAlias() != null) {
            sb.append(LINE_SEPARATOR);
            sb.append(" group by ");
            if (base.getGroupByAlias() != null) {
                sb.append(base.getGroupByAlias()).append(SPACE);
            } else {
                for (Iterator<Column<?, ?, ?>> it = base.getGroupBy().iterator(); it.hasNext(); ) {
                    sb.append(storage.getIdentifierForColumn(this, it.next()));
                    if (it.hasNext()) sb.append(COMMA_SPACE);
                }
            }
        }

        if (base.getHavingWrapper() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" having ")
                    .append(whereToExpression(base.getHavingWrapper(), storage));
        }

        if (base.getOrderBy() != null) {
            sb.append(LINE_SEPARATOR)
                    .append(" order by ")
                    .append(orderToExpression(base.getOrderBy(), storage));
        }

        if (base.getLimit() != null && !oracle) {
            sb.append(LINE_SEPARATOR)
                    .append(limitToExpression(base.getLimit(), storage));
        }

        String query = sb.toString();
        logQuery(query, base.isLogQueryEnabled());
        return query;
    }

    default StringBuilder setOperatorsToExpression(List<SetOperator> setOperators, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        if (setOperators == null) return sb;
        setOperators.forEach(setOperator -> {
            sb.append(LINE_SEPARATOR);
            switch (setOperator.getType()) {
                case SetOperator.UNION:
                    sb.append(" union ");
                    break;
                case SetOperator.UNION_ALL:
                    sb.append(" union all ");
                    break;
                case SetOperator.EXCEPT:
                    sb.append(" except ");
                    break;
                case SetOperator.EXCEPT_ALL:
                    sb.append(" except all ");
                    break;
                case SetOperator.INTERSECT:
                    sb.append(" intersect ");
                    break;
                case SetOperator.MINUS:
                    sb.append(" minus ");
                    break;
                default:
                    break;
            }
            IdentifierStorage subQueryStorage = new IdentifierStorage();
            storage.shareParametersWith(subQueryStorage);
            sb.append(OPEN_BRACKET)
                    .append(toSqlQuery((DataBaseQueryBase<?, ?>) setOperator.getQuery(), subQueryStorage))
                    .append(CLOSED_BRACKET);
        });

        return sb;
    }

    default <E extends Entity> QuerySpecialParameters toUpdateSqlQuery(DataBaseQueryUpdate<E> base) {
        QuerySpecialParameters rv = new QuerySpecialParameters();

        StringBuilder sb = new StringBuilder();

        IdentifierStorage storage = base.getIdentifierStorage();
        storage.clearBoundParameters();

        sb.append("update ")
                .append(base.getEntityName())
                .append(SPACE)
                .append(storage.getIdentifierFor(base.getEntityName()))
                .append(" set ");

        if (base.getSetFields() != null) {
            rv = toQuerySpecialParametersExpression(base.getSetFields(), storage);
            sb.append(rv.getQuery());
        }

        if (base.getWhereWrapper() != null) {
            sb.append(" where ");
            sb.append(whereToExpression(base.getWhereWrapper(), storage));
        }

        //values collected during the set and where clauses generation become PreparedStatement parameters
        for (Object boundParameter : storage.getBoundParameters()) {
            addSpecialParameter(rv, boundParameter);
        }

        logQuery(sb.toString(), base.isLogQueryEnabled());

        rv.setQuery(sb);
        return rv;
    }

    default QuerySpecialParameters toQuerySpecialParametersExpression(SetFields setFields, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        QuerySpecialParameters rv = new QuerySpecialParameters();

        for (int i = 1; i < setFields.getCounter(); i++) {
            Column<?, ?, ?> field = setFields.getFieldForPointer(i);
            Object value = setFields.getValueForPointer(i);

            DatabaseTypeConverter typeConverter = getConverterManager().getConverter(field).orElse(null);

            sb.append(storage.getIdentifierFor(getEntityName(field.entityClass())))
                    .append(DOT)
                    .append(field.getColumnName())
                    .append(" = ");

            if (value == null) {
                sb.append(NULL).append(" ");
            } else if (storage.isInlineParameters()) {
                sb.append(typeConverter.convertWritingTarget(value));
                if (typeConverter.needParameterConversion()) {
                    addSpecialParameter(rv, value);
                }
            } else {
                appendValueExpression(sb, value, typeConverter, storage);
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
            appendLimitValue(limit, sb, storage);
            sb.append("' ");
            sb.append(limit.getOffset() > 0 ? "and ROWNUM >" + limit.getOffset() : "");
        } else if (DictDatabaseType.MYSQL.equals(databaseEngine)) {
            if (limit.isDaoParamInUse()) {
                appendLimitValue(limit, sb, storage);
            } else {
                sb.append(LIMIT).append(limit.getOffset() > 0 ? limit.getOffset() + COMMA : "").append(limit.getLimit());
            }
        } else if (DictDatabaseType.PostgreSQL.equals(databaseEngine) || DictDatabaseType.H2.equals(databaseEngine)) {
            sb.append(LIMIT);
            appendLimitValue(limit, sb, storage);
            sb.append(SPACE).append(limit.getOffset() > 0 ? "offset " + limit.getOffset() : "");
        } else if (limit.getOffset() > 0 && databaseEngine.startsWith(DictDatabaseType.MicrosoftSQL)) {
            sb.append("offset ").append(limit.getOffset()).append(" rows ").append("fetch next ");
            appendLimitValue(limit, sb, storage);
            sb.append(" rows only ");
        }

        return sb;
    }

    /**
     * Appends the limit value: either a frozen query parameter marker or the plain number.
     */
    default void appendLimitValue(Limit limit, StringBuilder sb, IdentifierStorage storage) {
        if (limit.isDaoParamInUse()) {
            toSql(limit.getLimitDaoParam(), new StandardTypeConverterInteger(), sb, storage);
        } else {
            sb.append(limit.getLimit());
        }
    }

    default StringBuilder joinToExpression(JoinWrapper<?> joinWrapper, IdentifierStorage storage) {
        return new StringBuilder()
                .append(joinWrapper.getType().toString())
                .append(SPACE)
                .append(getEntityName(joinWrapper.getTable().entityClass()))
                .append(SPACE)
                .append(storage.getIdentifierFor(getEntityName(joinWrapper.getTable().entityClass())))
                .append(" on ")
                .append(whereToExpression(joinWrapper.getWhere(), storage));
    }

    default StringBuilder orderToExpression(Order order, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < order.getCounter(); i++) {
            Object orderedField = order.getObjectForPointer(i);

            if (orderedField instanceof String) {
                sb.append(orderedField)
                        .append(SPACE)
                        .append(order.getOrderKindForPointer(i));
            } else {
                Column<?, ?, ?> field = (Column<?, ?, ?>) orderedField;
                sb.append(storage.getIdentifierFor(getEntityName(field.entityClass())));
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
    default StringBuilder whereToExpression(Where where, IdentifierStorage storage) {
        return whereToExpression(where, storage, true);
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S3776"})
    default StringBuilder whereToExpression(Where where, IdentifierStorage storage, boolean useAliases) {
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
                if (value instanceof InnerQueryFields inner) {
                    isDBQuery = inner.isDatabaseQuery();
                } else {
                    isDBQuery = (value instanceof QueryExpressionProvider);
                }

                if (isDBQuery) {
                    appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                    QueryExpressionProvider<?> queryExpressionProvider = (QueryExpressionProvider<?>) value;
                    DataBaseQueryBase<? extends Entity, ?> innerQueryBase = queryExpressionProvider.getInnerQuery();
                    storage.shareParametersWith(innerQueryBase.getIdentifierStorage());
                    sb.append(OPEN_BRACKET).append(toSqlQuery(innerQueryBase)).append(CLOSED_BRACKET);
                    continue;
                }
                if (value instanceof FieldsProvider fieldsProvider) {
                    value = fieldsProvider.findMany();
                } else if (value instanceof EntitiesProvider<?> wr) {
                    value = wr.findMany().stream().map(e -> keyFromWrapper.getValueOf((RelatedTo) e)).collect(Collectors.toList());
                }
            }

            if (value == null && (Operator.IS_NULL.equals(relation) || Operator.NOT_NULL.equals(relation))) {
                appendKey(sb, storage, keyFromWrapper, relation, useAliases);
            } else if (value instanceof DaoParam daoParam) {
                appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                DatabaseTypeConverter typeConverter = getConverterManager().getConverter(keyFromWrapper).orElse(null);
                toSql(daoParam, typeConverter, sb, storage);

            } else if (value instanceof ColumnFunction<?, ?, ?, ?>) {
                appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                sb.append(columnFunctionToExpression((ColumnFunction<?, ?, ?, ?>) value, storage, false));
            } else if (value instanceof Column<?, ?, ?>) {
                appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                sb.append(storage.getIdentifierForColumn(this, (Column<?, ?, ?>) value, useAliases));
            } else if (value instanceof Where wr) {
                sb.append(SPACE_OPEN_BRACKET).append(whereToExpression(wr, storage, useAliases)).append(CLOSED_BRACKET);
            } else if (value instanceof InnerQueryFields wr) {
                appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                sb.append(toInnerQueryExpression(storage, wr));
            } else if (value instanceof Collection || (relation != null && relation.isRelationCollectionBased())) {

                DatabaseTypeConverter typeConverter = getConverterManager().getConverter(keyFromWrapper).orElse(null);

                if (value instanceof Collection<?> valueCollection) {
                    appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                    sb.append(convertCollection(valueCollection, typeConverter, storage));
                } else {
                    //w kolekcji moze sie znajdowac tylko jeden element wowczas typ obiektu nie bedzie collection
                    appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                    sb.append(OPEN_BRACKET);
                    appendValueExpression(sb, value, typeConverter, storage);
                    sb.append(CLOSED_BRACKET);
                }
            } else {
                Column<Entity, Object, RelatedTo> keyFromWrapper2;

                if (keyFromWrapper instanceof ColumnHaving columnHaving && columnHaving.isIdentifiedAs()) {
                    Column<Entity, Object, RelatedTo> relevantColumnTakenFromStorage = storage.getColumnByIdentifier(columnHaving.getColumnName());
                    keyFromWrapper2 = relevantColumnTakenFromStorage == null ? keyFromWrapper : relevantColumnTakenFromStorage;
                } else {
                    keyFromWrapper2 = keyFromWrapper;
                }
                DatabaseTypeConverter typeConverter = getConverterManager().getConverter(keyFromWrapper2).orElse(null);
                sb.append(SPACE);
                appendKey(sb, storage, keyFromWrapper, relation, useAliases);
                appendValueExpression(sb, value, typeConverter, storage);
            }

            if (relationToNext != null && i < where.getCounter() - 1) {
                sb.append(relationToNext);
            }
        }
        return sb;
    }

    /**
     * Renders a single value into the query.
     * By default a '?' placeholder is appended and the value is collected in the storage,
     * to be bound later on the PreparedStatement.
     * When the storage works in the inline mode (frozen queries), the value is rendered
     * as a SQL literal like before.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    default void appendValueExpression(StringBuilder sb, Object value, DatabaseTypeConverter typeConverter, IdentifierStorage storage) {
        if (storage.isInlineParameters()) {
            sb.append(typeConverter.convertWritingTarget(value));
        } else {
            sb.append(QUESTION_MARK);
            storage.addBoundParameter(typeConverter == null ? value : typeConverter.convertWritingParameter(value));
        }
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
    default void appendKey(final StringBuilder sb, IdentifierStorage storage, Column<Entity, Object, RelatedTo> keyFromWrapper, Operator relation, boolean useAliases) {
        if (keyFromWrapper instanceof ColumnFunction<?, ?, ?, ?> function) {
            sb.append(columnFunctionToExpression(function, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(this, keyFromWrapper, useAliases));
        }
        sb.append(relation);
    }

    @SuppressWarnings("rawtypes")
    default StringBuilder convertCollection(Collection<?> list, DatabaseTypeConverter databaseTypeConverter, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        if (list == null || list.isEmpty()) {
            sb.append("('')");
            return sb;
        }
        int counter = 0;
        sb.append(OPEN_BRACKET);

        for (Object value : list) {
            if (value != null) {
                appendValueExpression(sb, value, databaseTypeConverter, storage);
            }
            counter++;
            if (counter < list.size()) {
                sb.append(COMMA);
            }
        }
        sb.append(CLOSED_BRACKET);

        return sb;
    }

    default <E extends Entity, F> StringBuilder toInnerQueryExpression(IdentifierStorage storage, InnerQueryFields<E, F> innerQuery) {
        if (innerQuery.getInnerQuery() != null && getClass().getName().equals(innerQuery.getInnerQuery().getTarget().getClass().getName())) {
            StringBuilder sb = new StringBuilder();
            sb.append(OPEN_BRACKET)
                    .append(toSqlQuery(innerQuery.getInnerQuery(), storage))
                    .append(CLOSED_BRACKET);
            return sb;
        } else {
            return convertCollection(innerQuery.findMany(), getConverterManager().getConverter(innerQuery.getInnerQuery().getSelectedColumn().getColumn()).orElse(null), storage);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked", "java:S1872", "java:S3740", "java:S3776"})
    default StringBuilder columnFunctionToExpression(ColumnFunction columnFunction, IdentifierStorage storage, boolean internalFunction) {

        if (columnFunction.getClass().getName().equals(CastColumnRelation.class.getName())) {
            CastColumnRelation function = (CastColumnRelation) columnFunction;
            return toCastColumnRelationQueryExpression(this, function, storage, function.getMode(), function.type);
        }

        if (columnFunction.getClass().getName().equals(ManyArgumentsFunction.class.getName())) {
            ManyArgumentsFunction function = (ManyArgumentsFunction) columnFunction;
            StringBuilder sb = toManyArgumentsFunctionQueryExpression(this, function, storage, columnFunction.getMode());
            appendFunctionIdentifier(sb, columnFunction, storage, internalFunction);
            return sb;
        }
        StringBuilder sb = new StringBuilder();
        boolean table = columnFunction.columns != null;
        if (table) {
            sb.append(columnFunction.getMode()).append(OPEN_BRACKET);
            int counter = 1;

            for (Column<?, ?, ?> col : columnFunction.columns) {
                if (col instanceof ColumnFunction<?, ?, ?, ?> formerColumn) {
                    sb.append(columnFunctionToExpression(formerColumn, storage, true));
                } else {
                    sb.append(storage.getIdentifierForColumn(this, col));
                }
                if (counter < columnFunction.columns.length) {
                    sb.append(SPACE).append(columnFunction.mediator).append(SPACE);
                }
                counter++;
            }

            sb.append(CLOSED_BRACKET);
        } else if (columnFunction instanceof DummyColumnRelation dummy) {
            IdentifierStorage subQueryStorage = new IdentifierStorage();
            storage.shareParametersWith(subQueryStorage);
            sb.append(OPEN_BRACKET)
                    .append(toSqlQuery((DataBaseQueryBase<?, ?>) dummy.getQuery(), subQueryStorage))
                    .append(CLOSED_BRACKET);
        } else if (columnFunction.column == null) {
            sb.append(columnFunction.getMode())
                    .append(OPEN_BRACKET);
            if (columnFunction.query != null) {
                IdentifierStorage subQueryStorage = new IdentifierStorage();
                storage.shareParametersWith(subQueryStorage);
                sb.append(toSqlQuery((DataBaseQueryBase<?, ?>) columnFunction.query, subQueryStorage));
            } else {
                sb.append(columnFunction.isNoParameter() ? "" : "*");
            }

            sb.append(CLOSED_BRACKET);
            storage.getIdentifierFor(this.getEntityName(columnFunction.entityClass()));
        } else {
            sb.append(toColumnFunctionQueryExpression(columnFunction.column, columnFunction.identifier, storage, columnFunction.getMode(), columnFunction.getFunctionMap()));
        }

        appendFunctionIdentifier(sb, columnFunction, storage, internalFunction);
        return sb;
    }

    /**
     * Appends the 'as identifier' suffix and registers the identifier, unless the function is nested.
     */
    @SuppressWarnings("rawtypes")
    default void appendFunctionIdentifier(StringBuilder sb, ColumnFunction columnFunction, IdentifierStorage storage, boolean internalFunction) {
        if (!internalFunction && columnFunction.identifier != null && !columnFunction.identifier.trim().isEmpty()) {
            sb.append(" as ").append(columnFunction.identifier).append(SPACE);
            storage.addColumnIdentifiedAsKey(columnFunction.identifier, columnFunction.getFinalColumn());
        }
    }

    @SuppressWarnings({"rawtypes"})
    default <E extends Entity, F, R extends RelatedTo> StringBuilder toColumnFunctionQueryExpression(Column<E, F, R> column, String stringIdentifier, IdentifierStorage storage, String mode, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append(OPEN_BRACKET);

        if (column == null && stringIdentifier != null) {
            sb.append(stringIdentifier);
        }

        appendFunctionArgumentsBefore(sb, params, storage);

        if (column instanceof ColumnFunction formerColumn) {
            sb.append(columnFunctionToExpression(formerColumn, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(this, column));
        }

        appendFunctionArgumentsAfter(sb, params, storage);

        sb.append(CLOSED_BRACKET);
        return sb;
    }

    /**
     * Appends the function arguments placed before the column, each followed by a comma.
     */
    default void appendFunctionArgumentsBefore(StringBuilder sb, Map<String, Object> params, IdentifierStorage storage) {
        for (String key : new String[]{ColumnFunction.BEFORE_COL3, ColumnFunction.BEFORE_COL2, ColumnFunction.BEFORE_COL}) {
            Object obj = params.get(key);
            if (obj != null) {
                sb.append(objectToSomeInFunctions(obj, storage))
                        .append(COMMA);
            }
        }
    }

    /**
     * Appends the function arguments placed after the column, each preceded by a comma.
     */
    default void appendFunctionArgumentsAfter(StringBuilder sb, Map<String, Object> params, IdentifierStorage storage) {
        for (String key : new String[]{ColumnFunction.AFTER_COL, ColumnFunction.AFTER_COL2, ColumnFunction.AFTER_COL3, ColumnFunction.AFTER_COL4}) {
            Object obj = params.get(key);
            if (obj != null) {
                sb.append(SPACE_COMMA)
                        .append(objectToSomeInFunctions(obj, storage));
            }
        }
    }

    @SuppressWarnings("rawtypes")
    default StringBuilder objectToSomeInFunctions(Object secondColumn, IdentifierStorage storage) {
        StringBuilder sb = new StringBuilder();
        if (secondColumn != null) {
            sb.append(SPACE);
            if (secondColumn instanceof ColumnFunction secondColumn2) {
                sb.append(columnFunctionToExpression(secondColumn2, storage, true));
            } else if (secondColumn instanceof Column col) {
                sb.append(storage.getIdentifierForColumn(this, col));
            } else if (secondColumn instanceof String) {
                //valueStringToSQL already returns a quoted literal
                sb.append(StandardTypeConverterString.valueStringToSQL(secondColumn));
            } else if (secondColumn instanceof FunctionKey sc) {
                sb.append((sc).getKey());
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
        if (column instanceof ColumnFunction<?, ?, ?, ?> formerColumn) {
            sb.append(columnFunctionToExpression(formerColumn, storage, true));
        } else {
            sb.append(storage.getIdentifierForColumn(dataBaseTarget, column)).append(" as ").append(type.toString());
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
        String joiner = mediator == null ? separator : mediator;
        for (int i = 0; i < objSize; i++) {
            Object obj = objects.get(i);

            if (obj instanceof ColumnFunction cf) {
                sb.append(columnFunctionToExpression(cf, storage, true));
            } else if (obj instanceof Column col) {
                sb.append(storage.getIdentifierForColumn(dataBaseTarget, col));
            } else {
                sb.append(objectToSomeInFunctions(obj, storage));
            }
            if (i < objSize - 1) {
                sb.append(joiner);
            }
        }

        sb.append(CLOSED_BRACKET).append(SPACE);
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

        DataBaseQueryBase<?, ?> query = frozenQueryProvider.unfreeze();
        QueryTarget target = query.getTarget();
        if (target.getShowSql() || query.isLogQueryEnabled()) {
            target.getLog().info(sqlQuery);
        } else {
            target.getLog().debug(sqlQuery);
        }
        return sqlQuery;
    }

}
