package io.daobab.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a Daobab entity and carries its table metadata: the table {@link #name()}, whether the name
 * is computed by a method ({@link #useMethod()}), and the primary-key generation strategy ({@link #idGenerator()}
 * with an optional {@link #sequenceName()}).
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableInformation {

    /**
     * The database table name.
     */
    String name() default "";

    /** Whether the table name is provided by a method on the entity rather than by {@link #name()}. */
    boolean useMethod() default false;

    /** The primary-key generation strategy. */
    IdGeneratorType idGenerator() default IdGeneratorType.NONE;

    /** The sequence name, used when {@link #idGenerator()} is {@link IdGeneratorType#SEQUENCE}. */
    String sequenceName() default "";
}
