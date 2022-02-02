package io.daobab.result.index.comparator;

import java.util.Comparator;

public class NumberComparator<T extends Number & Comparable<T>> implements Comparator<T> {

    public int compare( T a, T b ) throws ClassCastException {
        return a.compareTo( b );
    }
}
