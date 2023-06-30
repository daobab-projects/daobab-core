package io.daobab.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface TableName {

    String value() default "";

    boolean useMethod() default false;
}
