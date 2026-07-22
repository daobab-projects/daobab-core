package io.daobab.statement.where.base;

import io.daobab.error.DaobabException;
import io.daobab.error.MandatoryColumn;
import io.daobab.generator.DictRemoteKey;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.query.marschal.Marshaller;
import io.daobab.statement.condition.Operator;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereNot;
import io.daobab.statement.where.WhereOr;
import io.daobab.target.Target;

import java.util.*;

/**
 * The storage and lifecycle backing a {@link Where} clause.
 * <p>
 * Conditions are kept positionally in a map keyed by {@code key<n>} / {@code value<n>} / {@code relation<n>} /
 * {@code WRAPPER<n>}, where {@code n} runs from 1 up to {@link #getCounter()}. Each pointer {@code n} holds a
 * column, its {@link Operator} and the value (or, in the wrapper slot, a nested clause). On top of that storage
 * this class provides remote (de)serialization ({@link #fromRemote}/{@link #toMap()}) and the selectivity
 * {@link #optimize() optimization}.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused", "UnusedReturnValue"})
public abstract class WhereBase {

    /**
     * The SQL {@code AND} relation.
     */
    public static final String AND = " and ";
    /** The SQL {@code OR} relation. */
    public static final String OR = " or ";
    /** The SQL {@code NOT} relation. */
    public static final String NOT = " not ";
    /** Map key prefix for a nested clause. */
    protected static final String WRAPPER = "WRAPPER";
    /** Map key prefix for a condition column. */
    protected static final String KEY = "key";
    /** Map key prefix for a condition value. */
    protected static final String VALUE = "value";
    /** Map key prefix for a condition operator. */
    protected static final String RELATION = "relation";
    /** Map key prefix marking an already processed condition. */
    protected static final String ALREADY_PROCEEDED = "proc";
    protected static final String DOT = ".";
    /** Map key prefix marking a condition that may use a buffer index. */
    protected static final String MAY_BE_INDEXED_IN_BUFFER = "index";
    private Map<String, Object> whereMap = new HashMap<>();
    private int counter = 1;
    private long optimisationWage = 0;

    /**
     * Rebuilds a where clause from its remote map representation (the inverse of {@link #toMap()}), resolving
     * the marshalled columns against the given target.
     *
     * @param target the target the columns are resolved against
     * @param map    the remote representation
     * @return the rebuilt clause, or {@code null} when the map is empty
     * @throws DaobabException if the relation between the expressions is invalid
     */
    @SuppressWarnings("unchecked")
    public static Where<?> fromRemote(Target target, Map<String, Object> map) {
        if (map == null || map.isEmpty()) return null; //TODO: Exception

        Where<?> rv;
        String relation = (String) map.get(DictRemoteKey.REL_BETWEEN_EXPRESSIONS);
        if (AND.equals(relation)) {
            rv = new WhereAnd();
        } else if (OR.equals(relation)) {
            rv = new WhereOr();
        } else if (NOT.equals(relation)) {
            rv = new WhereNot();
        } else {
            throw new DaobabException("Invalid relation: " + relation);
        }

        boolean conditionsEnd = false;

        while (!conditionsEnd) {
            Object key = map.get(KEY + rv.getCounter());
            Object wrapper = map.get(WRAPPER + rv.getCounter());
            String operator = (String) map.get(RELATION + rv.getCounter());
            Object val = map.get(VALUE + rv.getCounter());

            if (key == null && wrapper == null) {
                conditionsEnd = true;
                break;
            }
            if (key instanceof Map) {
                Column<?, ?, ?> keyColumn = Marshaller.fromRemote(target, (Map<String, Object>) key);
                if (keyColumn == null) {
                    throw new MandatoryColumn();
                } else {
                    rv.put(KEY + rv.getCounter(), keyColumn);
                }
            }

            if (wrapper != null) {
                rv.put(WRAPPER + rv.getCounter(), fromRemote(target, (Map<String, Object>) wrapper));
                rv.put(VALUE + rv.getCounter(), fromRemote(target, (Map<String, Object>) wrapper));
            } else if (val != null) {
                rv.put(VALUE + rv.getCounter(), val);
            }
            if (operator != null) {
                rv.put(RELATION + rv.getCounter(), Operator.valueOf(operator));
            }

            rv.setCounter(rv.getCounter() + 1);
        }
        return rv;
    }

    /**
     * Copies the counter and a shallow copy of the condition map from one clause into another.
     *
     * @param from the source clause
     * @param to   the destination clause
     * @param <W>  the clause type
     * @return {@code to}, or {@code null} when either argument is {@code null}
     */
    public static <W extends WhereBase> W clone(W from, W to) {
        if (from == null || to == null) {
            return null;
        }
        to.setCounter(from.getCounter());
        to.setWhereMap(new HashMap<>(from.getWhereMap()));
        return to;
    }

    /**
     * Removes the condition (column, value, operator and any nested clause) stored at the given pointer.
     *
     * @param where the clause to remove from
     * @param key   the pointer to remove
     * @param <W>   the clause type
     * @return the same clause
     */
    public static <W extends Where> Where<W> get(Where<W> where, int key) {

        where.getWhereMap().remove(WRAPPER + key);
        where.getWhereMap().remove(KEY + key);
        where.getWhereMap().remove(VALUE + key);
        where.getWhereMap().remove(RELATION + key);
        return where;
    }

    /**
     * Whether, after {@link #optimize() optimization}, the most selective condition is a primary-key lookup
     * (its weight is below the foreign-key threshold).
     */
    public boolean startsFromPK() {
        return optimisationWage > 0 && optimisationWage < 200;
    }

    /**
     * Stores a raw entry in the condition map.
     */
    protected void put(String key, Object value) {
        getWhereMap().put(key, value);
    }

    /**
     * The next free pointer; the conditions occupy the pointers {@code 1 .. counter - 1}.
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets the next free pointer.
     */
    protected void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * The column of the condition at the given pointer, or {@code null} when there is none.
     */
    public Column<?, ?, ?> getKeyForPointer(int pointer) {
        return (Column<?, ?, ?>) getWhereMap().get(KEY + pointer);
    }

    /**
     * The nested clause stored in the wrapper slot at the given pointer, or {@code null} when there is none.
     */
    public Where<?> getInnerWhere(int pointer) {
        return (Where<?>) getWhereMap().get(WRAPPER + pointer);
    }

    /**
     * The value of the condition at the given pointer.
     */
    public Object getValueForPointer(int pointer) {
        return getWhereMap().get(VALUE + pointer);
    }

    /**
     * Whether the condition at the given pointer may be resolved through a buffer index (numeric equality).
     */
    public boolean mayBeIndexedForPointer(int pointer) {
        return getWhereMap().containsKey(MAY_BE_INDEXED_IN_BUFFER + pointer);
    }

    /**
     * The operator of the condition at the given pointer.
     */
    public Operator getRelationForPointer(int pointer) {
        return (Operator) getWhereMap().get(RELATION + pointer);
    }

    /**
     * The SQL operator ({@link #AND}, {@link #OR} or {@link #NOT}) joining this clause's conditions.
     */
    public abstract String getRelationBetweenExpressions();

    /**
     * The raw condition map.
     */
    public Map<String, Object> getWhereMap() {
        return whereMap;
    }

    /**
     * Replaces the raw condition map.
     */
    protected void setWhereMap(Map<String, Object> whereMap) {
        this.whereMap = whereMap;
    }

    /**
     * Serializes this clause into a remote-friendly map: columns and entities are marshalled, nested clauses
     * are recursed into. The inverse of {@link #fromRemote}.
     *
     * @return the remote representation
     */
    public Map<String, Object> toMap() {
        Map<String, Object> rv = new HashMap<>();
        rv.put(DictRemoteKey.REL_BETWEEN_EXPRESSIONS, getRelationBetweenExpressions());
        for (Map.Entry<String, Object> entry : whereMap.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Entity) {
                rv.put(entry.getKey(), Marshaller.marshalEntity((Entity) val));
            } else if (val instanceof Column) {
                rv.put(entry.getKey(), Marshaller.marshallColumnToString((Column) val));
            } else if (val instanceof Where) {
                rv.put(entry.getKey(), ((Where<?>) val).toMap());
            } else {
                rv.put(entry.getKey(), val);
            }
        }
        return rv;
    }

    /**
     * Reorders the conditions by selectivity (see {@link OptymalisationWeight}) so the cheapest ones - e.g. a
     * primary-key equality - are evaluated first, recursing into nested clauses. Runs at most once: a clause
     * already optimized (non-zero weight) is left untouched.
     */
    public void optimize() {
        if (optimisationWage > 0) {
            return;
        }
        Map<Long, List<Integer>> map = new HashMap<>();
        for (int i = 1; i < getCounter(); i++) {
            Column key = getKeyForPointer(i);
            //Object value = getValueForPointer(i);
            Operator rel = getRelationForPointer(i);
            long optimalisationWeight = OptymalisationWeight.getColumnWeight(key) * OptymalisationWeight.getOperatorWeight(rel);

            if (map.containsKey(optimalisationWeight)) {
                List<Integer> pointerList = map.get(optimalisationWeight);
                pointerList.add(i);
            } else {
                List<Integer> pointerList = new ArrayList<>();
                pointerList.add(i);
                map.put(optimalisationWeight, pointerList);
            }
        }

        SortedSet<Long> keys = new TreeSet<>(map.keySet());
        int counter = 1;
        Map<String, Object> oldmap = new HashMap<>(getWhereMap());
        getWhereMap().clear();
        for (Long key : keys) {
            for (Integer value : map.get(key)) {
                Object k = oldmap.get(KEY + value);
                if (k != null) getWhereMap().put(KEY + counter, k);

                Object w = oldmap.get(WRAPPER + value);
                if (w != null) {
                    Where<?> where = ((Where<?>) w);
                    where.optimize();
                    getWhereMap().put(WRAPPER + counter, where);
                }

                Object v = oldmap.get(VALUE + value);
                if (v != null) getWhereMap().put(VALUE + counter, v);

                Object r = oldmap.get(RELATION + value);
                if (r != null) getWhereMap().put(RELATION + counter, r);

                Object m = oldmap.get(MAY_BE_INDEXED_IN_BUFFER + value);
                if (m != null) getWhereMap().put(MAY_BE_INDEXED_IN_BUFFER + counter, m);

                counter++;
            }
        }
        optimisationWage = map.isEmpty() ? 0 : Collections.min(map.keySet());
    }

    /**
     * Low-level insertion of a single condition at pointer 2 (column, value, nested clause and/or operator),
     * advancing the counter when anything was stored.
     *
     * @param wrapper  a nested clause, or {@code null}
     * @param key      the condition column, or {@code null}
     * @param val      the condition value, or {@code null}
     * @param relation the condition operator, or {@code null}
     * @param <W>      the clause type
     * @return this clause
     */
    public <W extends Where> W add(Object wrapper, Object key, Object val, Object relation) {

        boolean increaseCounter = false;
        if (key != null) {
            getWhereMap().put(KEY + 2, key);
            increaseCounter = true;
        }
        if (val != null) {
            getWhereMap().put(VALUE + 2, val);
            increaseCounter = true;
        }
        if (wrapper != null) {
            getWhereMap().put(WRAPPER + 2, wrapper);
            increaseCounter = true;
        }
        if (relation != null) {
            getWhereMap().put(RELATION + 2, relation);
            increaseCounter = true;
        }
        if (increaseCounter) {
            setCounter(getCounter() + 1);
        }
        return (W) this;
    }

}
