package io.daobab.generator.template;

import io.daobab.creation.ColumnCache;
import io.daobab.model.*;
import io.daobab.parser.ParserGeneral;
import io.daobab.query.base.QueryWhisperer;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.connection.SqlProducer;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
class KotlinTemplates {

    public static final String DATABASE_TABLES_INTERFACE_TEMP =
            "package " + GenKeys.TARGET_PACKAGE +
                    "\n" +
                    "\nimport " + ParserGeneral.class.getName() +
                    "\nimport " + QueryWhisperer.class.getName() +
                    "\n" + GenKeys.TAB_IMPORTS +
                    "\n" +
                    "\ninterface " + GenKeys.TABLES_INTERFACE_NAME + " : ParserGeneral, QueryWhisperer {" +
                    "\n" + GenKeys.TABLES_INITIATED +
                    "\n}";
    public static final String DATA_BASE_TARGET_CLASS_TEMP = "package " + GenKeys.TARGET_PACKAGE +
            "\n" +
            "\nimport " + Entity.class.getName() +
            "\nimport " + DataBaseTarget.class.getName() +
            "\nimport " + SqlProducer.class.getName() +
            "\n" +
            "\nimport " + DataSource.class.getName() +
            "\nimport " + Arrays.class.getName() +
            "\nimport " + List.class.getName() +
            "\n" +
            "\nclass " + GenKeys.TARGET_CLASS_NAME + " : " + DataBaseTarget.class.getSimpleName() + "(), " + SqlProducer.class.getSimpleName() + " {" +
            "\n\toverride fun initDataSource(): " + DataSource.class.getSimpleName() + " {" +
            "\n" +
            "\n\t\treturn /* return " + DataSource.class.getSimpleName() + " here */" +
            "\n\t}" +
            "\n" +
            "\n" +
            "\n\toverride fun initTables(): List<Entity> =" +
            "\n\t\tlistOf(" +
            "\n" + GenKeys.TAB_ARRAY +
            "\n\t\t)" +
            "\n" +
            "\n" + GenKeys.TABLES_INITIATED +
            "\n" +
            "\n}";
    public static final String TABLE_CLASS_TEMP = "package " + GenKeys.TABLE_PACKAGE +
            "\n" +
            "\n" + GenKeys.COLUMN_IMPORTS +
            "\n" + "import io.daobab.model.*;" +
            "\n" + GenKeys.TYPE_IMPORTS +
            "\nimport java.util.*" +
            "\n" +
            "\nclass " + GenKeys.TABLE_CAMEL_NAME + " : Table<" + GenKeys.TABLE_CAMEL_NAME + ">(), " +
            "\n" + GenKeys.COLUMN_INTERFACES +
            "\n" + GenKeys.PK_INTERFACE +
            "\n\t{" +
            "\n" +
            "\n\tpublic " + GenKeys.TABLE_CAMEL_NAME + "() {" +
            "\n\t\tsuper()" +
            "\n\t}" +
            "\n" +
            "\n\t" + GenKeys.TABLE_CAMEL_NAME + "(Map<String, Object> parameters) {" +
            "\n\t\tsuper(parameters)" +
            "\n\t}" +
            "\n" +
            "\n\toverride fun columns() = " +
            "\n\t\tlistOf(" +
            "\n" + GenKeys.COLUMN_METHODS +
            "\n\t\t)" +
            "\n\t" + GenKeys.PK_ID_METHOD +
            "\n}";
    public static final String COLUMN_INTERFACE_TEMP = "package " + GenKeys.PACKAGE +
            "\n" +
            "\nimport " + ColumnCache.class.getName() +
            "\nimport io.daobab.model.*;" +
            "\n" + GenKeys.CLASS_FULL_NAME +
            "\ninterface " + GenKeys.INTERFACE_NAME + "<E : " + Entity.class.getSimpleName() + ", F> : " + RelatedTo.class.getSimpleName() + "<E>," + MapHandler.class.getSimpleName() + " {" +
            "\n" +
            "\n\tfun get" + GenKeys.INTERFACE_NAME + "(): F = readParam(\"" + GenKeys.FIELD_NAME + "\")" +
            "\n" +
            "\n\t@Suppress(\"UNCHECKED_CAST\")" +
            "\n" +
            "\n\t\tfun set" + GenKeys.INTERFACE_NAME + "(value: F): E {" +
            "\n\t\treturn storeParam(\"" + GenKeys.FIELD_NAME + "\", value)" +
            "\n\t}" +
            "\n" +
            "\n" + GenKeys.TABLES_AND_TYPE +
            "\n\tfun col" + GenKeys.INTERFACE_NAME + "() =" +
            "\n\t\treturn " + ColumnCache.class.getSimpleName() + ".INSTANCE.getColumn(\"" + GenKeys.FIELD_NAME + "\", \"" + GenKeys.COLUMN_NAME + "\", (" + Table.class.getSimpleName() + "<?>) this, " + GenKeys.CLASS_SIMPLE_NAME + "::class.java);" +
            "\n\t}" +
            "\n}";

    public static final String COMPOSITE_KEY_TEMP = "package " + GenKeys.TABLE_PACKAGE +
            "\n" +
            "\n" + GenKeys.COLUMN_IMPORTS +
            "\nimport " + CompositeColumns.class.getName() +
            "\nimport " + Composite.class.getName() +
            "\nimport " + Entity.class.getName() +
            "\nimport " + TableColumn.class.getName() +
            "\n" +
            "\ninterface " + GenKeys.COMPOSITE_NAME + "<E : Entity" + GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES + ">" +
            "\n\t: " + GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES + "{" +
            "\n" +
            "\n\t" + GenKeys.COMPOSITE_KEY_METHOD +
            "\n" +
            "\n}";
    public static final String PK_COL_METHOD_TEMP =
            "\noverride fun colID() = col" + GenKeys.INTERFACE_NAME + "() " +
                    "\n" +
                    "\n\toverride fun hashCode() = Objects.hashCode(id)" +
                    "\n" +
                    "\n\toverride fun equals(obj: Any?): Boolean {" +
                    "\n\t\tif (this === obj) return true" +
                    "\n\t\tif (obj == null) return false" +
                    "\n\t\tif (javaClass != obj.javaClass) return false" +
                    "\n\t\tval other: PrimaryKey<*, *, *> = obj as PrimaryKey<*, *, *>" +
                    "\n\t\treturn id == other.id" +
                    "\n\t}" +
                    "\n";
    public static final String COMPOSITE_PK_KEY_METHOD_TEMP =
            "\n\toverride fun colCompositeId(): " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_KEY_METHOD + "<" + GenKeys.TABLE_NAME + ">> {" +
                    "\n\t\treturn " + " composite" + GenKeys.COMPOSITE_KEY_METHOD + "()" +
                    "\n\t\t}";
    public static final String COMPOSITE_METHOD_TEMP =
            "\n\tfun composite" + GenKeys.COMPOSITE_NAME + "() = " +
                    "\n\t\t" + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_NAME + "<E>>(" +
                    "\n" + GenKeys.COMPOSITE_KEY_METHOD + ")" +
                    "\n";

    private KotlinTemplates() {
    }
}
