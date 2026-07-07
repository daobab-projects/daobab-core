package io.daobab.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional column details for a method of a {@link DaobabTable} definition.
 * A method without this annotation is still treated as a column with the default settings.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 * @see DaobabTable
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface DaobabColumn {

    /**
     * Database column name. By default the method name converted to UPPER_SNAKE_CASE.
     */
    String name() default "";

    /**
     * Marks the primary key column. At most one column of the definition may be a primary key.
     */
    boolean primaryKey() default false;

    int size() default 0;

    int precision() default 0;

    int scale() default 0;

    boolean notNull() default false;

    boolean unique() default false;

    boolean lob() default false;
}
