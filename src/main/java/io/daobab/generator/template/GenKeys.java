package io.daobab.generator.template;

/**
 * The placeholder tokens (all {@code __UPPER_CASE}) substituted into the code templates by the
 * {@link io.daobab.generator.Replacer} while the generator fills a template - one constant per hole in the
 * {@link JavaTemplates}/{@link KotlinTemplates}/{@link TypeScriptTemplates} strings (package, class name,
 * column methods, DTO fields, ...).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@SuppressWarnings("unused")
public interface GenKeys {

    String TARGET_CLASS_NAME = "__TARGET_CLASS_NAME";
    String TAB_ARRAY = "__TAB_ARRAY";
    String TARGET_TABLES_INTERFACE = "__TARGET_TABLES_INTERFACE";
    String TARGET_PACKAGE = "__TARGET_PACKAGE";
    String TABLES_INTERFACE_NAME = "__TABLES_INTERFACE_NAME";
    String TAB_IMPORTS = "__TAB_IMPORTS";
    String TABLES_INITIATED = "__TABLES_INITIATED";
    String TABLE_CAMEL_NAME = "__TABLE_CAMEL_NAME";
    String FIELDS = "__FIELDS";
    String CLASS_FULL_NAME = "__CLASS_FULL_NAME";
    String COLUMN_NAME = "__COLUMN_NAME";
    String TABLES_AND_TYPE = "__TABLES_AND_TYPE";
    //    String __COLUMN_NAME_REAL="__COLUMN_NAME_REAL";
    String INTERFACE_NAME = "__INTERFACE_NAME";
    String FIELD_NAME = "__FIELD_NAME";
    String DB_TYPE = "__DB_TYPE";
    String PACKAGE = "__PACKAGE";
    String CLASS_SIMPLE_NAME = "__CLASS_SIMPLE_NAME";
    String PK_IMPORT = "__PK_IMPORT";
    String PK_TYPE_IMPORT = "__PK_TYPE_IMPORT";
    String TYPE_IMPORTS = "__TYPE_IMPORTS";
    String COLUMN_IMPORTS = "__COLUMN_IMPORTS";
    String COMPOSITE_KEY_COLUMN_TYPE_INTERFACES = "__COMPOSITE_KEY_COLUMN_TYPE_INTERFACES";
    String COMPOSITE_KEY_COLUMN_INTERFACES = "__COMPOSITE_KEY_COLUMN_INTERFACES";
    String COLUMN_INTERFACES = "__COLUMN_INTERFACES";
    String TABLE_NAME = "__TABLE_NAME";
    String COMPOSITE_NAME = "__COMPOSITE_NAME";
    String COLUMN_METHODS = "__COLUMN_METHODS";
    String PK_ID_METHOD = "__PK_ID_METHOD";
    String COMPOSITE_KEY_METHOD = "__COMPOSITE_KEY_METHOD";
    String PK_INTERFACE = "__PK_INTERFACE";
    String TABLE_PACKAGE = "__TABLE_PACKAGE";
    String TABLE_SUPERCLASS = "__TABLE_SUPERCLASS";
    String DTO_IMPORT = "__DTO_IMPORT";
    String DTO_METHODS = "__DTO_METHODS";
    String DTO_PACKAGE = "__DTO_PACKAGE";
    String DTO_NAME = "__DTO_NAME";
    String DTO_FIELDS = "__DTO_FIELDS";
    String DTO_ASSIGNMENTS = "__DTO_ASSIGNMENTS";
    String DTO_RECORD_COMPONENTS = "__DTO_RECORD_COMPONENTS";
    String DTO_BUILDER_BUILD_ARGS = "__DTO_BUILDER_BUILD_ARGS";
    String DTO_GETTERS = "__DTO_GETTERS";
    String DTO_EQUALS_HASHCODE = "__DTO_EQUALS_HASHCODE";
    String DTO_BUILDER_FIELDS = "__DTO_BUILDER_FIELDS";
    String DTO_BUILDER_METHODS = "__DTO_BUILDER_METHODS";
    String DEFINITION_NAME = "__DEFINITION_NAME";
    String DEFINITION_METHODS = "__DEFINITION_METHODS";
    String DEFINITION_TABLE_ATTRIBUTES = "__DEFINITION_TABLE_ATTRIBUTES";

}
