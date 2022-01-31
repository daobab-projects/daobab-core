package io.daobab.statement.where.base;

import io.daobab.error.ColumnMandatory;
import io.daobab.generator.DictRemoteKey;
import io.daobab.model.Column;
import io.daobab.model.Entity;
import io.daobab.query.marschal.Marschaller;
import io.daobab.statement.condition.Operator;
import io.daobab.statement.where.WhereAnd;
import io.daobab.statement.where.WhereNot;
import io.daobab.statement.where.WhereOr;
import io.daobab.target.Target;

import java.util.*;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public abstract class WhereBase {

    protected static final String WRAPPER = "WRAPPER";
    protected static final String KEY = "key";
    protected static final String VALUE = "value";
    protected static final String RELATION = "relation";
    protected static final String ALREADY_PROCEEDED = "proc";
    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String NOT = " not ";
    protected static final String DOT = ".";
    private Map<String, Object> whereMap = new HashMap<>();
    private int counter = 1;
    private long optimisation_wage=0;
    protected static final String MAY_BE_INDEXED_IN_BUFFER = "index";

    public boolean startsFromPK(){
        return optimisation_wage>0&&optimisation_wage<200;
    }
    public static Where<?> fromRemote(Target target, Map<String, Object> map) {
        if (map == null || map.isEmpty()) return null; //TODO: Exception

        Where<?> rv;
        String rel = (String) map.get(DictRemoteKey.REL_BETWEEN_EXPRESSIONS);
        if (AND.equals(rel)) {
            rv = new WhereAnd();
        } else if (OR.equals(rel)) {
            rv = new WhereOr();
        } else if (NOT.equals(rel)) {
            rv = new WhereNot();
        } else {
            return null;//TODO: Exception
        }

        boolean endofconditions = false;

        while (!endofconditions ) {
            Object key = map.get(KEY + rv.getCounter());
            Object wrapper = map.get(WRAPPER + rv.getCounter());
            String operator = (String) map.get(RELATION + rv.getCounter());
            Object val = map.get(VALUE + rv.getCounter());

            if (key == null && wrapper == null) {
                endofconditions = true;
                break;
            }
            if (key != null && key instanceof Map) {
                Column<?, ?, ?> keycolumn = Marschaller.fromRemote(target, (Map<String, Object>) key);
                if (keycolumn == null) {
                    throw new ColumnMandatory();
                } else {
                    rv.put(KEY + rv.getCounter(), keycolumn);
                }
            }

            if (wrapper != null) {
                rv.put(WRAPPER + rv.getCounter(), fromRemote(target, (Map<String, Object>) wrapper));
                rv.put(VALUE + rv.getCounter(), fromRemote(target, (Map<String, Object>) wrapper));
            } else if (val != null ) {
                rv.put(VALUE + rv.getCounter(), val);
            }
            if (operator != null) {
                rv.put(RELATION + rv.getCounter(), Operator.valueOf(operator));
            }

            rv.setCounter(rv.getCounter() + 1);
        }
        return rv;
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

    public static <W extends WhereBase> W clone(W from, W to) {
        if (from == null || to == null) {
            return null;
        }
        to.setCounter(from.getCounter());
        to.setWhereMap(new HashMap<>(from.getWhereMap()));
        return to;
    }

    public Map<String, Object> getWhereMap() {
        return whereMap;
    }

    protected void setWhereMap(Map<String, Object> whereMap) {
        this.whereMap = whereMap;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> rv = new HashMap<>();
        rv.put(DictRemoteKey.REL_BETWEEN_EXPRESSIONS, getRelationBetweenExpressions());
        for (String key : whereMap.keySet()) {
            Object val = whereMap.get(key);
            if (val instanceof Entity) {
                rv.put(key, Marschaller.marschallEntity((Entity) val));
            } else if (val instanceof Column) {
                rv.put(key, Marschaller.marschallColumnToString((Column) val));
            } else if (val instanceof Where) {
                rv.put(key, ((Where<?>) val).toMap());
            } else {
                rv.put(key, val);
            }
        }
        return rv;
    }


    public void optimize() {
        if (optimisation_wage>0){
            return;
        }
        Map<Long, List<Integer>> map = new HashMap<>();
        for (int i = 1; i < getCounter(); i++) {
            Column key = getKeyForPointer(i);
            //Object value = getValueForPointer(i);
            Operator rel = getRelationForPointer(i);
            long optimalisationWeight=OptymalisationWeight.getColumnWeight(key) * OptymalisationWeight.getOperatorWeight(rel);

            if (map.containsKey(optimalisationWeight)){
                List<Integer> pointerList=map.get(optimalisationWeight);
                pointerList.add(i);
            }else{
                List<Integer> pointerList=new LinkedList<>();
                pointerList.add(i);
                map.put(optimalisationWeight,pointerList);
            }
        }

        SortedSet<Long> keys = new TreeSet<>(map.keySet());
        int counter = 1;
        Map<String, Object> oldmap = new HashMap<>(getWhereMap());
        getWhereMap().clear();
        for (Long key : keys) {
            for (Integer value:map.get(key)){
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
        optimisation_wage=Collections.min(map.keySet());
    }

    public static <W extends Where>  Where<W> get(Where<W> where,int key){

     where.getWhereMap().remove(WRAPPER+key);
     where.getWhereMap().remove(KEY+key);
     where.getWhereMap().remove(VALUE+key);
     where.getWhereMap().remove(RELATION+key);
     return where;
    }
    public <W extends Where> W add(Object wrapper,Object key,Object val,Object relation){

        boolean increaseCounter=false;
        if (key!=null){
            getWhereMap().put(KEY+2,key);
            increaseCounter=true;
        }
        if (val!=null){
            getWhereMap().put(VALUE+2,val);
            increaseCounter=true;
        }
        if (wrapper!=null){
            getWhereMap().put(WRAPPER+2,wrapper);
            increaseCounter=true;
        }
        if (relation!=null){
            getWhereMap().put(RELATION+2,relation);
            increaseCounter=true;
        }
        if (increaseCounter){
            setCounter(getCounter()+1);
        }
        return (W)this;
    }

}
