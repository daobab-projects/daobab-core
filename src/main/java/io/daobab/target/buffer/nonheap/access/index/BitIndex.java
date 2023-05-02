package io.daobab.target.buffer.nonheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.statement.condition.Operator;
import io.daobab.target.buffer.nonheap.access.array.BitArrayBaseNotNull;
import io.daobab.target.buffer.nonheap.access.array.BitArrayUnsignedIntegerNotNull;
import io.daobab.target.buffer.nonheap.access.array.BitArrayUnsignedIntegerShortNotNull;
import io.daobab.target.buffer.nonheap.access.field.BitField;
import io.daobab.target.buffer.nonheap.index.BitBufferIndexBase;
import io.daobab.target.buffer.nonheap.index.BitBufferIndexCounterBase;

import java.nio.ByteBuffer;
import java.util.*;

public abstract class BitIndex<K, B extends BitField<K>, I extends BitIndex<K, B, I>> implements BitBufferIndexBase<K>, BitBufferIndexCounterBase<K> {

    final BitArrayBaseNotNull<Integer, ?> bitIntArray;

    final boolean useMatrix;

    final int positionValues;
    final int positionNullValues;
    final int nullValuesLength;
    final ByteBuffer byteBuffer;
    final BitArrayBaseNotNull<K, B> bitKeyArray;
    final SortedMap<K, Integer> keysQueue;
    protected final TableColumn tableColumn;
    protected final B bitKeyField;
    final KeyLengthPosition<K>[] keysArray;
    private final int keysLength;

    protected BitIndex(I original, final TableColumn tableColumn, final Collection<K> keys, final boolean nullValues) {
        nullValuesLength = nullValues ? original.nullValuesLength : 0;
        keysLength = keys.size();
        this.bitKeyArray = original.bitKeyArray;
        this.bitKeyField = bitKeyArray.getTypeBitField();
        this.tableColumn = tableColumn;
        this.positionValues = original.positionValues;
        bitIntArray = original.bitIntArray;
        keysQueue = new TreeMap<>(bitKeyField.comparator());
        keysArray = new KeyLengthPosition[keys.size()];
        useMatrix = original.useMatrix;

        int i = 0;
        for (K key : keys) {
            KeyLengthPosition<K> klp = new KeyLengthPosition<>(key);
            Integer originalKeyPosition = original.keysQueue.get(key);
            klp.setLength(original.keysArray[originalKeyPosition].getLength());
            klp.setPosition(original.keysArray[originalKeyPosition].getPosition());

            keysQueue.put(key, i);
            keysArray[i] = klp;
            i++;
        }

        this.byteBuffer = original.byteBuffer;

        //null values array
        positionNullValues = original.positionNullValues;

    }

    protected BitIndex(final TableColumn tableColumn, final SortedMap<K, Collection<Integer>> valueIndex, final List<Integer> nullValues, final BitArrayBaseNotNull<K, B> bitKeyArray) {
        nullValuesLength = nullValues.size();
        keysLength = valueIndex.size();
        this.bitKeyArray = bitKeyArray;
        this.bitKeyField = bitKeyArray.getTypeBitField();
        this.tableColumn = tableColumn;

        keysQueue = new TreeMap<>(bitKeyField.comparator());
        Set<K> keys = valueIndex.keySet();
        int totalElements = valueIndex.values().stream().mapToInt(Collection::size).sum();
        keysArray = new KeyLengthPosition[keys.size()];

        if (totalElements < (Short.MAX_VALUE * 2) - 1) {
            bitIntArray = new BitArrayUnsignedIntegerShortNotNull(tableColumn);
            useMatrix = true;
        } else {
            bitIntArray = new BitArrayUnsignedIntegerNotNull(tableColumn);
            useMatrix = false;
        }


        int i = 0;
        for (K key : keys) {
            KeyLengthPosition<K> klp = new KeyLengthPosition<>(key);
            keysQueue.put(key, i);
            keysArray[i] = klp;
            i++;
        }


        int bufferSpace = (
                +bitIntArray.calculateSpace(nullValuesLength) //values -int
                        + bitIntArray.calculateSpace(totalElements + keysLength)); //space for values -int
        this.byteBuffer = ByteBuffer.allocateDirect(bufferSpace);

        int position = 0;


        //null values array
        positionNullValues = position;
        bitIntArray.writeValue(byteBuffer, position, nullValues);
        position += bitIntArray.calculateSpace(nullValuesLength);

        int counter = 0;
        positionValues = position;
        //writting each collection linked to a key
        for (Map.Entry<K, Collection<Integer>> entry : valueIndex.entrySet()) {
            bitIntArray.writeValueWithoutLength(byteBuffer, position, entry.getValue());
            keysArray[counter].setLength(entry.getValue().size());
            keysArray[counter].setPosition(position);
//            System.out.println("counter "+counter+" length "+entry.getValue().size()+" position "+ position);
            position += bitIntArray.calculateSpace(entry.getValue().size()) - bitIntArray.getTypeSize();
            counter++;
        }

    }

    public static Integer[] flatten(Integer[][] a2) {
        Integer[] result = new Integer[totalSize(a2)];
        int index = 0;
        for (Integer[] a1 : a2) {
            for (Integer s : a1) {
                result[index++] = s;
            }
        }
        return result;
    }

    public int countGetNullValues() {
        return nullValuesLength;
    }

    public Set<K> getKeys() {
        return keysQueue.keySet();
    }

    public static int totalSize(Integer[][] a2) {
        int result = 0;
        for (Integer[] a1 : a2) {
            result += a1.length;
        }
        return result;
    }

    public int countGet(K key) {
        int keyPositionInQueue = keysQueue.get(key);
        KeyLengthPosition<K> klp = keysArray[keyPositionInQueue];
        return klp.getLength();
    }

    public static Integer[] flattenList(List<Integer[]> a2) {
        Integer[] result = new Integer[totalSizeList(a2)];
        int index = 0;
        for (Integer[] a1 : a2) {
            for (Integer s : a1) {
                result[index++] = s;
            }
        }
        return result;
    }

    public int countGet(KeyLengthPosition<K> klp) {
        return klp.getLength();
    }

    public static int totalSizeList(List<Integer[]> a2) {
        int result = 0;
        for (Integer[] a1 : a2) {
            result += a1.length;
        }
        return result;
    }

    public Integer[] getNullValues() {
        if (nullValuesLength == 0) {
            return new Integer[0];
        }
        return bitIntArray.readValue(byteBuffer, positionNullValues);
    }

    public K firstKey() {
        return getKeyAt(0);
    }

    public K lastKey() {
        return getKeyAt(keysLength - 1);
    }

    public K getKeyAt(int position) {
        return keysArray[position].getKey();
    }

    public boolean isEmpty() {
        return keysLength == 0;
    }
//
//    public I subIndex(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive, boolean includeNulls) {
//        int fromKeyNo = keysQueue.get(fromKey);
//        int toKeyNo = keysQueue.get(toKey);
//        if (!fromInclusive) {
//            fromKeyNo++;
//        }
//        if (toInclusive) {
//            toKeyNo++;
//        }
//
//        fromKeyNo = Math.max(0, fromKeyNo);
//        toKeyNo = Math.min(keysLength - 1, toKeyNo);
//
//        List<K> subKeys = new ArrayList<>();
//        for (int i = fromKeyNo; i < toKeyNo; i++) {
//            subKeys.add(keysArray[i].getKey());
//        }
//
//        return createSubIndex((I) this, tableColumn, subKeys, includeNulls);
//    }

    public Integer[] get(K key) {
        int keyPositionInQueue = keysQueue.get(key);
        KeyLengthPosition<K> klp = keysArray[keyPositionInQueue];
        Integer[] readTo = new Integer[klp.getLength()];
        bitIntArray.readValueListWithLength(byteBuffer, readTo, klp.getPosition(), klp.getLength());
        return readTo;
    }

    public int countSubValues(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        int fromKeyNo = keysQueue.get(fromKey);
        int toKeyNo = keysQueue.get(toKey);
        if (!fromInclusive) {
            fromKeyNo++;
        }
        if (toInclusive) {
            toKeyNo++;
        }

        fromKeyNo = Math.max(0, fromKeyNo);
        toKeyNo = Math.min(keysLength - 1, toKeyNo);

        int count = 0;

        for (int i = fromKeyNo; i < toKeyNo; i++) {
            count += countGet(keysArray[i]);
        }

        return count;
    }

    public Integer[] get(KeyLengthPosition<K> klp) {
        Integer[] readTo = new Integer[klp.getLength()];
        bitIntArray.readValueListWithLength(byteBuffer, readTo, klp.getPosition(), klp.getLength());
        return readTo;
    }

    public int countAllValues() {
        int count = 0;

        for (int i = 0; i < keysLength; i++) {
            count += countGet(keysArray[i]);
        }

        return count;
    }

    protected abstract I createInstance(final TableColumn tableColumn, final SortedMap<K, Collection<Integer>> valueIndex, final List<Integer> nullValues);

    protected abstract I createSubIndex(I original, final TableColumn tableColumn, final Collection<K> keys, final boolean nullValues);

    public Integer[] getValuesForKeys(List<K> keys) {
        List<Integer[]> rv = new ArrayList<>();
        for (K key : keys) {
            rv.add(get(key));
        }

//        return keys.stream()
//                .map(this::get)
//                .flatMap(Arrays::stream)
//                .collect(Collectors.toList());

        return flattenList(rv);
    }

    public Integer countFilter(Operator operator, K... keys) {
        int counter = 0;

        if (Operator.IN.equals(operator)) {
            for (K indexKey : getKeys()) {
                for (K key : keys) {
                    if (bitKeyField.comparator().compare(indexKey, key) == 0) {
                        counter += countGet(indexKey);
                    }
                }
            }

        } else if (Operator.NOT_IN.equals(operator)) {
            for (K indexKey : getKeys()) {
                for (K key : keys) {
                    if (bitKeyField.comparator().compare(indexKey, key) == 0) {
                        continue;
                    }
                    counter += countGet(indexKey);
                }
            }
        }
        return counter;
    }

    public int countGetValuesForKeys(List<K> keys) {
        return keys.stream()
                .map(this::get)
                .map(a -> a.length)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @SuppressWarnings("unchecked")
    public Integer countFilter(Operator operator, Object key1) {
        K key = (K) key1;
        switch (operator) {
            case IN:
            case EQ: {
                return countGet(key);
            }

            case NOT_EQ: {
                return countFilterNegative(Operator.EQ, key);
            }
            case NOT_IN: {
                return countFilterNegative(Operator.IN, key);
            }
            case GT: {
                return (isEmpty() || bitKeyField.comparator().compare(key, lastKey()) > 0) ? 0 : countSubValues(key, false, lastKey(), true);
            }
            case GTEQ: {
                return (isEmpty() || bitKeyField.comparator().compare(key, lastKey()) > 0) ? 0 : countSubValues(key, true, lastKey(), true);
            }
            case LT: {
                return (isEmpty() || bitKeyField.comparator().compare(key, firstKey()) < 0) ? 0 : countSubValues(firstKey(), true, key, false);
            }
            case LTEQ: {
                return (isEmpty() || bitKeyField.comparator().compare(key, firstKey()) < 0) ? 0 : countSubValues(firstKey(), true, key, true);
            }
            case IS_NULL: {
                return countGetNullValues();
            }
            case NOT_NULL: {
                return countAllValues();
            }
            default:
                return 0;
        }
    }

    public Integer[] subValues(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        int fromKeyNo = keysQueue.get(fromKey);
        int toKeyNo = keysQueue.get(toKey);
        if (!fromInclusive) {
            fromKeyNo++;
        }
        if (toInclusive) {
            toKeyNo++;
        }

        fromKeyNo = Math.max(0, fromKeyNo);
        toKeyNo = Math.min(keysLength - 1, toKeyNo);

        Integer[][] rv = new Integer[toKeyNo - fromKeyNo][];
        for (int i = 0; i < toKeyNo - fromKeyNo; i++) {
            rv[i] = get(keysArray[fromKeyNo + i]);
        }

        return flatten(rv);
    }


    @SuppressWarnings("unchecked")
    public Integer countFilterNegative(Operator operator, Object key1) {
        K key = (K) key1;
        switch (operator) {
            case IN:
            case EQ: {
                List<K> keys = new ArrayList<>(getKeys());
                keys.remove(key);
                return countGetValuesForKeys(keys);
            }
            case GT: {
                return countFilter(Operator.LTEQ, key);
            }
            case GTEQ: {
                return countFilter(Operator.LT, key);
            }
            case LT: {
                return countFilter(Operator.GTEQ, key);
            }
            case LTEQ: {
                return countFilter(Operator.GT, key);
            }
            case IS_NULL: {
                return countFilter(Operator.NOT_NULL, key);
            }
            case NOT_NULL: {
                return countFilter(Operator.IS_NULL, key);
            }
            default:
                return 0;
        }
    }

    @Override
    public void addValue(K value, int pointer) {

    }

    @Override
    public boolean removeValue(K value, int pointer) {
        return false;
    }

    public Integer[] allValues() {

        Integer[][] rv = new Integer[keysLength][];
        for (int i = 0; i < keysLength; i++) {
            rv[i] = get(keysArray[i]);
        }
//        List<Integer> rv = new ArrayList<>();
//
//        for (int i = 0; i < keysLength; i++) {
//            rv.addAll(get(keysArray[i]));
//        }

        return flatten(rv);
    }

    public Integer[] filter(Operator operator, K... keys) {
        List<Integer[]> rv = new ArrayList<>();

        if (Operator.IN.equals(operator)) {
            for (K indexKey : getKeys()) {
                for (K key : keys) {
                    if (bitKeyField.comparator().compare(indexKey, key) == 0) {
                        rv.add(get(indexKey));
                    }
                }
            }

        } else if (Operator.NOT_IN.equals(operator)) {
            for (K indexKey : getKeys()) {
                for (K key : keys) {
                    if (bitKeyField.comparator().compare(indexKey, key) == 0) {
                        continue;
                    }
                    rv.add(get(indexKey));
                }
            }
        }
//        return rv;
        return flattenList(rv);
    }

    @SuppressWarnings("unchecked")
    public Integer[] filter(Operator operator, Object key1) {
        K key = (K) key1;

        switch (operator) {
            case IN:
            case EQ: {
                return get(key);
            }
            case NOT_EQ: {
                return filterNegative(Operator.EQ, key);
            }
            case NOT_IN: {
                return filterNegative(Operator.IN, key);
            }

            case GT: {
                return (isEmpty() || bitKeyField.comparator().compare(key, lastKey()) > 0) ? new Integer[0] : subValues(key, false, lastKey(), true);
            }
            case GTEQ: {
                return (isEmpty() || bitKeyField.comparator().compare(key, lastKey()) > 0) ? new Integer[0] : subValues(key, true, lastKey(), true);
            }
            case LT: {
                return (isEmpty() || bitKeyField.comparator().compare(key, firstKey()) < 0) ? new Integer[0] : subValues(firstKey(), true, key, false);
            }
            case LTEQ: {
                return (isEmpty() || bitKeyField.comparator().compare(key, firstKey()) < 0) ? new Integer[0] : subValues(firstKey(), true, key, true);
            }
            case IS_NULL: {
                return getNullValues();
            }
            case NOT_NULL: {
                return allValues();
            }
            default:
                return new Integer[0];
        }
    }

    @SuppressWarnings("unchecked")
    public Integer[] filterNegative(Operator operator, Object key1) {
        K key = (K) key1;
        switch (operator) {
            case IN:
            case EQ: {
                List<K> keys = new ArrayList<>(getKeys());
                keys.remove(key);
                return getValuesForKeys(keys);
            }
            case GT: {
                return filter(Operator.LTEQ, key);
            }
            case GTEQ: {
                return filter(Operator.LT, key);
            }
            case LT: {
                return filter(Operator.GTEQ, key);
            }
            case LTEQ: {
                return filter(Operator.GT, key);
            }
            case IS_NULL: {
                return filter(Operator.NOT_NULL, key);
            }
            case NOT_NULL: {
                return filter(Operator.IS_NULL, key);
            }
            default:
                return new Integer[0];
        }
    }

}
