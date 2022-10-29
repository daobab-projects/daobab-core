package io.daobab.generator;


import io.daobab.parser.ParserString;
import io.daobab.property.DaobabProperty;
import io.daobab.property.PropertyReader;
import io.daobab.target.database.DaobabDataBaseMetaData;
import io.daobab.target.database.connection.ConnectionGateway;
import io.daobab.target.database.connection.JdbcType;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.daobab.generator.GenerateFormatter.decapitalize;
import static io.daobab.generator.TypeConverter.getDbTypeName;
import static java.lang.String.join;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
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
    private boolean generateTypeScript = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_TS, "false");
    private boolean generateTables = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_TABLES, "true");
    private boolean generateViews = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_VIEWS, "true");
    private boolean generateColumns = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_COLUMNS, "true");
    private boolean schemaIntoTableName = PropertyReader.readBooleanSmall(DaobabProperty.GENERATOR_USE_SCHEMA_INTO_TABLE_NAME, "false");
    private String[] schemas;
    private String[] catalogues;


    public DaobabGenerator() {
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
                driverInstance = driver.newInstance();
                DriverManager.registerDriver(driverInstance);
            }
            long startTime = System.currentTimeMillis();
            connection = DriverManager.getConnection(url, user, pass);

            createTables(connection.getMetaData());
            summary(startTime);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionGateway.closeConnectionIfOpened(connection);
        }
    }

    public void reverseEngineering(DataSource ds) {
        Connection c = null;
        try {
            c = ds.getConnection();
            long startTime = System.currentTimeMillis();

            createTables(c.getMetaData());
            summary(startTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionGateway.closeConnectionIfOpened(c);
        }
    }


    public void reverseEngineering(DaobabDataBaseMetaData rv) {
        rv.setDatabaseMajorVersion("" + rv.getDatabaseMajorVersion());
        rv.setDatabaseProductName(rv.getDatabaseProductName());
        rv.setDriverName(rv.getDriverName());
        rv.setDriverVersion(rv.getDriverVersion());
        rv.setMaxConnections(rv.getMaxConnections());
    }


    public void checkConnection(String url, String user, String pass, Class<? extends Driver> driver) {
        Connection c = null;
        Driver driverInstance;
        try {
            if (driver != null) {
                driverInstance = driver.newInstance();
                DriverManager.registerDriver(driverInstance);
            }
            c = DriverManager.getConnection(url, user, pass);


            DaobabDataBaseMetaData rv = new DaobabDataBaseMetaData();

            rv.setDatabaseMajorVersion("" + c.getMetaData().getDatabaseMajorVersion());
            rv.setDatabaseProductName(c.getMetaData().getDatabaseProductName());
            rv.setDriverName(c.getMetaData().getDriverName());
            rv.setDriverVersion(c.getMetaData().getDriverVersion());

            System.out.println("Connection OK. Database: " + rv.getDatabaseProductName() + " version: " + rv.getDatabaseMajorVersion() + "." + rv.getDatabaseMinorVersion() + " driver: " + rv.getDriverName());

            System.out.println("User '" + user + "' is allowed to read database content as follows: ");

            ResultSet rsCat = c.getMetaData().getCatalogs();

            boolean wasCatalog = false;
            while (rsCat.next()) {
                wasCatalog = true;
                String cat = rsCat.getString("TABLE_CAT");
                ResultSet rsSch = c.getMetaData().getSchemas(cat, null);

                boolean wasSchema = false;
                while (rsSch.next()) {
                    String sch = rsSch.getString("TABLE_SCHEM");
                    wasSchema = true;
                    System.out.println("Catalog: " + cat + ", Schema:" + sch);
                }

                if (!wasSchema) {
                    System.out.println("Catalog: " + cat + " (no schema)");
                }
            }

            if (!wasCatalog) {
                ResultSet rsSch = c.getMetaData().getSchemas();

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
            ConnectionGateway.closeConnectionIfOpened(c);
        }
    }

    private void createTables(DatabaseMetaData metaData) {
        List<String> catalogs = getCataloques(metaData);
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
        Writter writter = new Writter();

        List<GenerateColumn> allColumns = new ArrayList<>();
        List<GenerateTable> allTables = getTablesFromDB(meta, catalog, schema, allColumns);


        if (allTables.isEmpty()) {
            System.out.println("Warning: " + "catalog:" + catalog + ", schema:" + schema + " - There is no possibility to read any information from this place.");
            return allTables;
        }

        //Najpierw przygotowujemy kolumny
        allTables.forEach(t -> t.getColumnList().forEach(c -> prepareColumn(catalog, schema, c)));

        ColumnAnalysator.compileNames(allColumns);

        //pozniej z poprawionymi nazwami generujemy tabele
        allTables.forEach(tbl -> writter.generateTable(catalog, schema, tbl, allTables, getPackage(), getPath(), isOverride(), isSchemaIntoTableName()));

        for (GenerateTable tbl : allTables) {
            if (tbl.getPrimaryKeys() == null || tbl.getPrimaryKeys().size() <= 1) {
                continue;
            }
            for (GenerateTable tblinner : allTables) {
                if (tblinner.getTableName().equals(tbl.getTableName())) {
                    continue;
                }
                if (tblinner.containsPrimaryKeyAllCollumns(tbl.getPrimaryKeys())) {
                    tblinner.getInheritedSubCompositeKeys().add(tbl);
                }
            }
        }

        if (generateTables) {
            allTables.stream().filter(t -> t.getCompositeKeyName() != null).forEach(t -> writter.generateCompositeKey(t, getPath(), isOverride()));
        }

        //oraz kolumny
        if (generateColumns) {
            allColumns.forEach(c -> writter.generateColumn(catalog, schema, c, getPath(), isOverride()));
        }

        //generujemy target
        writter.generateTarget(catalog, schema, allTables, getPackage(), getPath(), isOverride());

        //opcjonalnie generujemy typescript
        if (isGenerateTypeScript()) {
            writter.createTypeScriptTables(catalog, schema, allTables, getPath(), isOverride());
        }

        generatedColumnsCount = generatedColumnsCount + writter.generatedColumnsCount;
        generatedTablesCount = generatedTablesCount + writter.generatedTablesCount;
        generatedCompositesCount = generatedCompositesCount + writter.generatedCompositesCount;
        generatedTargetsCount = generatedTargetsCount + writter.generatedTargetsCount;
        return allTables;
    }


    private GenerateColumn prepareColumn(String catalog, String schema, GenerateColumn column) {
        if (column == null) return null;
        String interfaceName = GenerateFormatter.toCamelCase(column.getColumnName());

        column.setFieldName(decapitalize(interfaceName));

        if (column.getFieldClass() == null) {
            column.setFieldClass(TypeConverter.convert(column.getDataType()));
        }
        column.setInterfaceName(interfaceName);

        StringBuilder javaPackageName = JavaPackageResolver.resolve(getPackage(), catalog, schema);

        javaPackageName.append(".column");

        column.setPackage(javaPackageName.toString());

        return column;
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
            if (columnName.equalsIgnoreCase(g.getColumnName()) && TypeConverter.convert(datatype, ParserString.toInteger(size), ParserString.toInteger(digits)).equals(g.getFieldClass())) {
                g.addTableUsage(tableName, getDbTypeName(datatype));
                return g;
            }
        }
        GenerateColumn rv = new GenerateColumn();
        rv.setColumnName(columnName);
        rv.setDataType(datatype);
        rv.setFieldClass(TypeConverter.convert(datatype));
        rv.addTableUsage(tableName, getDbTypeName(datatype));
        allColumns.add(rv);
        return rv;
    }

    private List<String> getCataloques(DatabaseMetaData meta) {
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


            for (GenerateTable gentable : tables) {

                List<String> primaryKeys = new ArrayList<>();
                ResultSet pks = meta.getPrimaryKeys(catalog, schema, gentable.getTableName());
                while (pks.next()) {
                    primaryKeys.add(pks.getString("COLUMN_NAME"));
                }

                String tableName = gentable.getTableName();
                System.out.println("Proceeding " + (gentable.isView() ? "view: " : "table: ") + tableName + "...");

                ResultSet columns = meta.getColumns(catalog, schema, gentable.getTableName(), "%");
                while (columns.next()) {
                    GenerateColumn col = getUniqueColumn(allColumns, tableName,
                            columns.getString("COLUMN_NAME"),
                            columns.getInt("DATA_TYPE"),
                            columns.getString("COLUMN_SIZE"),
                            columns.getString("DECIMAL_DIGITS")
                    );

                    GeneratedColumnInTable genColumnInTable = col.getColumnInTableOrCreate(tableName);
                    String columnSize = columns.getString("COLUMN_SIZE");
                    if (columnSize != null) genColumnInTable.setColumnSize(Integer.parseInt(columnSize))
                            .setDecimalDigits(columns.getString("DECIMAL_DIGITS"))
                            .setDataType(columns.getInt("DATA_TYPE"))
                            .setNullable(columns.getString("NULLABLE"))
                            .setRemarks(columns.getString("REMARKS"))
                            .setPosition(columns.getInt("ORDINAL_POSITION"))
                            .setIsAutoIncrement(columns.getString("IS_AUTOINCREMENT"));

                    if (primaryKeys.contains(col.getColumnName())) {
                        gentable.addPrimaryKey(col);
                        genColumnInTable.setPk(true);
                    }
                    gentable.getColumnList().add(col);
                    //Printing results

                    System.out.println("... column:" + col.getColumnName() + ", " + JdbcType.valueOf(col.getDataType()).toString() + getSizeInfo(Integer.parseInt(columnSize == null ? "0" : columnSize)) + (genColumnInTable.isPk() ? ",PrimaryKey" : "") + ", class:" + col.getFieldClass().getSimpleName());
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

    public boolean isGenerateTypeScript() {
        return generateTypeScript;
    }

    public DaobabGenerator allowTypeScriptGeneration(boolean generateTypeScript) {
        this.generateTypeScript = generateTypeScript;
        return this;
    }

    private String[] getSchemas() {
        return schemas;
    }

    public DaobabGenerator allowOnlySchemas(String... schemas) {
        this.schemas = schemas;
        return this;
    }

    private String[] getCatalogues() {
        return catalogues;
    }

    public DaobabGenerator allowOnlyCatalogues(String... catalogues) {
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
        System.out.println("Java root package: " + getPackage());
        System.out.println("Files saved into location: " + getPath());
        System.out.println("Override files: " + toYesNo(isOverride()));
        System.out.println("Order to generate tables: " + toYesNo(isGenerateTables()));
        System.out.println("Order to generate views: " + toYesNo(isGenerateViews()));
        System.out.println("Order to generate TypeScript classes: " + toYesNo(isGenerateTypeScript()));
        System.out.println("---------------------------------------------------------");
        System.out.println("Generated targets: " + generatedTargetsCount);
        System.out.println("Generated tables: " + generatedTablesCount);
        System.out.println("Generated composite keys: " + generatedCompositesCount);
        System.out.println("Generated columns: " + generatedColumnsCount);
        System.out.println("Execution time: " + sdf.format(new Date(stopTime - startTime)) + " sec");
        System.out.println("---------------------------------------------------------");
    }

    public void generateSingleColumn(String schema, String databaseColumnName, JdbcType jdbcType) {
        generateSingleColumn(null, schema, databaseColumnName, null, jdbcType, null);
    }

    public void generateSingleColumn(String schema, String databaseColumnName, JdbcType jdbcType, Class<?> javaType) {
        generateSingleColumn(null, schema, databaseColumnName, null, jdbcType, javaType);
    }

    public void generateSingleColumn(String catalog, String schema, String databaseColumnName, JdbcType jdbcType) {
        generateSingleColumn(catalog, schema, databaseColumnName, null, jdbcType, null);
    }

    public void generateSingleColumn(String catalog, String schema, String databaseColumnName, String javaClassName, JdbcType jdbcType, Class<?> javaType) {
        GenerateColumn column = new GenerateColumn();
        column.setColumnName(databaseColumnName);
        column.setDataType(jdbcType.getType());

        StringBuilder javaPackageName = JavaPackageResolver.resolve(getPackage(), catalog, schema);

        javaPackageName.append(".column");

        column.setPackage(javaPackageName.toString());
        column.setFinalFieldName(javaClassName == null ? GenerateFormatter.toCamelCase(databaseColumnName) : javaClassName);
        column.setFieldClass(javaType == null ? TypeConverter.convert(column.getDataType()) : javaType);
        column.setFieldName(decapitalize(column.getInterfaceName()));
        Writter writter = new Writter();
        writter.generateColumn(catalog, schema, column, getPath(), isOverride());
    }

    public void allowOnlyTables(String... onlyAllowedTables) {
        if (onlyAllowedTables == null) return;
        this.onlyAllowedTables.addAll(Arrays.asList(onlyAllowedTables));
    }


}
