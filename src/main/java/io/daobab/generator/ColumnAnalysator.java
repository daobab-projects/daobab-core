package io.daobab.generator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.daobab.generator.GenerateFormatter.toUpperCaseFirstCharacter;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
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
                    List<GenerateColumn> columnList = storage
                            .stream()
                            .filter(r -> r.getFinalFieldName() == null && columnName != null && columnName.equals(r.getColumnName()))
                            .collect(Collectors.toList());
                    if (columnList.size() == 1) {
                        columnList.get(0).setFinalFieldName(toUpperCaseFirstCharacter(columnList.get(0).getFieldName().replaceAll("\\s", "")));
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
