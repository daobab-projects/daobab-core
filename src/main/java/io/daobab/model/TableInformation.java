package io.daobab.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableInformation {

    String name() default "";

    boolean useMethod() default false;

    IdGeneratorType idGenerator() default IdGeneratorType.NONE;

    String sequenceName() default "";
}
