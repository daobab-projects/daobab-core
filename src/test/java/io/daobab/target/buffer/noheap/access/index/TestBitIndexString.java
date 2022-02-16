package io.daobab.target.buffer.noheap.access.index;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TestBitIndexString {

    @Test
    void test() {
        SortedMap<String, Collection<Integer>> map = new TreeMap<>();
        List<Integer> nullValues = new ArrayList<>();


        nullValues.add(1);
        nullValues.add(3);

        map.put("jas", Arrays.asList(2, 4));
        map.put("malgosia", Arrays.asList(9, 3, 7, 8));
        BitIndexString bitIndexLong = new BitIndexString(20, map, nullValues);//.subIndex("jas",false,"malgosia",true,true);

        System.out.println("**** null values ****");
        List<Integer> readNullValues = bitIndexLong.getNullValues();
        readNullValues.forEach(System.out::println);
        System.out.println("**** keys ****");
        Set<String> readKeys = bitIndexLong.getKeys();
        readKeys.forEach(System.out::println);
        System.out.println("**** key 'malgosia' values ****");

        List<Integer> keysValues = bitIndexLong.get("malgosia");
        keysValues.forEach(System.out::println);

        System.out.println("**** first key ****");
        System.out.println(bitIndexLong.firstKey());

        System.out.println("**** last key ****");
        System.out.println(bitIndexLong.lastKey());

    }
}
