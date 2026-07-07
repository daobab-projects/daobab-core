package io.daobab.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an interface as a daobab entity definition.
 * <p>
 * During the compilation, the daobab annotation processor generates a complete entity class
 * (together with the missing column interfaces) and an immutable DTO class
 * out of such a definition, for example:
 * <pre>{@code
 * @DaobabTable(tableName = "BOOK")
 * public interface BookDef {
 *
 *     @DaobabColumn(primaryKey = true)
 *     Integer bookId();
 *
 *     @DaobabColumn(size = 256)
 *     String title();
 *
 *     Timestamp printDate();
 * }
 * }</pre>
 * The example produces the {@code BookEntity} entity and the {@code Book} DTO,
 * connected by the {@code BookEntity.toDto()} and {@code BookEntity.fromDto(Book)} conversions.
 * <p>
 * Every abstract, parameterless method of the interface describes one column:
 * the method name becomes the field name and the return type becomes the field type.
 * The {@link DaobabColumn} annotation is optional and provides the column details.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 * @see DaobabColumn
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface DaobabTable {

    /**
     * Database table name. By default the entity name (without the 'Entity' suffix)
     * converted to UPPER_SNAKE_CASE.
     */
    String tableName() default "";

    /**
     * Generated entity class name. By default the definition interface name
     * without the 'Def' or 'Definition' suffix, with the 'Entity' suffix appended.
     */
    String entityName() default "";

    /**
     * Package of the generated entity. By default the definition package.
     */
    String entityPackage() default "";

    /**
     * Package of the generated column interfaces. By default the definition package + '.column'.
     * Column interfaces are shared: an already existing interface with a matching type is reused.
     */
    String columnPackage() default "";

    /**
     * Whether the DTO class and the toDto/fromDto conversions should be generated.
     */
    boolean generateDto() default true;

    /**
     * Generated DTO class name. By default the definition interface name
     * without the 'Def' or 'Definition' suffix.
     */
    String dtoName() default "";

    /**
     * Package of the generated DTO. By default the entity package.
     */
    String dtoPackage() default "";
}
