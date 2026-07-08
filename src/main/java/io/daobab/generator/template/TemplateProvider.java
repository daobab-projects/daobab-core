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
                return switch (type) {
                    case TABLE_CLASS -> JavaTemplates.TABLE_CLASS_TEMP;
                    case DTO_CLASS -> JavaTemplates.DTO_CLASS_TEMP;
                    case DEFINITION_INTERFACE -> JavaTemplates.DEFINITION_INTERFACE_TEMP;
                    case COLUMN_INTERFACE -> JavaTemplates.COLUMN_INTERFACE_TEMP;
                    case DATABASE_TABLES_INTERFACE -> JavaTemplates.DATABASE_TABLES_INTERFACE_TEMP;
                    case DATA_BASE_TARGET_CLASS -> JavaTemplates.DATA_BASE_TARGET_CLASS_TEMP;
                    case COMPOSITE_KEY_TEMP -> JavaTemplates.COMPOSITE_KEY_TEMP;
                    case PK_COL_METHOD -> JavaTemplates.PK_COL_METHOD_TEMP;
                    case COMPOSITE_PK_KEY_METHOD -> JavaTemplates.COMPOSITE_PK_KEY_METHOD_TEMP;
                    case COMPOSITE_METHOD -> JavaTemplates.COMPOSITE_METHOD_TEMP;
                    default -> "";
                };
            }
            case KOTLIN: {
                return switch (type) {
                    case TABLE_CLASS -> KotlinTemplates.TABLE_CLASS_TEMP;
                    case DTO_CLASS -> KotlinTemplates.DTO_CLASS_TEMP;
                    case COLUMN_INTERFACE -> KotlinTemplates.COLUMN_INTERFACE_TEMP;
                    case DATABASE_TABLES_INTERFACE -> KotlinTemplates.DATABASE_TABLES_INTERFACE_TEMP;
                    case DATA_BASE_TARGET_CLASS -> KotlinTemplates.DATA_BASE_TARGET_CLASS_TEMP;
                    case COMPOSITE_KEY_TEMP -> KotlinTemplates.COMPOSITE_KEY_TEMP;
                    case PK_COL_METHOD -> KotlinTemplates.PK_COL_METHOD_TEMP;
                    case COMPOSITE_PK_KEY_METHOD -> KotlinTemplates.COMPOSITE_PK_KEY_METHOD_TEMP;
                    case COMPOSITE_METHOD -> KotlinTemplates.COMPOSITE_METHOD_TEMP;
                    default -> "";
                };
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
        return switch (language) {
            default -> ".java";
            case KOTLIN -> ".kt";
            case TYPE_SCRIPT -> ".ts";
        };
    }
}
