package io.daobab.generator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.daobab.generator.GenerateFormatter.toUpperCaseFirstCharacter;

/**
 * Resolves the final field name of each generated column and disambiguates the clashes - the runtime-generator
 * counterpart of the annotation processor's {@code ensureColumnInterface}. A column name used once keeps its
 * plain field name; a name shared by columns of <b>different</b> types is disambiguated with a type suffix
 * ({@code NameTypeString} / {@code NameTypeInteger}, {@code byte[]} → {@code NameTypeByteArray}); any residual
 * collision then gets a numeric counter. Finally the names that clash with a reserved word are fixed.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class ColumnAnalysator {

    /**
     * Appends {@code Column} to the final field name when it collides with a Java / Daobab / OS reserved word.
     */
    static void fixColumnName(GenerateColumn col) {
        if (col == null) return;
        if (col.getFinalFieldName() == null || col.getFinalFieldName().isEmpty()) return;
        if (JavaPackageResolver.forbiddenNames.contains(col.getFinalFieldName().toLowerCase())) {
            col.setFinalFieldName(col.getFinalFieldName() + "Column");
        }
    }

    /**
     * Assigns and disambiguates the final field name of every column: plain name when unique, a type suffix on a
     * same-name/different-type clash, a numeric counter on any residual collision, and a reserved-word fix at the
     * end (see the class description).
     */
    static void compileNames(List<GenerateColumn> storage) {
        storage.stream()
                .map(GenerateColumn::getColumnName)
                .forEach(columnName -> {
                    List<GenerateColumn> columnList = storage
                            .stream()
                            .filter(r -> r.getFinalFieldName() == null && columnName != null && columnName.equals(r.getColumnName()))
                            .collect(Collectors.toList());
                    if (columnList.size() == 1) {
                        columnList.getFirst().setFinalFieldName(toUpperCaseFirstCharacter(columnList.getFirst().getFieldName().replaceAll("\\s", "")));
                    } else {
                        columnList.stream()
                                .map(GenerateColumn::getFieldClass)
                                .forEach(c ->
                                        columnList.stream()
                                                .filter(r -> r.getFieldClass().equals(c))
                                                .forEach(rf -> rf.setFinalFieldName(toUpperCaseFirstCharacter(rf.getFieldName() + (c.equals(byte[].class) ? "TypeByteArray" : "Type" + c.getSimpleName())))));

                    }
                });


        Set<String> finalNames = storage.stream()
                .map(GenerateColumn::getFinalFieldName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        for (String finalName : finalNames) {
            List<GenerateColumn> theSameColumnNames = storage.stream()
                    .filter(r -> r.getFinalFieldName().equalsIgnoreCase(finalName))
                    .collect(Collectors.toList());
            if (theSameColumnNames.size() <= 1) continue;
            int counter = 0;
            for (GenerateColumn c : theSameColumnNames) {
                counter++;
                c.setFinalFieldName(c.getFinalFieldName() + counter);
            }
        }

        storage.forEach(ColumnAnalysator::fixColumnName);

    }


}
