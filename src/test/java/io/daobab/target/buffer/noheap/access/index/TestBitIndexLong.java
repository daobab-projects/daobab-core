package io.daobab.target.buffer.noheap.access.index;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TestBitIndexLong {

    @Test
    void test() {
        SortedMap<Long, Collection<Integer>> map = new TreeMap<>();
        List<Integer> nullValues = new ArrayList<>();

        nullValues.add(1);
        nullValues.add(3);

        map.put(5L, Arrays.asList(2, 4));
        map.put(9L, Arrays.asList(9, 3, 7, 8));
        BitIndexLong bitIndexLong = new BitIndexLong(map, nullValues);

        System.out.println("**** null values ****");
        Integer[] readNullValues = bitIndexLong.getNullValues();
//        readNullValues.forEach(System.out::println);
        System.out.println("**** keys ****");
        Set<Long> readKeys = bitIndexLong.getKeys();
        readKeys.forEach(System.out::println);
        System.out.println("**** key 9 values ****");

        Integer[] keysValues = bitIndexLong.get(9L);
        Arrays.stream(keysValues).forEach(System.out::println);


    }
}
