package io.daobab.generator;

import io.daobab.generator.template.TemplateLanguage;

import java.util.ArrayList;
import java.util.List;

/**
 * The per-target model of the runtime generator: the schema / catalog, the tables of one database location and
 * the generated names (the {@code XxxDataBase} target class and the {@code XxxMyTables} interface). It renders
 * the table imports, the {@code tabXxx = new Xxx()} field initializers and the target's table array for the
 * templates.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class GenerateTarget {

    private String schemaName;
    private String catalogName;
    private String javaPackage;

    private List<GenerateTable> tableList = new ArrayList<>();


    public String toString() {
        return "schema:" + schemaName;
    }

    /**
     * The schema name.
     */
    public String getSchemaName() {
        return schemaName;
    }

    /** Sets the schema name. */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /** The package of the generated target. */
    public String getJavaPackage() {
        return javaPackage;
    }

    /** Sets the package of the generated target. */
    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    /** The tables of this target. */
    public List<GenerateTable> getTableList() {
        return tableList;
    }

    /** Replaces the tables of this target. */
    public void setTableList(List<GenerateTable> tableList) {
        this.tableList = tableList;
    }

    /** The base name for the generated classes: the catalog or schema camel name, or {@code "My"} when both blank. */
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

    /** The generated target class name ({@code <name>DataBase}). */
    public String getTargetClassName() {
        return getTargetName() + "DataBase";
    }

    /** The generated tables interface name ({@code <name>MyTables}). */
    public String getTargetTablesInterfaceName() {
        return getTargetName() + "MyTables";
    }


    /** The {@code import} statements for the entity classes of the target. */
    public String getTableImports(TemplateLanguage language) {
        if (getTableList() == null) return "";
        StringBuilder sb = new StringBuilder();
        getTableList().forEach(gc -> sb.append("import ")
                .append(gc.getJavaPackage())
                .append(".")
                .append(gc.getEntityCamelName(language))
                .append(";")
                .append("\n"));
        return sb.toString();
    }

    /** The comma-separated list of the {@code tab...}/{@code view...} field names of the target. */
    public String getTableArray() {
        if (getTableList() == null || getTableList().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getTableList().size(); i++) {

            GenerateTable gc = getTableList().get(i);

            sb.append("\t\t\t")
                    .append(gc.isView() ? "view" : "tab")
                    .append(GenerateFormatter.toCamelCase(gc.getTableName()));
            if (i < getTableList().size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /** The initialized entity fields ({@code Entity tabXxx = new Entity();}), each with its schema doc, per language. */
    public String getTablesInitiation(TemplateLanguage language) {
        if (getTableList() == null || getTableList().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        if (language == TemplateLanguage.JAVA) {
            for (GenerateTable gc : getTableList()) {
                String tableNameCamel = GenerateFormatter.toCamelCase(gc.getTableName());
                String entityName = gc.getEntityCamelName(language);
                sb.append(TableDescriptionGenerator.getTableDescription(gc))
                        .append("\t")
                        .append(entityName)
                        .append(" ")
                        .append(gc.isView() ? "view" : "tab")
                        .append(tableNameCamel)
                        .append(" = new ")
                        .append(entityName)
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

    /** The catalog name. */
    public String getCatalogName() {
        return catalogName;
    }

    /** Sets the catalog name. */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
