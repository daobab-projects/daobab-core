package io.daobab.generator;

import java.util.Comparator;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public class ComparatorByFinalFieldName implements Comparator<GenerateColumn> {
    @Override
    public int compare(GenerateColumn o1, GenerateColumn o2) {
        if ((o1 == null || o1.getFinalFieldName() == null) && (o2 == null || o2.getFinalFieldName() == null)) return 0;
        else if (o1 == null || o1.getFinalFieldName() == null) return 1;
        else if (o2 == null || o2.getFinalFieldName() == null) return -1;

        return o1.getFinalFieldName().compareTo(o2.getFinalFieldName());
    }
}