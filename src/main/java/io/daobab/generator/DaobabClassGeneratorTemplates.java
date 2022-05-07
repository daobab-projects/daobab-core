package io.daobab.generator;

import io.daobab.model.Composite;
import io.daobab.model.CompositeColumns;
import io.daobab.model.EntityMap;
import io.daobab.model.TableColumn;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2021
 */
public interface DaobabClassGeneratorTemplates {


    String typeScriptTabletemp = "export class " + GenKeys.TABLE_CAMEL_NAME + " {" +
            "\n" +
            "\n" +
            GenKeys.FIELDS + " \n" +
            "\n" +
            "\n" +
            "}";


    String TABLESINTERFACETEMP = "package " + GenKeys.TARGET_PACKAGE + "\n" +
            "\n" +
            "import io.daobab.parser.ParserGeneral;\n" +
            "import io.daobab.query.base.QueryWhisperer;\n" +
            GenKeys.TAB_IMPORTS + "\n" +
            "\n" +
            "public interface " + GenKeys.TABLES_INTERFACE_NAME + " extends ParserGeneral, QueryWhisperer {\n" +
            "\n" +
            GenKeys.TABLES_INITIATED + "\n" +
            "\n" +
            "}";

    String targettemp = "package " + GenKeys.TARGET_PACKAGE + "\n" +
            "\n" +
            "\n" +
            "import io.daobab.model.Entity;\n" +
            "import io.daobab.target.database.DataBaseTarget;\n" +
            "import io.daobab.target.database.connection.SqlQueryResolver;\n" +
            "\n" +
            "import javax.sql.DataSource;\n" +
            "import java.util.Arrays;\n" +
            "import java.util.List;\n" +
            "\n" +
            "\n" +
            "public class " + GenKeys.TARGET_CLASS_NAME + " extends DataBaseTarget implements " + GenKeys.TARGET_TABLES_INTERFACE + ", SqlQueryResolver {\n" +
            "    @Override\n" +
            "    protected DataSource initDataSource() {\n" +
            "\n" +
            "        return /* return DataSource here */;\n" +
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

    String tabtemp = "package " + GenKeys.TABLE_PACKAGE + ";\n" +
            "\n" +
            "import com.fasterxml.jackson.annotation.JsonAutoDetect;\n" +
            "import com.fasterxml.jackson.annotation.JsonInclude;\n" +
            "import io.daobab.clone.EntityDuplicator;\n" +
            "import io.daobab.model.Column;\n" +
            "import io.daobab.model.TableColumn;\n" +
            GenKeys.PK_IMPORT + "\n" +
            GenKeys.COLUMN_IMPORTS + "\n" +
            "import io.daobab.model.Table;\n" +
            "\n" +
            GenKeys.PK_TYPE_IMPORT + "\n" +
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


    String COLUMN_TEMPLATE = "package " + GenKeys.PACKAGE + ";\n" +
            "\n" +
            "import io.daobab.error.AttemptToReadFromNullEntityException;\n" +
            "import io.daobab.error.AttemptToWriteIntoNullEntityException;\n" +
            "import io.daobab.model.Column;\n" +
            "import io.daobab.model.EntityRelationMap;\n" +
            "import io.daobab.model.EntityMap;\n" +
            "import java.util.Objects;\n" +
            "\n" +
            GenKeys.CLASS_FULL_NAME + "\n" +
            "\n" +
            "public interface " + GenKeys.INTERFACE_NAME + "<E extends EntityMap> extends EntityRelationMap<E> {\n" +
            "\n" +
            "\n" +
            "" + GenKeys.TABLES_AND_TYPE +
            "    default " + GenKeys.CLASS_SIMPLE_NAME + " get" + GenKeys.INTERFACE_NAME + "(){return getColumnParam(\"" + GenKeys.FIELD_NAME + "\");}\n" +
            "    default E set" + GenKeys.INTERFACE_NAME + "(" + GenKeys.CLASS_SIMPLE_NAME + " val){setColumnParam(\"" + GenKeys.FIELD_NAME + "\",val); return (E)this;}\n" +
            "\n" +
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

    String COMPOSITE_KEY_TEMP = "package " + GenKeys.TABLE_PACKAGE + ";\n" +
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
}
