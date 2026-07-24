package io.daobab.generator;

import java.util.Comparator;

/**
 * Orders {@link GenerateColumn}s alphabetically by their final field name (nulls last), so the generated columns
 * appear in a stable order.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ComparatorByFinalFieldName implements Comparator<GenerateColumn> {
    /**
     * {@inheritDoc} Compares by final field name, treating {@code null} names as greatest.
     */
    @Override
    public int compare(GenerateColumn o1, GenerateColumn o2) {
        if ((o1 == null || o1.getFinalFieldName() == null) && (o2 == null || o2.getFinalFieldName() == null)) return 0;
        else if (o1 == null || o1.getFinalFieldName() == null) return 1;
        else if (o2 == null || o2.getFinalFieldName() == null) return -1;

        return o1.getFinalFieldName().compareTo(o2.getFinalFieldName());
    }
}
