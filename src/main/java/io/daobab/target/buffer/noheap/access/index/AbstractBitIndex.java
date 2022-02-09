package io.daobab.target.buffer.noheap.access.index;

import io.daobab.model.TableColumn;
import io.daobab.target.buffer.noheap.access.array.BitArrayBaseNotNull;
import io.daobab.target.buffer.noheap.access.array.BitArrayIntegerNotNull;
import io.daobab.target.buffer.noheap.access.field.BitField;
import io.daobab.target.buffer.noheap.access.field.BitFieldIntegerNotNull;
import io.daobab.target.buffer.noheap.access.field.BitSize;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractBitIndex<K, B extends BitField<K>, I extends AbstractBitIndex<K, B, I>> {

    private final BitArrayIntegerNotNull bitIntArray;
    private final BitFieldIntegerNotNull bitFieldInteger = new BitFieldIntegerNotNull();

    private final int positionForPositionPointers;
    private final int positionNullValues;
    private final int positionKeys;
    private final int keysLength;

    private final ByteBuffer byteBuffer;
    private final BitArrayBaseNotNull<K, B> bitKeyArray;
    private final TableColumn tableColumn;

    protected AbstractBitIndex(final TableColumn tableColumn, final SortedMap<K, Collection<Integer>> valueIndex, final List<Integer> nullValues, final BitArrayBaseNotNull<K, B> bitKeyArray) {
        int nullValuesLength = nullValues.size();
        keysLength = valueIndex.size();
        this.bitKeyArray = bitKeyArray;
        this.tableColumn = tableColumn;
        bitIntArray = new BitArrayIntegerNotNull(tableColumn);

        int totalElements = valueIndex.values().stream().mapToInt(Collection::size).sum();

        int bufferSpace = (
                (BitSize.INT * 2) //keys and null values size - int
                        + (bitKeyArray.getTypeSize() * keysLength) //keys
                        + bitIntArray.calculateSpace(keysLength)  //key positions - int
                        + bitIntArray.calculateSpace(nullValuesLength) //values -int
                        + bitIntArray.calculateSpace(totalElements + keysLength)); //space for values -int
        this.byteBuffer = ByteBuffer.allocateDirect(bufferSpace);

        int position = 0;
        List<Integer> positionPointers = new ArrayList<>(keysLength);

        //how many keys index has
        bitFieldInteger.writeValue(byteBuffer, position, keysLength);
        position = position + BitSize.INT;

        //how many null values index has
        bitFieldInteger.writeValue(byteBuffer, position, nullValuesLength);
        position = position + BitSize.INT;

        //the place of key array
        positionKeys = position;
        bitKeyArray.writeValue(byteBuffer, position, valueIndex.keySet());
        position = position + bitKeyArray.calculateSpace(keysLength);

        //pointer where a key has values
        positionForPositionPointers = position;
        position = position + bitIntArray.calculateSpace(keysLength);

        //null values array
        positionNullValues = position;
        bitIntArray.writeValue(byteBuffer, position, nullValues);
        position = position + bitIntArray.calculateSpace(nullValuesLength);

        //writting each collection linked to a key
        for (Map.Entry<K, Collection<Integer>> entry : valueIndex.entrySet()) {
            bitIntArray.writeValue(byteBuffer, position, entry.getValue());
            positionPointers.add(position);
            position = position + bitIntArray.calculateSpace(entry.getValue().size());
        }

        //fulfilling the key pointers array
        bitIntArray.writeValue(byteBuffer, positionForPositionPointers, positionPointers);
    }

    public List<Integer> getNullValues() {
        return Arrays.asList(bitIntArray.readValue(byteBuffer, positionNullValues));
    }

    public List<K> getKeys() {
        return Arrays.asList(bitKeyArray.readValue(byteBuffer, positionKeys));
    }

    public List<Integer> getValuesForKey(K key) {
        return Arrays.asList(bitIntArray.readValue(byteBuffer, getPositionForKeyValues(key)));
    }

    public List<Integer> getValuesForKeys(List<K> keys) {
        return keys.stream()
                .map(this::getValuesForKey)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public K firstKey() {
        return getKeyAt(0);
    }

    public K lastKey() {
        return getKeyAt(keysLength - 1);
    }

    public K getKeyAt(int position) {
        return bitKeyArray.getTypeBitField().readValue(byteBuffer, positionKeys + BitSize.INT + (bitKeyArray.getTypeSize() * position));
    }

    public I subIndex(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive, boolean includeNulls) {
        int fromKeyNo = getKeyNo(fromKey);
        int toKeyNo = getKeyNo(toKey);
        if (!fromInclusive) {
            fromKeyNo++;
        }
        if (!toInclusive) {
            toKeyNo--;
        }

        fromKeyNo = Math.max(0, fromKeyNo);
        toKeyNo = Math.min(keysLength - 1, toKeyNo);

        List<K> subKeys = bitKeyArray.subListKey(byteBuffer, positionKeys, fromKeyNo, toKeyNo);
        List<Integer> subNull = includeNulls ? getNullValues() : new ArrayList<>();

        SortedMap<K, Collection<Integer>> valueIndex = new TreeMap<>(bitKeyArray.comparator());

        for (K key : subKeys) {
            valueIndex.put(key, getValuesForKey(key));
        }

        return createInstance(tableColumn, valueIndex, subNull);
    }

    protected abstract I createInstance(final TableColumn tableColumn, final SortedMap<K, Collection<Integer>> valueIndex, final List<Integer> nullValues);

    private int getPositionForKeyNo(int keyNo) {
        return bitFieldInteger.readValue(byteBuffer, positionForPositionPointers + BitSize.INT + (keyNo * BitSize.INT));
    }

    //return position of the key in a buffer
    private int getPositionForKeyValues(K key) {
        List<K> keys = getKeys();
        for (int i = 0; i < keys.size(); i++) {
            if (Objects.equals(keys.get(i), key)) {
                return getPositionForKeyNo(i);
            }
        }
        return 0;
    }

    private int getKeyNo(K key) {
        //todo: optimise
        List<K> keys = getKeys();
        for (int i = 0; i < keys.size(); i++) {
            if (Objects.equals(keys.get(i), key)) {
                return i;
            }
        }
        return 0;
    }


}
