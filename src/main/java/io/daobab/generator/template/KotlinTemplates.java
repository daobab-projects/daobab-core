package io.daobab.generator.template;

import io.daobab.clone.EntityDuplicator;
import io.daobab.error.AttemptToReadFromNullEntityException;
import io.daobab.error.AttemptToWriteIntoNullEntityException;
import io.daobab.model.*;
import io.daobab.parser.ParserGeneral;
import io.daobab.query.base.QueryWhisperer;
import io.daobab.target.database.DataBaseTarget;
import io.daobab.target.database.connection.SqlProducer;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
class KotlinTemplates {


    public static final String DATABASE_TABLES_INTERFACE_TEMP = "package " + GenKeys.TARGET_PACKAGE + "\n" +
            "\n" +
            "import " + ParserGeneral.class.getName() + ";\n" +
            "import " + QueryWhisperer.class.getName() + ";\n" +
            GenKeys.TAB_IMPORTS + "\n" +
            "\n" +
            "interface " + GenKeys.TABLES_INTERFACE_NAME + " : ParserGeneral, QueryWhisperer {\n" +
            "\n" +
            GenKeys.TABLES_INITIATED + "\n" +
            "\n" +
            "}";

    public static final String DATA_BASE_TARGET_CLASS_TEMP = "package " + GenKeys.TARGET_PACKAGE + "\n" +
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
            "class " + GenKeys.TARGET_CLASS_NAME + " : " + DataBaseTarget.class.getSimpleName() + "(), " + GenKeys.TARGET_TABLES_INTERFACE + ", " + SqlProducer.class.getSimpleName() + " {\n" +
            "    override fun initDataSource(): " + DataSource.class.getSimpleName() + " {\n" +
            "\n" +
            "        return /* return " + DataSource.class.getSimpleName() + " here */;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    override fun initTables(): List<Entity> {\n" +
            "        return listOf(\n" +
            GenKeys.TAB_ARRAY + "\n" +
            "        );\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "}";

    public static final String TABLE_CLASS_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";\n" +
            "\n" +
            "import com.fasterxml.jackson.annotation.JsonAutoDetect;\n" +
            "import com.fasterxml.jackson.annotation.JsonInclude;\n" +
            "import " + EntityDuplicator.class.getName() + ";\n" +
            "import " + Column.class.getName() + ";\n" +
            "import " + TableColumn.class.getName() + ";\n" +
            GenKeys.PK_IMPORT + "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import " + Table.class.getName() + ";\n" +
            "\n" +
            GenKeys.PK_TYPE_IMPORT + "\n" +
            "import java.util.Arrays;\n" +
            "import java.util.List;\n" +
            "import java.util.Objects;\n" +
            "\n" +
            "import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;\n" +
            "import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;\n" +
            "\n" +
            "\n" +
            "@JsonInclude(JsonInclude.Include.NON_NULL)\n" +
            "@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)\n" +
            "class " + GenKeys.TABLE_CAMEL_NAME + " : Table(), \n" +
            GenKeys.COLUMN_INTERFACES + "\n" +
            "\t" + GenKeys.PK_INTERFACE + "\n" +
            "\t{\n" +
            "\n" +
            "\toverride fun getEntityName(): String {\n" +
            "\t\treturn \"" + GenKeys.TABLE_NAME + "\"\n" +
            "\t}\n" +
            "\n" +
            "    override fun columns(): kotlin.collections.List<TableColumn> {\n" +
            "        return listOf(\n" +
            GenKeys.COLUMN_METHODS +
            "        )\n" +
            "\t}\n" +

            "\n" +
            "\toverride fun clone(): " + GenKeys.TABLE_CAMEL_NAME + "  {\n" +
            "\t\treturn EntityDuplicator.cloneEntity(this)\n" +
            "\t}\n" +
            "\n" +
            "\t" + GenKeys.PK_ID_METHOD + "\n" +
            "\n" +
            "\n" +
            "}";


    public static final String COLUMN_INTERFACE_TEMP = "package " + GenKeys.PACKAGE + ";\n" +
            "\n" +
            "import " + AttemptToReadFromNullEntityException.class.getName() + ";\n" +
            "import " + AttemptToWriteIntoNullEntityException.class.getName() + ";\n" +
            "import " + Column.class.getName() + ";\n" +
            "import " + EntityRelationMap.class.getName() + ";\n" +
            "import " + EntityMap.class.getName() + ";\n" +
            "import " + Objects.class.getName() + ";\n" +
            "\n" +
            GenKeys.CLASS_FULL_NAME + "\n" +
            "\n" +
            "interface " + GenKeys.INTERFACE_NAME + "<E : " + EntityMap.class.getSimpleName() + "> : " + EntityRelationMap.class.getSimpleName() + "<E> {\n" +
            "\n" +
            "\n" +
            "" + GenKeys.TABLES_AND_TYPE +
            "    fun get" + GenKeys.INTERFACE_NAME + "(): " + GenKeys.CLASS_SIMPLE_NAME + "? {\n\t\treturn getColumnParam(\"" + GenKeys.FIELD_NAME + "\")\n}" +
            "\n" +
            "    fun set" + GenKeys.INTERFACE_NAME + "(value: " + GenKeys.CLASS_SIMPLE_NAME + "?): E {\n\t\tsetColumnParam(\"" + GenKeys.FIELD_NAME + "\", value)" +
            "\n\t\treturn this as E\n\t}" +
            "\n" +
            "\n" +
            "    fun col" + GenKeys.INTERFACE_NAME + "(): Column<E, " + GenKeys.CLASS_SIMPLE_NAME + ", " + GenKeys.INTERFACE_NAME + "<*>> {\n" +
            "        return object : Column<E, " + GenKeys.CLASS_SIMPLE_NAME + ", " + GenKeys.INTERFACE_NAME + "<*>> {\n" +
            "            override fun getColumnName(): String {\n" +
            "                return \"" + GenKeys.COLUMN_NAME + "\"\n" +
            "            }\n" +
            "\n" +
            "            override fun getFieldName(): String {\n" +
            "                return \"" + GenKeys.FIELD_NAME + "\"\n" +
            "            }\n" +
            "\n" +
            "            override fun getInstance(): E {\n" +
            "                return entity\n" +
            "            }\n" +
            "\n" +
            "            override fun getFieldClass(): Class<" + GenKeys.CLASS_SIMPLE_NAME + "> {\n" +
            "                return " + GenKeys.CLASS_SIMPLE_NAME + "::class.java\n" +
            "            }\n" +
            "\n" +
            "            override fun getValue(entity: " + GenKeys.INTERFACE_NAME + "<*>): " + GenKeys.CLASS_SIMPLE_NAME + "? {\n" +
            "                return entity.get" + GenKeys.INTERFACE_NAME + "()\n" +
            "            }\n" +
            "\n" +
            "            override fun setValue(entity: " + GenKeys.INTERFACE_NAME + "<*>, value: " + GenKeys.CLASS_SIMPLE_NAME + "?){\n" +
            "                entity.set" + GenKeys.INTERFACE_NAME + "(value)\n" +
            "            }\n" +
            "\n" +
            "            override fun hashCode(): Int {\n" +
            "                return toString().hashCode()\n" +
            "            }\n" +
            "\n" +
            "            override fun toString(): String {\n" +
            "                return \"$entityName.$fieldName\"\n" +
            "            }\n" +
            "\n" +
            "            override fun equals(obj: Any?): Boolean {\n" +
            "                if (this === obj)return true\n" +
            "                if (obj == null)return false\n" +
            "                if (javaClass != obj.javaClass)return false\n" +
            "                val other = obj as Column<*, *, *>\n" +
            "                return hashCode() == other.hashCode()\n" +
            "            }\n" +
            "        };\n" +
            "    }\n" +
            "\n" +
            "}";

    public static final String COMPOSITE_KEY_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";\n" +
            "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import " + CompositeColumns.class.getName() + ";\n" +
            "import " + Composite.class.getName() + ";\n" +
            "import " + EntityMap.class.getName() + ";\n" +
            "import " + TableColumn.class.getName() + ";\n" +
            "\n" +
            "interface " + GenKeys.COMPOSITE_NAME + "<E : EntityMap" +
            GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES +
            ">\n" +
            "\t: " + GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES + "{\n" +
            "\n" +
            "\t" + GenKeys.COMPOSITE_KEY_METHOD + "\n" +
            "\n" +
            "\n" +
            "}";


    public static final String PK_COL_METHOD_TEMP =
            "override fun colID(): Column<" + GenKeys.TABLE_NAME + "," + GenKeys.PK_TYPE_IMPORT + "," + GenKeys.CLASS_SIMPLE_NAME + "<*>> {\n" +
                    "\t\treturn col" + GenKeys.INTERFACE_NAME + "() \n" +
                    "\t}" +
                    "\n" +
                    "\n" +
                    "\toverride fun hashCode(): Int {\n" +
                    "        return Objects.hashCode(id)\n" +
                    "    }\n" +
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
                    "\t\treturn " + " composite" + GenKeys.COMPOSITE_KEY_METHOD + "();\n\t\t}";


    public static final String COMPOSITE_METHOD_TEMP =
            "fun composite" + GenKeys.COMPOSITE_NAME + "(): " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_NAME + "<E>> {\n" +
                    "\t" +
                    "return " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_NAME + "<E>>(\n"
                    + GenKeys.COMPOSITE_KEY_METHOD +
                    ");\n\t\t}";
}
