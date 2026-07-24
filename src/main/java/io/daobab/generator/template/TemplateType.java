package io.daobab.generator.template;


/**
 * The kinds of source artifact the generator can emit, each backed by a language template (see
 * {@link TemplateProvider}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public enum TemplateType {

    /**
     * The {@code Tables} interface listing every entity of the database.
     */
    DATABASE_TABLES_INTERFACE,
    /** The {@code DataBaseTarget} subclass wiring the tables to a data source. */
    DATA_BASE_TARGET_CLASS,
    /** An entity class. */
    TABLE_CLASS,
    /** A DTO class. */
    DTO_CLASS,
    /** An annotated {@code @DaobabTable} definition interface (input for the annotation processor). */
    DEFINITION_INTERFACE,
    /** A shared column interface. */
    COLUMN_INTERFACE,
    /** A composite-key type. */
    COMPOSITE_KEY_TEMP,
    /** The {@code colID()} primary-key accessor. */
    PK_COL_METHOD,
    /** The {@code colCompositeId()} composite-key accessor. */
    COMPOSITE_PK_KEY_METHOD,
    /** The composite-key columns method. */
    COMPOSITE_METHOD,
}
