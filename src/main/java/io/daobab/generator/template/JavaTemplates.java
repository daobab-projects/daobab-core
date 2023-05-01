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
class JavaTemplates {

    static final String COLUMN_INTERFACE_TEMP = "package " + GenKeys.PACKAGE + ";\n" +
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
            "public interface " + GenKeys.INTERFACE_NAME + "<E extends EntityMap> extends EntityRelationMap<E> {\n" +
            "\n" +
            "\n" +
            "    default " + GenKeys.CLASS_SIMPLE_NAME + " get" + GenKeys.INTERFACE_NAME + "(){return getColumnParam(\"" + GenKeys.FIELD_NAME + "\");}\n" +
            "    @SuppressWarnings(\"unchecked\")" +
            "\n" +
            "    default E set" + GenKeys.INTERFACE_NAME + "(" + GenKeys.CLASS_SIMPLE_NAME + " val){setColumnParam(\"" + GenKeys.FIELD_NAME + "\",val); return (E)this;}\n" +
            "\n" +
            "    @SuppressWarnings(\"rawtypes\")" +
            "\n" +
            GenKeys.TABLES_AND_TYPE + "\n" +
            "    default Column<E," + GenKeys.CLASS_SIMPLE_NAME + "," + GenKeys.INTERFACE_NAME + "> col" + GenKeys.INTERFACE_NAME + "(){\n" +
            "        return new Column<E," + GenKeys.CLASS_SIMPLE_NAME + "," + GenKeys.INTERFACE_NAME + ">() {\n" +
            "\n" +
            "            @Override\n" +
            "            public String getColumnName() {\n" +
            "                return \"" + GenKeys.COLUMN_NAME + "\";\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public String getFieldName() {\n" +
            "                return \"" + GenKeys.FIELD_NAME + "\";\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public E getInstance(){\n" +
            "                return getEntity();\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public Class<" + GenKeys.CLASS_SIMPLE_NAME + "> getFieldClass(){\n" +
            "                return  " + GenKeys.CLASS_SIMPLE_NAME + ".class;\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public " + GenKeys.CLASS_SIMPLE_NAME + " getValue(" + GenKeys.INTERFACE_NAME + " entity){\n" +
            "                if (entity==null) throw new AttemptToReadFromNullEntityException(getEntityClass(),\"" + GenKeys.FIELD_NAME + "\");\n" +
            "                return  entity.get" + GenKeys.INTERFACE_NAME + "();\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public void setValue(" + GenKeys.INTERFACE_NAME + " entity, " + GenKeys.CLASS_SIMPLE_NAME + " param){\n" +
            "                if (entity==null) throw new AttemptToWriteIntoNullEntityException(getEntityClass(),\"" + GenKeys.FIELD_NAME + "\");\n" +
            "                entity.set" + GenKeys.INTERFACE_NAME + "(param);\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public int hashCode() {\n" +
            "                return toString().hashCode();\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public String toString(){\n" +
            "                return getEntityName()+\".\"+getFieldName();\n" +
            "            }" +
            "\n" +
            "\n" +
            "            @Override\n" +
            "            public boolean equals(Object obj) {\n" +
            "                if (this == obj)return true;\n" +
            "                if (obj == null)return false;\n" +
            "                if (getClass() != obj.getClass())return false;\n" +
            "                Column other = (Column) obj;\n" +
            "                return Objects.equals(hashCode(), other.hashCode());\n" +
            "            }\n" +
            "        };\n" +
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
            "import com.fasterxml.jackson.annotation.JsonAutoDetect;\n" +
            "import com.fasterxml.jackson.annotation.JsonInclude;\n" +
            "import " + EntityDuplicator.class.getName() + ";\n" +
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
            "@JsonInclude(JsonInclude.Include.NON_NULL)\n" +
            "@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)\n" +
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


    private JavaTemplates() {
    }

    static final String COMPOSITE_KEY_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";\n" +
            "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import " + CompositeColumns.class.getName() + ";\n" +
            "import " + Composite.class.getName() + ";\n" +
            "import " + EntityMap.class.getName() + ";\n" +
            "import " + TableColumn.class.getName() + ";\n" +
            "\n" +
            "public interface " + GenKeys.COMPOSITE_NAME + "<E extends EntityMap" +
            GenKeys.COMPOSITE_KEY_COLUMN_TYPE_INTERFACES +
            ">\n" +
            "\textends " + GenKeys.COMPOSITE_KEY_COLUMN_INTERFACES + "{\n" +
            "\n" +
            "\t" + GenKeys.COMPOSITE_KEY_METHOD + "\n" +
            "\n" +
            "\n" +
            "}";

    public static String PK_COL_METHOD_TEMP=
            "@Override\n" +
                "\tpublic Column<" + GenKeys.TABLE_NAME + "," + GenKeys.PK_TYPE_IMPORT + "," + GenKeys.CLASS_SIMPLE_NAME + "> colID() {\n" +
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

    public static String COMPOSITE_PK_KEY_METHOD_TEMP ="@Override\n" +
            "\tpublic " + CompositeColumns.class.getSimpleName() + "<" + GenKeys.COMPOSITE_KEY_METHOD + "<" + GenKeys.TABLE_NAME + ">>" + " keyColumns() {\n" +
            "\t\treturn " + " composite" + GenKeys.COMPOSITE_KEY_METHOD + "();\n\t\t}";

    public static String COMPOSITE_METHOD_TEMP =
        "default "+CompositeColumns.class.getSimpleName()+"<"+GenKeys.COMPOSITE_NAME+"<E>>  composite"+GenKeys.COMPOSITE_NAME+"() {\n" +
                "\t" +
                "return new "+CompositeColumns.class.getSimpleName()+"<>(\n"
                +GenKeys.COMPOSITE_KEY_METHOD+

                ");\n\t\t}";
}
