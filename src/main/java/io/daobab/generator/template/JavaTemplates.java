package io.daobab.generator.template;

import io.daobab.creation.ColumnCache;
import io.daobab.creation.EntityCreator;
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
class JavaTemplates {

    static final String COLUMN_INTERFACE_TEMP = "package " + GenKeys.PACKAGE + ";\n" +
            "\n" +
            "import " + ColumnCache.class.getName() + ";\n" +
            "import io.daobab.model.*;" +
            "\n" +
            GenKeys.CLASS_FULL_NAME + "\n" +
            "\n" +
            "public interface " + GenKeys.INTERFACE_NAME + "<E extends Entity, F> extends RelatedTo<E>, MapHandler<E> {\n" +
            "\n" +
            "    default F get" + GenKeys.INTERFACE_NAME + "(){return readParam(\"" + GenKeys.FIELD_NAME + "\");}\n" +
            "\n" +
            "    default E set" + GenKeys.INTERFACE_NAME + "(F val){return storeParam(\"" + GenKeys.FIELD_NAME + "\",val);}\n" +
            "\n" +
            GenKeys.TABLES_AND_TYPE + "\n" +
            "    @SuppressWarnings({\"rawtypes\",\"unchecked\"})" +
            "\n" +
            "    default Column<E, F," + GenKeys.INTERFACE_NAME + "> col" + GenKeys.INTERFACE_NAME + "(){\n" +
            "        return " + ColumnCache.class.getSimpleName() + ".INSTANCE.getColumn(\"" + GenKeys.FIELD_NAME + "\", \"" + GenKeys.COLUMN_NAME + " \", (" + Table.class.getSimpleName() + "<?>) this, " + GenKeys.CLASS_SIMPLE_NAME + " );" +
            "    }\n" +
            "\n" +
            "}";

    static final String DATABASE_TABLES_INTERFACE_TEMP = "package " + GenKeys.TARGET_PACKAGE + "\n" +
            "\n" +
            "import " + ParserGeneral.class.getName() + ";\n" +
            "import " + QueryWhisperer.class.getName() + ";\n" +
            GenKeys.TAB_IMPORTS + "\n" +
            "\n" +
            "public interface " + GenKeys.TABLES_INTERFACE_NAME + " extends ParserGeneral, QueryWhisperer {\n" +
            "\n" +
            GenKeys.TABLES_INITIATED + "\n" +
            "\n" +
            "}";

    static final String DATA_BASE_TARGET_CLASS_TEMP = "package " + GenKeys.TARGET_PACKAGE + "\n" +
            "\n" +
            "\n" +
            "import " + Entity.class.getName() + ";\n" +
            "import " + DataBaseTarget.class.getName() + ";\n" +
            "import " + SqlProducer.class.getName() + ";\n" +
            "\n" +
            "import " + DataSource.class.getName() + ";\n" +
            "import " + Arrays.class.getName() + ";\n" +
            "import " + List.class.getName() + ";\n" +
            "\n" +
            "\n" +
            "public class " + GenKeys.TARGET_CLASS_NAME + " extends " + DataBaseTarget.class.getSimpleName() + " implements " + GenKeys.TARGET_TABLES_INTERFACE + ", " + SqlProducer.class.getSimpleName() + " {\n" +
            "    @Override\n" +
            "    protected " + DataSource.class.getSimpleName() + " initDataSource() {\n" +
            "\n" +
            "        return /* return " + DataSource.class.getSimpleName() + " here */;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "\n" +
            "    @Override\n" +
            "    public List<Entity> initTables() {\n" +
            "        return Arrays.asList(\n" +
            GenKeys.TAB_ARRAY + "\n" +
            "        );\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "}";

    static final String TABLE_CLASS_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";\n" +
            "\n" +
            "import " + EntityCreator.class.getName() + ";\n" +
            "import " + Column.class.getName() + ";\n" +
            "import " + TableColumn.class.getName() + ";\n" +
            GenKeys.PK_IMPORT + "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import " + Table.class.getName() + ";\n" +
            "\n" +
            GenKeys.TYPE_IMPORTS + "\n" +
            "import java.util.Arrays;\n" +
            "import java.util.List;\n" +
            "import java.util.Objects;\n" +
            "\n" +
            "import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;\n" +
            "import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;\n" +
            "\n" +
            "\n" +
            "@SuppressWarnings(\"rawtypes\")\n" +
            "public class " + GenKeys.TABLE_CAMEL_NAME + " extends Table implements \n" +
            GenKeys.COLUMN_INTERFACES + "\n" +
            "\t" + GenKeys.PK_INTERFACE + "\n" +
            "\t{\n" +
            "\n" +
            "\t@Override\n" +
            "\tpublic String getEntityName() {\n" +
            "\t\treturn \"" + GenKeys.TABLE_NAME + "\";\n" +
            "\t}\n" +
            "\n" +
            "\t@Override\n" +
            "    public List<TableColumn> columns() {\n" +
            "        return Arrays.asList(\n" +
            GenKeys.COLUMN_METHODS +
            "        );\n" +
            "\t}\n" +

            "\n" +
            "\t@Override\n" +
            "\tpublic " + GenKeys.TABLE_CAMEL_NAME + " clone() {\n" +
            "\t\treturn EntityDuplicator.cloneEntity(this);\n" +
            "\t}\n" +
            "\n" +
            "\t" + GenKeys.PK_ID_METHOD + "\n" +
            "\n" +
            "\n" +
            "}";
    static final String COMPOSITE_KEY_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";\n" +
            "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import " + CompositeColumns.class.getName() + ";\n" +
            "import " + Composite.class.getName() + ";\n" +
            "import " + Entity.class.getName() + ";\n" +
            "import " + TableColumn.class.getName() + ";\n" +
            "\n" +
            "public interface " + GenKeys.COMPOSITE_NAME + "<E extends Entity" +
            GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES +
            ">\n" +
            "\textends " + GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES + "{\n" +
            "\n" +
            "\t" + GenKeys.COMPOSITE_KEY_METHOD + "\n" +
            "\n" +
            "\n" +
            "}";
    public static String PK_COL_METHOD_TEMP =
            "@Override\n" +
                    "\tpublic Column<" + GenKeys.TABLE_NAME + "," + GenKeys.PK_TYPE_IMPORT + "," + GenKeys.INTERFACE_NAME + "> colID() {\n" +
                    "\t\treturn col" + GenKeys.INTERFACE_NAME + "();\n" +
                    "\t}" +
                    "\n" +
                    "\n" +
                    "\t@Override\n" +
                    "\tpublic int hashCode() {\n" +
                    "\t\treturn Objects.hashCode(getId());\n" +
                    "\t}\n" +
                    "\n" +
                    "\t@Override\n" +
                    "\tpublic boolean equals(Object obj) {\n" +
                    "\t\tif (this == obj)return true;\n" +
                    "\t\tif (obj == null)return false;\n" +
                    "\t\tif (getClass() != obj.getClass())return false;\n" +
                    "\t\tPrimaryKey<?,?,?> other = (PrimaryKey<?,?,?>) obj;\n" +
                    "\t\treturn Objects.equals(getId(), other.getId());\n" +
                    "\t}\n" +
                    "\n";
    public static String COMPOSITE_PK_KEY_METHOD_TEMP = "@Override\n" +
            "\tpublic " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_KEY_METHOD + "<" + GenKeys.TABLE_NAME + ">>" + " colCompositeId() {\n" +
            "\t\treturn " + " composite" + GenKeys.COMPOSITE_KEY_METHOD + "();\n\t\t}";
    public static String COMPOSITE_METHOD_TEMP =
            "default " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_NAME + "<E>>  composite" + GenKeys.COMPOSITE_NAME + "() {\n" +
                    "\t" +
                    "return new " + CompositeColumns.class.getSimpleName() + "<>(\n"
                    + GenKeys.COMPOSITE_KEY_METHOD +

                    ");\n\t\t}";

    private JavaTemplates() {
    }
}
