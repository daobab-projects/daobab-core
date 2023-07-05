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
class KotlinTemplates {

    public static final String DATABASE_TABLES_INTERFACE_TEMP = "package " + GenKeys.TARGET_PACKAGE + "\n" +
            "\n" +
            "import " + ParserGeneral.class.getName() + "\n" +
            "import " + QueryWhisperer.class.getName() + "\n" +
            GenKeys.TAB_IMPORTS + "\n" +
            "\n" +
            "interface " + GenKeys.TABLES_INTERFACE_NAME + " : ParserGeneral, QueryWhisperer {\n" +
            "\n" +
            GenKeys.TABLES_INITIATED + "\n" +
            "\n" +
            "}";
    public static final String DATA_BASE_TARGET_CLASS_TEMP = "package " + GenKeys.TARGET_PACKAGE + "\n" +
            "\n" +
            "import " + Entity.class.getName() + "\n" +
            "import " + DataBaseTarget.class.getName() + "\n" +
            "import " + SqlProducer.class.getName() + "\n" +
            "\n" +
            "import " + DataSource.class.getName() + "\n" +
            "import " + Arrays.class.getName() + "\n" +
            "import " + List.class.getName() + "\n" +
            "\n" +
            "\n" +
            "class " + GenKeys.TARGET_CLASS_NAME + " : " + DataBaseTarget.class.getSimpleName() + "(), " + SqlProducer.class.getSimpleName() + " {\n" +
            "    override fun initDataSource(): " + DataSource.class.getSimpleName() + " {\n" +
            "\n" +
            "        return /* return " + DataSource.class.getSimpleName() + " here */\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    override fun initTables(): List<Entity> =\n" +
            "        listOf(\n" +
            GenKeys.TAB_ARRAY + "\n" +
            "        )\n" +
            "\n" +
            GenKeys.TABLES_INITIATED + "\n" +
            "\n" +
            "}";
    public static final String TABLE_CLASS_TEMP = "package " + GenKeys.TABLE_PACKAGE + "\n" +
            "\n" +
            "import " + EntityCreator.class.getName() + "\n" +
            "import " + Column.class.getName() + "\n" +
            "import " + TableColumn.class.getName() + "\n" +
            GenKeys.PK_IMPORT + "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import " + Table.class.getName() + "\n" +
            "\n" +
            GenKeys.TYPE_IMPORTS + "\n" +
            "import java.util.*\n" +
            "\n" +
            "import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY\n" +
            "import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE\n" +
            "\n" +
            "class " + GenKeys.TABLE_CAMEL_NAME + " : Table(), \n" +
            GenKeys.COLUMN_INTERFACES + "\n" +
            "\t" + GenKeys.PK_INTERFACE + "\n" +
            "\t{\n" +
            "\n" +
            "\toverride fun getEntityName() = \"" + GenKeys.TABLE_NAME + "\"\n" +
            "\n" +
            "\toverride fun columns() = \n" +
            "\t\tlistOf(\n" +
            GenKeys.COLUMN_METHODS +
            "\t\t)\n" +

            "\n" +
            "\toverride fun clone(): " + GenKeys.TABLE_CAMEL_NAME + "  {\n" +
            "\t\treturn EntityDuplicator.cloneEntity(this)\n" +
            "\t}\n" +
            "\n" +
            "\t" + GenKeys.PK_ID_METHOD + "\n" +
            "\n" +
            "\n" +
            "}";
    public static final String COLUMN_INTERFACE_TEMP = "package " + GenKeys.PACKAGE + "\n" +
            "\n" +
            "import " + ColumnCache.class.getName() + ";\n" +
            "import io.daobab.model.*;" +
            "\n" +
            GenKeys.CLASS_FULL_NAME + "\n" +
            "\n" +
            "interface " + GenKeys.INTERFACE_NAME + "<E : " + Entity.class.getSimpleName() + ", F> : " + RelatedTo.class.getSimpleName() + "<E>," + MapHandler.class.getSimpleName() + " {\n" +
            "\n" +
            "    fun get" + GenKeys.INTERFACE_NAME + "(): F = readParam(\"" + GenKeys.FIELD_NAME + "\")" +
            "\n" +
            "    @Suppress(\"UNCHECKED_CAST\")" + "\n" +
            "    fun set" + GenKeys.INTERFACE_NAME + "(value: F): E {\n\t\treturn storeParam(\"" + GenKeys.FIELD_NAME + "\", value)}" +
            "\n" +

            GenKeys.TABLES_AND_TYPE + "\n" +
            "    fun col" + GenKeys.INTERFACE_NAME + "() =\n" +
            "        object : Column<E, F, " + GenKeys.INTERFACE_NAME + "<*, F>> {\n" +
            "            override fun getColumnName() = \"" + GenKeys.COLUMN_NAME + "\"\n" +
            "            override fun getFieldName() = \"" + GenKeys.FIELD_NAME + "\"\n" +
            "            override fun getInstance() = entity\n" +
            "            override fun getFieldClass() = " + GenKeys.CLASS_SIMPLE_NAME + "::class.java\n" +
            "            override fun getValue(entity: " + GenKeys.INTERFACE_NAME + "<*, F>) = entity.get" + GenKeys.INTERFACE_NAME + "()\n" +
            "            override fun hashCode() = toString().hashCode()\n" +
            "            override fun toString() = \"$entityName.$fieldName\"\n" +
            "            override fun setValue(entity: " + GenKeys.INTERFACE_NAME + "<*, F>, value: F){\n" +
            "                entity.set" + GenKeys.INTERFACE_NAME + "(value)\n" +
            "            }\n" +
            "            override fun equals(other: Any?): Boolean {\n" +
            "                if (this === other) return true\n" +
            "                if (other == null) return false\n" +
            "                if (javaClass != other.javaClass) return false\n" +
            "                val otherColumn = other as Column<*, *, *>\n" +
            "                return hashCode() == otherColumn.hashCode()\n" +
            "            }\n" +
            "        }\n" +
            "    }\n";
    public static final String COMPOSITE_KEY_TEMP = "package " + GenKeys.TABLE_PACKAGE + "\n" +
            "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import " + CompositeColumns.class.getName() + "\n" +
            "import " + Composite.class.getName() + "\n" +
            "import " + Entity.class.getName() + "\n" +
            "import " + TableColumn.class.getName() + "\n" +
            "\n" +
            "interface " + GenKeys.COMPOSITE_NAME + "<E : Entity" +
            GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES +
            ">\n" +
            "\t: " + GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES + "{\n" +
            "\n" +
            "\t" + GenKeys.COMPOSITE_KEY_METHOD + "\n" +
            "\n" +
            "}";
    public static final String PK_COL_METHOD_TEMP =
            "override fun colID() = col" + GenKeys.INTERFACE_NAME + "() \n" +
                    "\n" +
                    "\toverride fun hashCode() = Objects.hashCode(id)\n" +
                    "\n" +
                    "    override fun equals(obj: Any?): Boolean {\n" +
                    "        if (this === obj) return true\n" +
                    "        if (obj == null) return false\n" +
                    "        if (javaClass != obj.javaClass) return false\n" +
                    "        val other: PrimaryKey<*, *, *> = obj as PrimaryKey<*, *, *>\n" +
                    "        return id == other.id\n" +
                    "    }" +
                    "\n";
    public static final String COMPOSITE_PK_KEY_METHOD_TEMP =
            "\toverride fun colCompositeId(): " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_KEY_METHOD + "<" + GenKeys.TABLE_NAME + ">> {\n" +
                    "\t\treturn " + " composite" + GenKeys.COMPOSITE_KEY_METHOD + "()\n\t\t}";
    public static final String COMPOSITE_METHOD_TEMP =
            "fun composite" + GenKeys.COMPOSITE_NAME + "() = \n\t\t" +
                    CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_NAME + "<E>>(\n"
                    + GenKeys.COMPOSITE_KEY_METHOD +
                    ")\n";

    private KotlinTemplates() {
    }
}
