package io.daobab.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software 2018-2022
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IdGenerator {
    IdGeneratorType type() default IdGeneratorType.NONE;

    String sequenceName() default "";

}
