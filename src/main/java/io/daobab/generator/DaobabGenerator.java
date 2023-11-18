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
    private String filePath;
    private String javaPackage;
    private boolean override = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_OVERRIDE, "true");
    private boolean generateTables = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_TABLES, "true");
    private boolean generateViews = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_VIEWS, "true");
    private boolean generateColumns = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_COLUMNS, "true");
    private final TemplateLanguage generateLanguage = PropertyReader.readEnum(DaobabProperty.GENERATOR_LANGUAGE, TemplateLanguage.class, "JAVA");
    private boolean schemaIntoTableName = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_USE_SCHEMA_INTO_TABLE_NAME, "false");
    private String[] schemas;
    private String[] catalogues;
    private final JDBCTypeConverter typeConverter = new JDBCTypeConverter();
    private TemplateLanguage language = generateLanguage;

    public DaobabGenerator() {

    }

    public void setLanguage(TemplateLanguage language) {
        this.language = language;
    }

    public boolean isSchemaIntoTableName() {
        return schemaIntoTableName;
    }

    public void setSchemaIntoTableName(boolean enable) {
        schemaIntoTableName = enable;
    }

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

    public void reverseEngineering(DaobabDataBaseMetaData rv) {
        rv.setDatabaseMajorVersion(rv.getDatabaseMajorVersion());
        rv.setDatabaseProductName(rv.getDatabaseProductName());
        rv.setDriverName(rv.getDriverName());
        rv.setDriverVersion(rv.getDriverVersion());
        rv.setMaxConnections(rv.getMaxConnections());
    }

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

    private List<GenerateTable> createTables(DatabaseMetaData meta, String catalog, String schema) {
        Writer writer = new Writer(language);

        List<GenerateColumn> allColumns = new ArrayList<>();
        List<GenerateTable> allTables = getTablesFromDB(meta, catalog, schema, allColumns);

        if (allTables.isEmpty()) {
            System.out.println("Warning: " + "catalog:" + catalog + ", schema:" + schema + " - There is no possibility to read any information from this place.");
            return allTables;
        }

        //Najpierw przygotowujemy kolumny
        allTables.forEach(table -> table.getColumnList().forEach(column -> prepareColumn(table.getTableName(), catalog, schema, column)));

        ColumnAnalysator.compileNames(allColumns);

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
        return allTables;
    }

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

    @SuppressWarnings("java:S112")
    public String getPath() {
        if (filePath == null || filePath.trim().isEmpty())
            throw new RuntimeException("Path must be provided. Daobab generator need to know, where to write generated files.");

        return filePath;
    }

    public void setPath(String fileDirectoryPath) {
        this.filePath = fileDirectoryPath;
    }

    @SuppressWarnings("java:S112")
    public String getPackage() {
        if (javaPackage == null || javaPackage.trim().isEmpty()) {
            throw new RuntimeException("Java package must be provided.");
        }

        return javaPackage;
    }

    public void setPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

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

    private boolean isTableAllowedToGenerate(String tableName) {
        if (tableName == null) return false;
        if (onlyAllowedTables.isEmpty()) return true;
        return onlyAllowedTables.contains(tableName);
    }

    private String[] getSchemas() {
        return schemas;
    }

    public DaobabGenerator generateOnlyForSchemas(String... schemas) {
        this.schemas = schemas;
        return this;
    }

    private String[] getCatalogues() {
        return catalogues;
    }

    public DaobabGenerator generateOnlyForCatalogues(String... catalogues) {
        this.catalogues = catalogues;
        return this;
    }

    private boolean isGenerateTables() {
        return generateTables;
    }

    public DaobabGenerator enableTablesGeneration(boolean generateTables) {
        this.generateTables = generateTables;
        return this;
    }

    private boolean isGenerateViews() {
        return generateViews;
    }

    public DaobabGenerator enableViewGeneration(boolean generateViews) {
        this.generateViews = generateViews;
        return this;
    }

    public boolean isEnabledColumnsGeneration() {
        return generateColumns;
    }

    public DaobabGenerator enableColumnsGeneration(boolean generateColumns) {
        this.generateColumns = generateColumns;
        return this;
    }


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
        System.out.println("Target language: " + generateLanguage);
        System.out.println("Files saved into a location: " + getPath());
        System.out.println("Override files: " + toYesNo(isOverride()));
        System.out.println("Generating tables: " + toYesNo(isGenerateTables()));
        System.out.println("Generating views: " + toYesNo(isGenerateViews()));
        System.out.println("---------------------------------------------------------");
        System.out.println("Generated targets: " + generatedTargetsCount);
        System.out.println("Generated tables: " + generatedTablesCount);
        System.out.println("Generated composite keys: " + generatedCompositesCount);
        System.out.println("Generated columns: " + generatedColumnsCount);
        System.out.println("Execution time: " + sdf.format(new Date(stopTime - startTime)) + " sec");
        System.out.println("---------------------------------------------------------");
    }

    public void generateSingleColumn(String schema, String databaseColumnName, JdbcType jdbcType, Class<?> javaType) {
        generateSingleColumn(null, schema, databaseColumnName, null, jdbcType, javaType);
    }

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

    public void generateOnlyTables(String... onlyAllowedTables) {
        if (onlyAllowedTables == null) return;
        this.onlyAllowedTables.addAll(Arrays.asList(onlyAllowedTables));
    }


    public DaobabGenerator setEnforcedTypeFor(final String tableName, final String columnName, final Class<?> enforcedType) {
        typeConverter.setEnforcedTypeFor(tableName, columnName, enforcedType);
        return this;
    }

    public DaobabGenerator setGeneralConversionFor(int jdbcType, final Class<?> enforcedType) {
        typeConverter.setGeneralConversionFor(jdbcType, enforcedType);
        return this;
    }


}
