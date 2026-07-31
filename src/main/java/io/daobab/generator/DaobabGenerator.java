package io.daobab.generator;


import io.daobab.generator.template.TemplateLanguage;
import io.daobab.parser.ParserString;
import io.daobab.property.DaobabProperty;
import io.daobab.property.PropertyReader;
import io.daobab.target.database.DaobabDataBaseMetaData;
import io.daobab.target.database.connection.ConnectionGateway;
import io.daobab.target.database.connection.JdbcType;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.daobab.generator.GenerateFormatter.decapitalize;
import static java.lang.String.join;

/**
 * The runtime code generator: it reverse-engineers a database through the JDBC {@code DatabaseMetaData} and
 * writes the Daobab sources - entities, shared column interfaces, DTOs, composite keys and the target/tables
 * classes - via the {@link Writer} and the language templates. What and how to generate (language, catalog/schema
 * filters, type overrides, output path/package) is set with the fluent {@code enableXxx}/{@code generateOnlyXxx}
 * methods and the {@code GENERATOR_*} properties. In "definitions only" mode it emits the {@code @DaobabTable}
 * definition interfaces instead, leaving the entity/column/DTO generation to the annotation processor. The entry
 * points are the {@code reverseEngineering} overloads.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings({"java:S106", "java:S1192", "java:S1144", "unused"})
public class DaobabGenerator {

    private final boolean generateTargets = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_TARGETS, "true");
    private final boolean generateTargetInterfaces = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_TARGET_INTERFACE, "true");
    private final List<String> onlyAllowedTables = new ArrayList<>();
    int generatedColumnsCount = 0;
    int generatedTablesCount = 0;
    int generatedCompositesCount = 0;
    int generatedTargetsCount = 0;
    int generatedDtosCount = 0;
    int generatedDefinitionsCount = 0;
    private String filePath;
    private String javaPackage;
    private boolean override = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_OVERRIDE, "true");
    private boolean generateTables = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_TABLES, "true");
    private boolean generateViews = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_VIEWS, "true");
    private boolean generateColumns = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_COLUMNS, "true");
    private boolean generateDtos = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_DTOS, "true");
    private boolean generateDefinitionsOnly = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_DEFINITIONS, "false");
    private TemplateLanguage language = PropertyReader.readEnum(DaobabProperty.GENERATOR_LANGUAGE, TemplateLanguage.class, "JAVA");
    private boolean schemaIntoTableName = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_USE_SCHEMA_INTO_TABLE_NAME, "false");
    private String[] schemas;
    private String[] catalogues;
    private final JDBCTypeConverter typeConverter = new JDBCTypeConverter();

    public DaobabGenerator() {

    }

    /**
     * Sets the target language of the generated sources.
     */
    public void setLanguage(TemplateLanguage language) {
        this.language = language;
    }

    /** Whether the schema is prefixed into the generated table name. */
    public boolean isSchemaIntoTableName() {
        return schemaIntoTableName;
    }

    /** Sets whether the schema is prefixed into the generated table name. */
    public void setSchemaIntoTableName(boolean enable) {
        schemaIntoTableName = enable;
    }

    /** Reverse-engineers the database reached by the JDBC url/user/password (registering {@code driver} first). */
    public void reverseEngineering(String url, String user, String pass, Class<? extends Driver> driver) {

        Connection connection = null;
        Driver driverInstance;
        try {
            if (driver != null) {
                driverInstance = driver.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(driverInstance);
            }
            long startTime = System.currentTimeMillis();
            connection = DriverManager.getConnection(url, user, pass);

            validateAndJoinPathAndPackage();

            createTables(connection.getMetaData());
            summary(startTime);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionGateway.closeConnectionIfOpened(connection);
        }
    }

    private void validateAndJoinPathAndPackage() {
        if (getPath() == null || getPath().trim().isEmpty()) {
            throw new RuntimeException("Please set a path.");
        }

        if (getPackage() == null || getPackage().trim().isEmpty()) {
            throw new RuntimeException("Please set a package.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getPath());
        sb.append(File.separator);
        for (String pck : getPackage().split("\\.")) {
            sb.append(pck);
            sb.append(File.separator);
        }
        setPath(sb.toString());
    }

    /** Reverse-engineers the database reached by the given {@link DataSource}. */
    public void reverseEngineering(DataSource ds) {
        Connection connection = null;
        try {
            connection = ds.getConnection();
            long startTime = System.currentTimeMillis();

            validateAndJoinPathAndPackage();

            createTables(connection.getMetaData());
            summary(startTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionGateway.closeConnectionIfOpened(connection);
        }
    }

    /** Copies the given database meta data onto itself (a no-op placeholder overload). */
    public void reverseEngineering(DaobabDataBaseMetaData rv) {
        rv.setDatabaseMajorVersion(rv.getDatabaseMajorVersion());
        rv.setDatabaseProductName(rv.getDatabaseProductName());
        rv.setDriverName(rv.getDriverName());
        rv.setDriverVersion(rv.getDriverVersion());
        rv.setMaxConnections(rv.getMaxConnections());
    }

    /** Connects and prints the reachable catalogs/schemas - a diagnostic to verify the connection and access. */
    public void checkConnection(String url, String user, String pass, Class<? extends Driver> driver) {
        Connection connection = null;
        Driver driverInstance;
        try {
            if (driver != null) {
                driverInstance = driver.getDeclaredConstructor().newInstance();
                DriverManager.registerDriver(driverInstance);
            }
            connection = DriverManager.getConnection(url, user, pass);


            DaobabDataBaseMetaData rv = new DaobabDataBaseMetaData();

            rv.setDatabaseMajorVersion(String.valueOf(connection.getMetaData().getDatabaseMajorVersion()));
            rv.setDatabaseProductName(connection.getMetaData().getDatabaseProductName());
            rv.setDriverName(connection.getMetaData().getDriverName());
            rv.setDriverVersion(connection.getMetaData().getDriverVersion());

            System.out.println("Connection OK. Database: " + rv.getDatabaseProductName() + " version: " + rv.getDatabaseMajorVersion() + "." + rv.getDatabaseMinorVersion() + " driver: " + rv.getDriverName());

            System.out.println("User '" + user + "' is allowed to read database content as follows: ");

            ResultSet rsCat = connection.getMetaData().getCatalogs();

            boolean wasCatalog = false;
            while (rsCat.next()) {
                wasCatalog = true;
                String cat = rsCat.getString("TABLE_CAT");
                ResultSet rsSch = connection.getMetaData().getSchemas(cat, null);

                boolean wasSchema = false;
                while (rsSch.next()) {
                    String sch = rsSch.getString("TABLE_SCHEM");
                    wasSchema = true;
                    System.out.printf("Catalog: %s, Schema: %s%n", cat, sch);
                }

                if (!wasSchema) {
                    System.out.printf("Catalog: %s (no schema)%n", cat);
                }
            }

            if (!wasCatalog) {
                ResultSet rsSch = connection.getMetaData().getSchemas();

                boolean wasSchema = false;
                while (rsSch.next()) {
                    String sch = rsSch.getString("TABLE_SCHEM");
                    wasSchema = true;
                    System.out.println("(no catalog), Schema:" + sch);
                }

                if (!wasSchema) {
                    System.out.println("Unfortunately, seems like there is no content available for user '" + user + "'. No catalogs and no schemas were allowed to read.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionGateway.closeConnectionIfOpened(connection);
        }
    }

    /** Generates every allowed catalog and schema of the metadata. */
    private void createTables(DatabaseMetaData metaData) {
        List<String> catalogs = getCatalogues(metaData);
        for (String cat : catalogs) {
            List<String> schemaNames = getSchemas(metaData, cat);
            for (String schema : schemaNames) {
                createTables(metaData, cat, schema);
            }
            if (schemaNames.isEmpty()) {
                createTables(metaData, cat, "%");
            }
        }
    }

    /** Reads the tables/columns of one catalog+schema, resolves the column names and writes all the artifacts. */
    private List<GenerateTable> createTables(DatabaseMetaData meta, String catalog, String schema) {
        Writer writer = new Writer(language);
        writer.setGenerateDtos(generateDtos);

        List<GenerateColumn> allColumns = new ArrayList<>();
        List<GenerateTable> allTables = getTablesFromDB(meta, catalog, schema, allColumns);

        if (allTables.isEmpty()) {
            System.out.println("Warning: " + "catalog:" + catalog + ", schema:" + schema + " - There is no possibility to read any information from this place.");
            return allTables;
        }

        //Najpierw przygotowujemy kolumny
        allTables.forEach(table -> table.getColumnList().forEach(column -> prepareColumn(table.getTableName(), catalog, schema, column)));

        ColumnAnalysator.compileNames(allColumns);

        //definitions-only mode: annotated interfaces for the annotation processor, nothing else
        if (generateDefinitionsOnly) {
            allTables.forEach(tbl -> writer.generateJavaDefinition(catalog, schema, tbl, getPackage(), getPath(), isOverride(), isSchemaIntoTableName()));
            generatedDefinitionsCount = generatedDefinitionsCount + writer.generatedDefinitionsCount;
            return allTables;
        }

        //pozniej z poprawionymi nazwami generujemy tabele
        allTables.forEach(tbl -> writer.generateJavaTable(catalog, schema, tbl, allTables, getPackage(), getPath(), isOverride(), isSchemaIntoTableName()));

        for (GenerateTable table : allTables) {
            if (table.getPrimaryKeys() == null || table.getPrimaryKeys().size() <= 1) {
                continue;
            }

            allTables.stream()
                    .filter(t -> !t.getTableName().equals(table.getTableName()))
                    .filter(t -> t.containsPrimaryKeyAllCollumns(table.getPrimaryKeys()))
                    .forEach(t -> t.getInheritedSubCompositeKeys().add(table));

        }

        if (generateTables) {
            allTables.stream()
                    .filter(table -> table.getCompositeKeyName() != null)
                    .forEach(t -> writer.generateJavaCompositeKey(t, getPath(), isOverride()));
        }

        //oraz kolumny
        if (generateColumns) {
            allColumns.forEach(column -> writer.generateJavaColumn(catalog, schema, column, getPath(), isOverride()));
        }

        if (language.equals(TemplateLanguage.JAVA) || language.equals(TemplateLanguage.KOTLIN)) {
            writer.generateJavaTarget(catalog, schema, allTables, getPackage(), getPath(), isOverride());
        } else if (language.equals(TemplateLanguage.TYPE_SCRIPT)) {
            writer.createTypeScriptTables(catalog, schema, allTables, getPath(), isOverride());
        }

        generatedColumnsCount = generatedColumnsCount + writer.generatedColumnsCount;
        generatedTablesCount = generatedTablesCount + writer.generatedTablesCount;
        generatedCompositesCount = generatedCompositesCount + writer.generatedCompositesCount;
        generatedTargetsCount = generatedTargetsCount + writer.generatedTargetsCount;
        generatedDtosCount = generatedDtosCount + writer.generatedDtosCount;
        return allTables;
    }

    /** Fills a column's field name, field class, interface name and package (under {@code <base>.column}). */
    private void prepareColumn(String tableName, String catalog, String schema, GenerateColumn column) {
        if (column == null) return;
        String interfaceName = GenerateFormatter.toCamelCase(column.getColumnName());

        column.setFieldName(decapitalize(interfaceName));

        if (column.getFieldClass() == null) {
            column.setFieldClass(typeConverter.convert(tableName, column));
        }
        column.setInterfaceName(interfaceName);

        StringBuilder javaPackageName = JavaPackageResolver.resolve(getPackage(), catalog, schema);
        javaPackageName.append(".column");
        column.setPackage(javaPackageName.toString());

    }

    /** The output directory (mandatory). */
    @SuppressWarnings("java:S112")
    public String getPath() {
        if (filePath == null || filePath.trim().isEmpty())
            throw new RuntimeException("Path must be provided. Daobab generator needs to know where to write the generated files.");

        return filePath;
    }

    /** Sets the output directory. */
    public void setPath(String fileDirectoryPath) {
        this.filePath = fileDirectoryPath;
    }

    /** The root Java package of the generated sources (mandatory). */
    @SuppressWarnings("java:S112")
    public String getPackage() {
        if (javaPackage == null || javaPackage.trim().isEmpty()) {
            throw new RuntimeException("Java package must be provided.");
        }

        return javaPackage;
    }

    /** Sets the root Java package. */
    public void setPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    /** Whether existing files are overwritten. */
    public boolean isOverride() {
        return override;
    }

    /** Sets whether existing files are overwritten. */
    public void setOverride(boolean override) {
        this.override = override;
    }

    /** Reuses an existing column of the same name and type (recording the table usage), or creates a new one. */
    private GenerateColumn getUniqueColumn(List<GenerateColumn> allColumns, String tableName, String columnName, int datatype, String size, String digits) {
        for (GenerateColumn g : allColumns) {
            if (columnName.equalsIgnoreCase(g.getColumnName()) && typeConverter.convert(tableName, columnName, datatype, ParserString.toInteger(size), ParserString.toInteger(digits)).equals(g.getFieldClass())) {
                g.addTableUsage(tableName, JDBCTypeConverter.getDataBaseTypeName(datatype));
                return g;
            }
        }
        GenerateColumn rv = new GenerateColumn();
        rv.setColumnName(columnName);
        rv.setDataType(datatype);
        rv.setFieldClass(typeConverter.convert(tableName, rv));
        rv.addTableUsage(tableName, JDBCTypeConverter.getDataBaseTypeName(datatype));
        allColumns.add(rv);
        return rv;
    }

    /** The catalogs the metadata exposes, filtered by the {@code catalogues} allow-list. */
    private List<String> getCatalogues(DatabaseMetaData meta) {
        List<String> rv = new ArrayList<>();
        try {
            ResultSet rs = meta.getCatalogs();
            while (rs.next()) {
                String catalog = rs.getString("TABLE_CAT");
                if (arrayContains(catalogues, catalog)) {
                    rv.add(catalog);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rv;
    }

    /** Whether {@code str} is in {@code array}; a {@code null}/empty array passes everything. */
    private boolean arrayContains(String[] array, String str) {
        if (array == null || array.length == 0) return true; //all pass
        for (String arrstr : array) {
            if (arrstr.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    private String toYesNo(boolean flag) {
        return flag ? "yes" : "no";
    }


    /** The schemas of the catalog, filtered by the {@code schemas} allow-list. */
    private List<String> getSchemas(DatabaseMetaData meta, String catalog) {
        List<String> rv = new ArrayList<>();
        try {
            ResultSet rs = meta.getSchemas(catalog, "%");
            while (rs.next()) {
                String schema = rs.getString("TABLE_SCHEM");
                if (arrayContains(schemas, schema)) {
                    rv.add(schema);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rv;
    }

    /** Reads the tables and their columns/primary keys of a catalog+schema from the metadata. */
    @SuppressWarnings("java:S3776")
    private List<GenerateTable> getTablesFromDB(DatabaseMetaData meta, String catalog, String schema, List<GenerateColumn> allColumns) {

        List<GenerateTable> tables = new ArrayList<>();

        List<String> tableTypesOrdered = new ArrayList<>();
        if (isGenerateTables()) {
            tableTypesOrdered.add("TABLE");
        }

        if (isGenerateViews()) {
            tableTypesOrdered.add("VIEW");
        }

        try {
            ResultSet rs = meta.getTables(catalog, schema, "%", tableTypesOrdered.toArray(new String[]{}));
            while (rs.next()) {
                GenerateTable generateTable = new GenerateTable();
                generateTable.setTableName(rs.getString("TABLE_NAME"));
                generateTable.setSchemaName(rs.getString("TABLE_SCHEM"));
                generateTable.setCatalogName(rs.getString("TABLE_CAT"));
                generateTable.setRemarks(rs.getString("REMARKS"));
                generateTable.setType(rs.getString("TABLE_TYPE"));
                generateTable.setView(generateTable.getType().equalsIgnoreCase("view"));
                if (isTableAllowedToGenerate(generateTable.getTableName())) tables.add(generateTable);
            }


            for (GenerateTable table : tables) {

                List<String> primaryKeys = new ArrayList<>();
                ResultSet pks = meta.getPrimaryKeys(catalog, schema, table.getTableName());
                while (pks.next()) {
                    primaryKeys.add(pks.getString("COLUMN_NAME"));
                }

                String tableName = table.getTableName();
                System.out.println("Proceeding " + (table.isView() ? "view: " : "table: ") + tableName + "...");

                ResultSet columns = meta.getColumns(catalog, schema, table.getTableName(), "%");
                while (columns.next()) {
                    GenerateColumn column = getUniqueColumn(allColumns, tableName,
                            columns.getString("COLUMN_NAME"),
                            columns.getInt("DATA_TYPE"),
                            columns.getString("COLUMN_SIZE"),
                            columns.getString("DECIMAL_DIGITS")
                    );

                    GeneratedColumnInTable genColumnInTable = column.getColumnInTableOrCreate(tableName);
                    String columnSize = columns.getString("COLUMN_SIZE");
                    if (columnSize != null) genColumnInTable.setColumnSize(Integer.parseInt(columnSize))
                            .setDecimalDigits(columns.getString("DECIMAL_DIGITS"))
                            .setDataType(columns.getInt("DATA_TYPE"))
                            .setNullable(columns.getString("NULLABLE"))
                            .setRemarks(columns.getString("REMARKS"))
                            .setPosition(columns.getInt("ORDINAL_POSITION"))
                            .setIsAutoIncrement(columns.getString("IS_AUTOINCREMENT"));

                    if (primaryKeys.contains(column.getColumnName())) {
                        table.addPrimaryKey(column);
                        genColumnInTable.setPk(true);
                    }
                    table.getColumnList().add(column);
                    //Printing results

                    System.out.println("... column:" + column.getColumnName() + ", " + JdbcType.valueOf(column.getDataType()).toString() + getSizeInfo(Integer.parseInt(columnSize == null ? "0" : columnSize)) + ("Nullable:" + genColumnInTable.getNullable() + " ") + (genColumnInTable.isPk() ? ",PrimaryKey" : "") + ", class:" + column.getFieldClass().getSimpleName());
                }
            }
            return tables;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private String getSizeInfo(int size) {
        if (size == 0) return "";
        if (size == 2147483647) return "(lob)";
        return "(" + size + ")";
    }

    /** Whether the table passes the {@code generateOnlyTables} allow-list (empty list allows all). */
    private boolean isTableAllowedToGenerate(String tableName) {
        if (tableName == null) return false;
        if (onlyAllowedTables.isEmpty()) return true;
        return onlyAllowedTables.contains(tableName);
    }

    private String[] getSchemas() {
        return schemas;
    }

    /** Restricts generation to the given schemas. */
    public DaobabGenerator generateOnlyForSchemas(String... schemas) {
        this.schemas = schemas;
        return this;
    }

    private String[] getCatalogues() {
        return catalogues;
    }

    /** Restricts generation to the given catalogs. */
    public DaobabGenerator generateOnlyForCatalogues(String... catalogues) {
        this.catalogues = catalogues;
        return this;
    }

    private boolean isGenerateTables() {
        return generateTables;
    }

    /** Enables or disables generating tables. */
    public DaobabGenerator enableTablesGeneration(boolean generateTables) {
        this.generateTables = generateTables;
        return this;
    }

    private boolean isGenerateViews() {
        return generateViews;
    }

    /** Enables or disables generating views. */
    public DaobabGenerator enableViewGeneration(boolean generateViews) {
        this.generateViews = generateViews;
        return this;
    }

    /** Whether column-interface generation is enabled. */
    public boolean isEnabledColumnsGeneration() {
        return generateColumns;
    }

    /** Enables or disables generating the shared column interfaces. */
    public DaobabGenerator enableColumnsGeneration(boolean generateColumns) {
        this.generateColumns = generateColumns;
        return this;
    }

    /** Whether DTO generation is enabled. */
    public boolean isEnabledDtoGeneration() {
        return generateDtos;
    }

    /** Enables or disables generating the DTOs. */
    public DaobabGenerator enableDtoGeneration(boolean generateDtos) {
        this.generateDtos = generateDtos;
        return this;
    }

    /** Whether "definitions only" generation is enabled. */
    public boolean isEnabledDefinitionsOnlyGeneration() {
        return generateDefinitionsOnly;
    }

    /**
     * When enabled, the generator produces only the annotated definition interfaces
     * ({@code @DaobabTable}/{@code @DaobabColumn}) instead of the ready entities.
     * The entities, column interfaces and DTOs are then generated by the daobab
     * annotation processor during the compilation.
     */
    public DaobabGenerator enableDefinitionsOnlyGeneration(boolean generateDefinitionsOnly) {
        this.generateDefinitionsOnly = generateDefinitionsOnly;
        return this;
    }


    /** Prints the run summary (ordered catalogs/schemas/tables, settings and the generated counts). */
    private void summary(long startTime) {
        long stopTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("ss.SSS");
        System.out.println("---------------------------------------------------------");
        System.out.println("Daobab reverse engineering has been finished successfully.");
        System.out.println("Ordered catalogs: " + (catalogues == null ? "all available" : join(",", catalogues)));
        System.out.println("Ordered schemas: " + (schemas == null ? "all available" : join(",", schemas)));
        System.out.println("Ordered tables: " + (onlyAllowedTables.isEmpty() ? "all available" : join(",", onlyAllowedTables)));
        System.out.println("---------------------------------------------------------");
        System.out.println("root package: " + getPackage());
        System.out.println("Target language: " + language);
        System.out.println("Files saved into a location: " + getPath());
        System.out.println("Override files: " + toYesNo(isOverride()));
        System.out.println("Generating tables: " + toYesNo(isGenerateTables()));
        System.out.println("Generating views: " + toYesNo(isGenerateViews()));
        System.out.println("---------------------------------------------------------");
        System.out.println("Generated targets: " + generatedTargetsCount);
        System.out.println("Generated tables: " + generatedTablesCount);
        System.out.println("Generated DTOs: " + generatedDtosCount);
        System.out.println("Generated definition interfaces: " + generatedDefinitionsCount);
        System.out.println("Generated composite keys: " + generatedCompositesCount);
        System.out.println("Generated columns: " + generatedColumnsCount);
        System.out.println("Execution time: " + sdf.format(new Date(stopTime - startTime)) + " sec");
        System.out.println("---------------------------------------------------------");
    }

    /** Generates a single column interface from its database name, JDBC type and Java type. */
    public void generateSingleColumn(String schema, String databaseColumnName, JdbcType jdbcType, Class<?> javaType) {
        generateSingleColumn(null, schema, databaseColumnName, null, jdbcType, javaType);
    }

    /** Generates a single column interface with an explicit class name and package (catalog/schema). */
    public void generateSingleColumn(String catalog, String schema, String databaseColumnName, String javaClassName, JdbcType jdbcType, final Class<?> javaType) {
        GenerateColumn column = new GenerateColumn();
        column.setColumnName(databaseColumnName);
        column.setDataType(jdbcType.getType());

        StringBuilder javaPackageName = JavaPackageResolver.resolve(getPackage(), catalog, schema);

        javaPackageName.append(".column");

        column.setPackage(javaPackageName.toString());
        column.setFinalFieldName(javaClassName == null ? GenerateFormatter.toCamelCase(databaseColumnName) : javaClassName);
        column.setFieldClass(javaType);
        column.setFieldName(decapitalize(column.getInterfaceName()));
        Writer writer = new Writer(language);
        writer.generateJavaColumn(catalog, schema, column, getPath(), isOverride());
    }

    /** Restricts generation to the named tables. */
    public void generateOnlyTables(String... onlyAllowedTables) {
        if (onlyAllowedTables == null) return;
        this.onlyAllowedTables.addAll(Arrays.asList(onlyAllowedTables));
    }


    /** Forces the Java type for a specific {@code table.column}. */
    public DaobabGenerator setEnforcedTypeFor(final String tableName, final String columnName, final Class<?> enforcedType) {
        typeConverter.setEnforcedTypeFor(tableName, columnName, enforcedType);
        return this;
    }

    /** Overrides the Java type mapped to a JDBC type globally. */
    public DaobabGenerator setGeneralConversionFor(int jdbcType, final Class<?> enforcedType) {
        typeConverter.setGeneralConversionFor(jdbcType, enforcedType);
        return this;
    }


}
