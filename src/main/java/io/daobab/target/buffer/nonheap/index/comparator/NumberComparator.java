package io.daobab.target.buffer.nonheap.index.comparator;

import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class NumberComparator<T extends Number & Comparable<T>> implements Comparator<T> {

    public int compare(T a, T b) throws ClassCastException {
        return a.compareTo(b);
    }
}
