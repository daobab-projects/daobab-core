package io.daobab.generator.template;

import io.daobab.creation.DaobabCache;
import io.daobab.model.*;
import io.daobab.query.base.QueryWhisperer;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.connection.SqlProducer;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
class JavaTemplates {

    public static final String PK_COL_METHOD_TEMP =
            "\n\t@Override" +
                    "\n\tpublic Column< " + GenKeys.TABLE_NAME + ", " + GenKeys.PK_TYPE_IMPORT + ", " + GenKeys.INTERFACE_NAME + "> colID() {" +
                    "\n\t\treturn col" + GenKeys.INTERFACE_NAME + "();" +
                    "\n\t}" +
                    "\n" +
                    "\n\t@Override" +
                    "\n\tpublic int hashCode() {" +
                    "\n\t\treturn Objects.hashCode(getId());" +
                    "\n\t}" +
                    "\n" +
                    "\n\t@Override" +
                    "\n\tpublic boolean equals(Object obj) {" +
                    "\n\t\tif (this == obj) return true;" +
                    "\n\t\tif (obj == null) return false;" +
                    "\n\t\tif (getClass() != obj.getClass()) return false;" +
                    "\n\t\tPrimaryKey<?, ?, ?> other = (PrimaryKey<?, ?, ?>) obj;" +
                    "\n\t\treturn Objects.equals(getId(), other.getId());" +
                    "\n\t}" +
                    "\n" +
                    "\n";

    static final String DATABASE_TABLES_INTERFACE_TEMP = "package " + GenKeys.TARGET_PACKAGE +
            "\n" +
            "\nimport " + QueryWhisperer.class.getName() + ";" +
            "\n" + GenKeys.TAB_IMPORTS + "\n" +
            "\npublic interface " + GenKeys.TABLES_INTERFACE_NAME + " extends QueryWhisperer {" +
            "\n" +
            "\n" + GenKeys.TABLES_INITIATED +
            "\n" +
            "\n}";

    static final String DATA_BASE_TARGET_CLASS_TEMP = "package " + GenKeys.TARGET_PACKAGE +
            "\n" +
            "\nimport " + Entity.class.getName() + ";" +
            "\nimport " + DataBaseTarget.class.getName() + ";" +
            "\nimport " + SqlProducer.class.getName() + ";" +
            "\n" +
            "\nimport " + DataSource.class.getName() + ";" +
            "\nimport " + Arrays.class.getName() + ";" +
            "\nimport " + List.class.getName() + ";" +
            "\n" +
            "\npublic class " + GenKeys.TARGET_CLASS_NAME + " extends " + DataBaseTarget.class.getSimpleName() + " implements " + GenKeys.TARGET_TABLES_INTERFACE + ", " + SqlProducer.class.getSimpleName() + " {" +
            "\n\n\t@Override" +
            "\n\tprotected " + DataSource.class.getSimpleName() + " initDataSource() {" +
            "\n" +
            "\n\t\treturn /* return " + DataSource.class.getSimpleName() + " here */;" +
            "\n\t}" +
            "\n\t@Override" +
            "\n\tpublic List<Entity> initTables() {" +
            "\n\t\treturn Arrays.asList(" +
            "\n" + GenKeys.TAB_ARRAY +
            "\n\t\t);" +
            "\n\t}" +
            "\n}";
    public static final String COMPOSITE_PK_KEY_METHOD_TEMP =
            "\n\t@Override" +
                    "\n\tpublic " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_KEY_METHOD + "<" + GenKeys.TABLE_NAME + ">>" + " colCompositeId() {" +
                    "\n\t\treturn " + " composite" + GenKeys.COMPOSITE_KEY_METHOD + "();" +
                    "\n\t}";
    static final String COMPOSITE_KEY_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";" +
            "\n" +
            "\n" + GenKeys.COLUMN_IMPORTS +
            "\nimport " + CompositeColumns.class.getName() + ";" +
            "\nimport " + Composite.class.getName() + ";" +
            "\nimport " + Entity.class.getName() + ";" +
            "\nimport " + TableColumn.class.getName() + ";" +
            "\n" +
            "\npublic interface " + GenKeys.COMPOSITE_NAME + "<E extends Entity" + GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES + ">" +
            "\n\textends " + GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES + "{" +
            "\n" +
            "\n\t" + GenKeys.COMPOSITE_KEY_METHOD +
            "\n}";
    public static final String COMPOSITE_METHOD_TEMP =
            "\n\tdefault " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_NAME + "<E>>  composite" + GenKeys.COMPOSITE_NAME + "() {" +
                    "\n\treturn new " + CompositeColumns.class.getSimpleName() + "<>(" +
                    "\n" + GenKeys.COMPOSITE_KEY_METHOD + ");" +
                    "\n\t}";
    static final String COLUMN_INTERFACE_TEMP = "package " + GenKeys.PACKAGE + ";" +
            "\n" +
            "\nimport " + DaobabCache.class.getName() + ";" +
            "\nimport io.daobab.model.*;" +
            "\n" + GenKeys.CLASS_FULL_NAME +
            "\n" +
            "\n@SuppressWarnings(\"unused\")" +
            "\npublic interface " + GenKeys.INTERFACE_NAME + "<E extends Entity> extends RelatedTo<E>, MapHandler<E> {" +
            "\n" +
            "\n\tdefault " + GenKeys.CLASS_SIMPLE_NAME + " get" + GenKeys.INTERFACE_NAME + "(){" +
            "\n\t\treturn readParam(\"" + GenKeys.FIELD_NAME + "\");" +
            "\n\t}" +
            "\n" +
            "\n\tdefault E set" + GenKeys.INTERFACE_NAME + "(" + GenKeys.CLASS_SIMPLE_NAME + " val){" +
            "\n\t\treturn storeParam(\"" + GenKeys.FIELD_NAME + "\",val);" +
            "\n\t}" +
            "\n" +
            "\n" + GenKeys.TABLES_AND_TYPE +
            "\n\t@SuppressWarnings({\"rawtypes\",\"unchecked\"})" +
            "\n\tdefault Column<E, " + GenKeys.CLASS_SIMPLE_NAME + "," + GenKeys.INTERFACE_NAME + "> col" + GenKeys.INTERFACE_NAME + "(){" +
            "\n\t\treturn " + DaobabCache.class.getSimpleName() + ".getColumn(\"" + GenKeys.FIELD_NAME + "\", \"" + GenKeys.COLUMN_NAME + "\", (" + Table.class.getSimpleName() + "<?>) this, " + GenKeys.CLASS_SIMPLE_NAME + ".class);" +
            "\n\t}" +
            "\n}";
    static final String TABLE_CLASS_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";" +
            "\n" +
            "\n" + GenKeys.COLUMN_IMPORTS +
            "\n" + "import " + DaobabCache.class.getName() + ";" +
            "\n" + "import io.daobab.model.*;" +
            "\n" + GenKeys.TYPE_IMPORTS +
            "\nimport java.util.*;" +
            "\n" +
            "\n@SuppressWarnings({\"rawtypes\", \"unused\"})" +
            "\n@TableInformation(name = \"" + GenKeys.TABLE_NAME + "\")" +
            "\npublic class " + GenKeys.TABLE_CAMEL_NAME + " extends Table<" + GenKeys.TABLE_CAMEL_NAME + "> implements" +
            "\n" + GenKeys.COLUMN_INTERFACES +
            "\n\t" + GenKeys.PK_INTERFACE +
            "\n\t{" +
            "\n" +
            "\n\tpublic " + GenKeys.TABLE_CAMEL_NAME + "() {" +
            "\n\t\tsuper();" +
            "\n\t}" +
            "\n" +
            "\n\tpublic " + GenKeys.TABLE_CAMEL_NAME + "(Map<String, Object> parameters) {" +
            "\n\t\tsuper(parameters);" +
            "\n\t}" +
            "\n" +
            "\n\t@Override" +
            "\n\tpublic List<TableColumn> columns() {" +
            "\n\t\treturn " + DaobabCache.class.getSimpleName() + ".getTableColumns(this," +
            "\n\t\t\t() -> Arrays.asList(" +
            "\n" + GenKeys.COLUMN_METHODS +
            "\n\t\t));" +
            "\n\t}" +
            "\n" +
            "\n\t" + GenKeys.PK_ID_METHOD +
            "\n}";

    private JavaTemplates() {
    }
}
