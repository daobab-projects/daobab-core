package io.daobab.generator.template;

import java.util.Objects;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TemplateProvider {

    private TemplateProvider() {
    }

    public static String getTemplate(TemplateLanguage language, TemplateType type) {
        switch (language) {
            default:
            case JAVA: {
                switch (type) {
                    case TABLE_CLASS:
                        return JavaTemplates.TABLE_CLASS_TEMP;
                    case COLUMN_INTERFACE:
                        return JavaTemplates.COLUMN_INTERFACE_TEMP;
                    case DATABASE_TABLES_INTERFACE:
                        return JavaTemplates.DATABASE_TABLES_INTERFACE_TEMP;
                    case DATA_BASE_TARGET_CLASS:
                        return JavaTemplates.DATA_BASE_TARGET_CLASS_TEMP;
                    case COMPOSITE_KEY_TEMP:
                        return JavaTemplates.COMPOSITE_KEY_TEMP;
                    case PK_COL_METHOD:
                        return JavaTemplates.PK_COL_METHOD_TEMP;
                    case COMPOSITE_PK_KEY_METHOD:
                        return JavaTemplates.COMPOSITE_PK_KEY_METHOD_TEMP;
                    case COMPOSITE_METHOD:
                        return JavaTemplates.COMPOSITE_METHOD_TEMP;
                    default:
                        return "";
                }
            }
            case KOTLIN: {
                switch (type) {
                    case TABLE_CLASS:
                        return KotlinTemplates.TABLE_CLASS_TEMP;
                    case COLUMN_INTERFACE:
                        return KotlinTemplates.COLUMN_INTERFACE_TEMP;
                    case DATABASE_TABLES_INTERFACE:
                        return KotlinTemplates.DATABASE_TABLES_INTERFACE_TEMP;
                    case DATA_BASE_TARGET_CLASS:
                        return KotlinTemplates.DATA_BASE_TARGET_CLASS_TEMP;
                    case COMPOSITE_KEY_TEMP:
                        return KotlinTemplates.COMPOSITE_KEY_TEMP;
                    case PK_COL_METHOD:
                        return KotlinTemplates.PK_COL_METHOD_TEMP;
                    case COMPOSITE_PK_KEY_METHOD:
                        return KotlinTemplates.COMPOSITE_PK_KEY_METHOD_TEMP;
                    case COMPOSITE_METHOD:
                        return KotlinTemplates.COMPOSITE_METHOD_TEMP;
                    default:
                        return "";
                }
            }
            case TYPE_SCRIPT: {
                if (Objects.requireNonNull(type) == TemplateType.TABLE_CLASS) {
                    return TypeScriptTemplates.typeScriptTabletemp;
                }
                return "";
            }
        }
    }


    public static String getFileExtension(TemplateLanguage language) {
        switch (language) {
            default:
            case JAVA:
                return ".java";
            case KOTLIN:
                return ".kt";
            case TYPE_SCRIPT:
                return ".ts";
        }
    }
}
