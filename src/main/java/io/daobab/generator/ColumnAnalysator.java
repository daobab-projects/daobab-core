package io.daobab.generator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.daobab.generator.GenerateFormatter.toUpperCaseFirstCharacter;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class ColumnAnalysator {

    static void fixColumnName(GenerateColumn col) {
        if (col == null) return;
        if (col.getFinalFieldName() == null || col.getFinalFieldName().isEmpty()) return;
        if (JavaPackageResolver.forbiddenNames.contains(col.getFinalFieldName().toLowerCase())) {
            col.setFinalFieldName(col.getFinalFieldName() + "Column");
        }

    }


    static void compileNames(List<GenerateColumn> storage) {
        storage.stream()
                .map(GenerateColumn::getColumnName).forEach(columnName -> {
                    List<GenerateColumn> list = storage
                            .stream()
                            .filter(r -> r.getFinalFieldName() == null && columnName != null && columnName.equals(r.getColumnName()))
                            .collect(Collectors.toList());
                    if (list.size() == 1) {
                        list.get(0).setFinalFieldName(toUpperCaseFirstCharacter(list.get(0).getFieldName().replaceAll("\\s", "")));
                    } else {
                        list.stream()
                                .map(GenerateColumn::getFieldClass)
                                .forEach(c ->
                                        list.stream()
                                                .filter(r -> r.getFieldClass().equals(c))
                                                .forEach(rf -> rf.setFinalFieldName(toUpperCaseFirstCharacter(rf.getFieldName() + (c.equals(byte[].class) ? "TypeByteArray" : "Type" + c.getSimpleName())))));

                    }
                });


        Set<String> finalNames = storage.stream()
                .map(GenerateColumn::getFinalFieldName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        for (String finalName : finalNames) {
            List<GenerateColumn> thesameColumnNames = storage.stream()
                    .filter(r -> r.getFinalFieldName().equalsIgnoreCase(finalName))
                    .collect(Collectors.toList());
            if (thesameColumnNames.size() <= 1) continue;
            int counter = 0;
            for (GenerateColumn c : thesameColumnNames) {
                counter++;
                c.setFinalFieldName(c.getFinalFieldName() + counter);
            }

        }

        storage.forEach(ColumnAnalysator::fixColumnName);

    }


}
