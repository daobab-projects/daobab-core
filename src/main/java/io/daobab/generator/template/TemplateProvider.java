package io.daobab.generator.template;

import java.util.Objects;

/**
 * Resolves the code template for a {@link TemplateLanguage} + {@link TemplateType} pair (from
 * {@link JavaTemplates} / {@link KotlinTemplates} / {@link TypeScriptTemplates}) and the file extension per
 * language. Unsupported combinations return an empty template.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class TemplateProvider {

    private TemplateProvider() {
    }

    /**
     * The template for the given language and artifact type, or an empty string when unsupported.
     */
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


    /** The source file extension for the language ({@code .java} / {@code .kt} / {@code .ts}). */
    public static String getFileExtension(TemplateLanguage language) {
        return switch (language) {
            default -> ".java";
            case KOTLIN -> ".kt";
            case TYPE_SCRIPT -> ".ts";
        };
    }
}
