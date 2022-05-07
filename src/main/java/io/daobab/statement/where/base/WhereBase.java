package io.daobab.statement.where.base;

import io.daobab.error.ColumnMandatory;
import io.daobab.error.DaobabException;
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
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused", "UnusedReturnValue"})
public abstract class WhereBase {

    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String NOT = " not ";
    protected static final String WRAPPER = "WRAPPER";
    protected static final String KEY = "key";
    protected static final String VALUE = "value";
    protected static final String RELATION = "relation";
    protected static final String ALREADY_PROCEEDED = "proc";
    protected static final String DOT = ".";
    protected static final String MAY_BE_INDEXED_IN_BUFFER = "index";
    private Map<String, Object> whereMap = new HashMap<>();
    private int counter = 1;
    private long optimisationWage = 0;

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

        boolean endofconditions = false;

        while (!endofconditions) {
            Object key = map.get(KEY + rv.getCounter());
            Object wrapper = map.get(WRAPPER + rv.getCounter());
            String operator = (String) map.get(RELATION + rv.getCounter());
            Object val = map.get(VALUE + rv.getCounter());

            if (key == null && wrapper == null) {
                endofconditions = true;
                break;
            }
            if (key instanceof Map) {
                Column<?, ?, ?> keycolumn = Marshaller.fromRemote(target, (Map<String, Object>) key);
                if (keycolumn == null) {
                    throw new ColumnMandatory();
                } else {
                    rv.put(KEY + rv.getCounter(), keycolumn);
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

    public static <W extends WhereBase> W clone(W from, W to) {
        if (from == null || to == null) {
            return null;
        }
        to.setCounter(from.getCounter());
        to.setWhereMap(new HashMap<>(from.getWhereMap()));
        return to;
    }

    public static <W extends Where> Where<W> get(Where<W> where, int key) {

        where.getWhereMap().remove(WRAPPER + key);
        where.getWhereMap().remove(KEY + key);
        where.getWhereMap().remove(VALUE + key);
        where.getWhereMap().remove(RELATION + key);
        return where;
    }

    public boolean startsFromPK() {
        return optimisationWage > 0 && optimisationWage < 200;
    }

    protected void put(String key, Object value) {
        getWhereMap().put(key, value);
    }

    public int getCounter() {
        return counter;
    }

    protected void setCounter(int counter) {
        this.counter = counter;
    }

    public Column<?, ?, ?> getKeyForPointer(int pointer) {
        return (Column<?, ?, ?>) getWhereMap().get(KEY + pointer);
    }

    public Where<?> getInnerWhere(int pointer) {
        return (Where<?>) getWhereMap().get(WRAPPER + pointer);
    }

    public Object getValueForPointer(int pointer) {
        return getWhereMap().get(VALUE + pointer);
    }

    public boolean mayBeIndexedForPointer(int pointer) {
        return getWhereMap().containsKey(MAY_BE_INDEXED_IN_BUFFER + pointer);
    }

    public Operator getRelationForPointer(int pointer) {
        return (Operator) getWhereMap().get(RELATION + pointer);
    }

    public abstract String getRelationBetweenExpressions();

    public Map<String, Object> getWhereMap() {
        return whereMap;
    }

    protected void setWhereMap(Map<String, Object> whereMap) {
        this.whereMap = whereMap;
    }

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
        optimisationWage = Collections.min(map.keySet());
    }

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
