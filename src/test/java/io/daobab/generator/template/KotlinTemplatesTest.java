package io.daobab.generator.template;

import io.daobab.generator.Replacer;
import org.junit.jupiter.api.Test;

import static io.daobab.generator.template.TemplateLanguage.KOTLIN;
import static io.daobab.generator.template.TemplateType.PK_COL_METHOD;

public class KotlinTemplatesTest {


    @Test
    void testColumnGenerator() {

        Replacer replacer = new Replacer();

        replacer.add(GenKeys.CLASS_SIMPLE_NAME, "Int")
                .add(GenKeys.PACKAGE, "io.daobab.demo.dao.column")
                .add(GenKeys.INTERFACE_NAME, "AddressId")
                .add(GenKeys.COLUMN_NAME, "ADDRESS_ID")

                .add(GenKeys.TABLES_AND_TYPE, "    /**\n" +
                        "     * ADDRESS: SMALLINT\n" +
                        "     * CUSTOMER: SMALLINT\n" +
                        "     * STAFF: SMALLINT\n" +
                        "     * STORE: SMALLINT\n" +
                        "     */\n")
                .add(GenKeys.FIELD_NAME, "AddressId");

        String result = replacer.replaceAll(KotlinTemplates.COLUMN_INTERFACE_TEMP);

        System.out.println(result);

    }


    @Test
    void testTableGenerator() {

        Replacer replacer = new Replacer();

        replacer.add(GenKeys.CLASS_SIMPLE_NAME, "Int")
                .add(GenKeys.PK_IMPORT, "io.daobab.model.PrimaryKey")
                .add(GenKeys.TABLE_PACKAGE, "io.daobab.demo.dao.table")
                .add(GenKeys.TABLE_CAMEL_NAME, "Address")
                .add(GenKeys.PK_ID_METHOD, new Replacer()
                        .add(GenKeys.TABLE_NAME, "Address")
                        .add(GenKeys.PK_TYPE_IMPORT, "Long")
                        .add(GenKeys.CLASS_SIMPLE_NAME, "AddressId")
                        .add(GenKeys.INTERFACE_NAME, "ActorId")
                        .replaceAll(TemplateProvider.getTemplate(KOTLIN, PK_COL_METHOD)))
                .add(GenKeys.TABLE_NAME, "ADDRESS")
                .add(GenKeys.FIELD_NAME, "AddressId");

        String result = replacer.replaceAll(KotlinTemplates.TABLE_CLASS_TEMP);

        System.out.println(result);

    }
}
