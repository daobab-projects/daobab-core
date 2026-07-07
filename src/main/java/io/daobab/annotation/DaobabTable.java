package io.daobab.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an interface as a daobab entity definition.
 * <p>
 * During the compilation, the daobab annotation processor generates a complete entity class
 * (together with the missing column interfaces) out of such a definition, for example:
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
     * Database table name. By default the entity name converted to UPPER_SNAKE_CASE.
     */
    String tableName() default "";

    /**
     * Generated entity class name. By default the definition interface name
     * without the 'Def' or 'Definition' suffix.
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
}
