package io.daobab.generator;

import io.daobab.generator.template.TemplateLanguage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
public class GenerateTarget {

    private String schemaName;
    private String catalogName;
    private String javaPackage;

    private List<GenerateTable> tableList = new ArrayList<>();


    public String toString() {
        return "schema:" + schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public List<GenerateTable> getTableList() {
        return tableList;
    }

    public void setTableList(List<GenerateTable> tableList) {
        this.tableList = tableList;
    }

    public String getTargetName() {
        boolean catalogExists = getCatalogName() != null && !"%".equalsIgnoreCase(getCatalogName()) && !getCatalogName().trim().isEmpty();
        boolean schemaExists = getSchemaName() != null && !"%".equalsIgnoreCase(getSchemaName()) && !getSchemaName().trim().isEmpty();

        if (!catalogExists && !schemaExists) {
            return "My";
        }
        if (catalogExists && !schemaExists) {
            return GenerateFormatter.toCamelCase(getCatalogName());
        } else {
            return GenerateFormatter.toCamelCase(getSchemaName());
        }
    }

    public String getTargetClassName() {
        return getTargetName() + "DataBase";
    }

    public String getTargetTablesInterfaceName() {
        return getTargetName() + "MyTables";
    }


    public String getTableImports() {
        if (getTableList() == null) return "";
        StringBuilder sb = new StringBuilder();
        getTableList().forEach(gc -> sb.append("import ")
                .append(gc.getJavaPackage())
                .append(".")
                .append(GenerateFormatter.toCamelCase(gc.getTableName()))
                .append(";")
                .append("\n"));
        return sb.toString();
    }

    public String getTableArray() {
        if (getTableList() == null || getTableList().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getTableList().size(); i++) {

            GenerateTable gc = getTableList().get(i);

            sb.append("\t\t\t")
                    .append(gc.isView() ? "view" : "tab")
                    .append(GenerateFormatter.toCamelCase(gc.getTableName()));
            if (i < getTableList().size() - 1){
                sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getTablesInitiation(TemplateLanguage language) {
        if (getTableList() == null || getTableList().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        if (language == TemplateLanguage.JAVA) {
            for (GenerateTable gc : getTableList()) {
                String tableNameCamel = GenerateFormatter.toCamelCase(gc.getTableName());
                sb.append(TableDescriptionGenerator.getTableDescription(gc))
                        .append("\t")
                        .append(tableNameCamel)
                        .append(" ")
                        .append(gc.isView() ? "view" : "tab")
                        .append(tableNameCamel)
                        .append(" = new ")
                        .append(tableNameCamel)
                        .append("();");
            }
            sb.append("\n");
        } else if (language == TemplateLanguage.KOTLIN) {
            for (GenerateTable gc : getTableList()) {
                String tableNameCamel = GenerateFormatter.toCamelCase(gc.getTableName());
                String tableNameCamelStartLower = GenerateFormatter.toCamelCaseStartLower(gc.getTableName());
                sb.append(TableDescriptionGenerator.getTableDescription(gc))
                        .append("\t")
                        .append("val ")
                        .append(gc.isView() ? "view" : "tab")
                        .append(tableNameCamel)
                        .append(": ")
                        .append(tableNameCamel)
                        .append(" = ")
                        .append(tableNameCamel)
                        .append("()");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
