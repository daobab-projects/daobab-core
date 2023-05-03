package io.daobab.result;

import io.daobab.model.*;
import io.daobab.query.base.Query;
import io.daobab.result.predicate.AlwaysTrue;
import io.daobab.result.predicate.GeneralWhereAnd;
import io.daobab.result.predicate.GeneralWhereOr;
import io.daobab.result.remove.Index;
import io.daobab.result.remove.IndexDate;
import io.daobab.result.remove.IndexNumber;
import io.daobab.result.remove.IndexString;
import io.daobab.statement.condition.Operator;
import io.daobab.statement.where.base.Where;
import io.daobab.statement.where.base.WhereBase;
import io.daobab.target.buffer.single.EntityList;

import java.util.*;
import java.util.function.Predicate;

import static io.daobab.statement.where.base.WhereBase.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public abstract class EntitiesBufferIndexed<E extends Entity> extends ListProxy<E> {

    private final TreeMap<Number, FakePkEntity<Number, E>> valueMap = new TreeMap<>();
    private final Map<String, Index<E, ?>> indexRepository = new HashMap<>();
    private boolean primaryKey = false;

    protected EntitiesBufferIndexed() {
        this(new ArrayList<>());
    }

    protected EntitiesBufferIndexed(List<E> entities) {
        super(entities);

        if (!entities.isEmpty()) {
            E entity = entities.get(0);
            setPrimaryKey(entity instanceof PrimaryKey);
        }
    }

    private Optional<Index<E, ?>> getIndexFor(Column<E, ?, ?> column) {
        return indexRepository.entrySet().stream()
                .filter(entry -> column.getColumnName().equals(entry.getKey()))
                .findAny()
                .map(Map.Entry::getValue);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<E> finalFilter(List<E> entities, Query<E, ?, ?> query, List<Integer> skipSteps) {
        int counter = 0;

        List<E> rv = new ArrayList<>();
        Where wrapper = query.getWhereWrapper();
        boolean useLimit = query.getOrderBy() == null && query.getLimit() != null;
        boolean useWhere = wrapper != null;

        int offset = !useLimit ? 0 : query.getLimit().getOffset();
        int limit = !useLimit ? 0 : query.getLimit().getLimit() > 0 ? (offset + query.getLimit().getLimit()) : Integer.MAX_VALUE;

        Predicate<Object> generalPredicate;
        if (useWhere) {
            if (WhereBase.OR.equals(wrapper.getRelationBetweenExpressions())) {
                GeneralWhereOr genPred = new GeneralWhereOr<>(wrapper, skipSteps);
                generalPredicate = genPred.isEmpty() ? new AlwaysTrue() : genPred;
            } else {
                GeneralWhereAnd genPred = new GeneralWhereAnd<>(wrapper, skipSteps);
                generalPredicate = genPred.isEmpty() ? new AlwaysTrue() : genPred;
            }
        } else {
            generalPredicate = new AlwaysTrue();
        }

        if (useLimit) {
            for (E entity : entities) {
                if (generalPredicate.test(entity)) {
                    counter++;
                    if (counter < offset) {
                        continue;
                    } else if (counter >= limit) {
                        break;
                    }
                    rv.add(entity);
                }
            }
        } else {
            entities.stream().filter(generalPredicate).forEach(rv::add);
        }
        return rv;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<E> filter(Query<E, ?, ?> query) {
        Where wrapper = query.getWhereWrapper();

        //if indexRepository is empty, don't even use the index logic
        if (indexRepository.isEmpty()) {
            return finalFilter(entities, query, Collections.emptyList());
        }

        if (OR.equals(wrapper.getRelationBetweenExpressions())) {
            boolean allOr = true;
            for (int counter = 1; counter < wrapper.getCounter(); counter++) {
                Column column = wrapper.getKeyForPointer(counter);
                if (!wrapper.mayBeIndexedForPointer(counter) || !getIndexFor(column).isPresent()) {
                    allOr = false;
                    break;
                }
            }

            if (!allOr) {
                return finalFilter(entities, query, Collections.emptyList());
            }
        }

        //seems like there is a reason to use indexes....
        ResultEntitiesWithSkipStepsWrapper<E> resultWrapper = filter(wrapper);

        //if there is some result as a set of PK, we have to convert it to whole entities with eventual limit
        return finalFilter(resultWrapper.getEntities(), query, resultWrapper.getSkipSteps());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private ResultEntitiesWithSkipStepsWrapper<E> filter(Where wrapper) {
        String relations = wrapper.getRelationBetweenExpressions();
        List<E> result = new LinkedList<>();
        List<Number> resultPk = new LinkedList<>();
        List<Integer> skipSteps = new LinkedList<>();

        for (int counter = 1; (counter < wrapper.getCounter()) && result.isEmpty(); counter++) {
            if (!wrapper.mayBeIndexedForPointer(counter)) {
                continue;
            }

            Column column = wrapper.getKeyForPointer(counter);

            if (column == null) {
                continue;
            }

            Optional<Index<E, Number>> indexOpt = getIndexFor(column);
            if (!indexOpt.isPresent()) {
                continue;
            }
            skipSteps.add(counter);

            Index<E, ?> index = indexOpt.get();

            //if Where has a second Where inside
            Where innerWhere = wrapper.getInnerWhere(counter);
            if (innerWhere != null) {
                ResultEntitiesWithSkipStepsWrapper<E> pks = filter(innerWhere);
                if (OR.equals(relations)) {
                    result.addAll(pks.getEntities());
                } else if (AND.equals(relations)) {
                    if (result.isEmpty()) {
                        result.addAll(pks.getEntities());
                    } else {
                        result.removeIf(pkres -> !pks.getEntities().contains(pkres));
                    }
                } else if (NOT.equals(relations)) {
                    if (result.isEmpty()) {
                        result.addAll(pks.getEntities());
                    } else {
                        result.removeIf(pkres -> pks.getEntities().contains(pkres));
                    }
                }
                continue;
            }

            Operator operator = wrapper.getRelationForPointer(counter);
            Object val = wrapper.getValueForPointer(counter);

            switch (relations) {
                case AND: {
                    result = index.filter(operator, val);
                    break;
                }
                case OR: {
//                    List<Number> orlist=index.filterPk(operator, val);
                    resultPk.addAll(index.filterPk(operator, val));
                    break;
                }
                case NOT: {
                    result = index.filterNegative(operator, val);
                    break;
                }
            }
//            result = NOT.equalsIgnoreCase(relations) ? index.filterNegative(operator, val) : index.filter(operator, val);
        }

        if (OR.equals(relations)) {
            SortedSet<Number> pkListFromOrClause = new TreeSet<>(resultPk);
            for (Number pk : pkListFromOrClause) {
                result.add(valueMap.get(pk).getEntity());
            }
        }

        //END_OF_WHERE_COUNT

        return new ResultEntitiesWithSkipStepsWrapper<>(result, skipSteps);
    }

    public Set<Number> getKeys() {
        return valueMap.keySet();
    }

    public Collection<FakePkEntity<Number, E>> getValues() {
        return valueMap.values();
    }

    //TODO: move up
    public EntityList<E> calculateIndexes() {
        E entity = entities.get(0);
        List<TableColumn> columns = entity.columns();
        for (TableColumn tableColumn : columns) {

            Column<E, ?, EntityRelation> column = (Column<E, ?, EntityRelation>) tableColumn.getColumn();
            Index index;
            if (Number.class.isAssignableFrom(column.getFieldClass())) {
                index = new IndexNumber<>(column, this);
                if (!index.isWorthless()) indexRepository.put(column.getColumnName(), index);
            } else if (String.class.isAssignableFrom(column.getFieldClass()) || column.getFieldClass().isEnum()) {
                index = new IndexString<>(column, this);
                if (!index.isWorthless()) indexRepository.put(column.getColumnName(), index);
            } else if (Date.class.isAssignableFrom(column.getFieldClass())) {
                index = new IndexDate<>(column, this);
                if (!index.isWorthless()) indexRepository.put(column.getColumnName(), index);
            }
        }
        return (EntityList<E>) this;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

}
