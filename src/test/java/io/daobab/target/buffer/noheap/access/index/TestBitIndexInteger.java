package io.daobab.target.buffer.noheap.access.index;

import io.daobab.target.buffer.noheap.access.field.EmptyTableColumn;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestBitIndexInteger {

    @Test
    void test() {
        SortedMap<Integer, Collection<Integer>> map = new TreeMap<>();
        List<Integer> nullValues = new ArrayList<>();


        nullValues.add(1);
        nullValues.add(3);

        map.put(5, Arrays.asList(2, 4));
        map.put(9, Arrays.asList(9, 3, 7, 8));
        BitIndexInteger bitIndexInteger = new BitIndexInteger(new EmptyTableColumn(), map, nullValues);

        Integer[] readNullValues = bitIndexInteger.getNullValues();
//        readNullValues.forEach(System.out::println);
        Set<Integer> readKeys = bitIndexInteger.getKeys();
        readKeys.forEach(System.out::println);

        Integer[] keysValues = bitIndexInteger.get(9);
//        keysValues.forEach(System.out::println);


    }
}
