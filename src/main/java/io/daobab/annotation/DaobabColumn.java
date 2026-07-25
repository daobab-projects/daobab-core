package io.daobab.annotation;

import io.daobab.target.database.converter.type.DatabaseTypeConverter;

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

    /**
     * A {@link DatabaseTypeConverter} pinned to this column, translating its value to and from the raw
     * database (JDBC) representation. When set, the generated column interface wires it into the column
     * (its {@code col...()} returns a {@code Column} whose {@code getColumnTypeConverter()} yields this
     * class), so Daobab uses it in preference to the automatically resolved converter - the compile-time
     * counterpart of overriding {@link io.daobab.model.Column#getColumnTypeConverter()} by hand.
     * <p>
     * The converter's column type (the {@code T} of {@code DatabaseTypeConverter<F, T>}) must match the
     * annotated method's return type; a mismatch (e.g. an {@code Integer} converter on a
     * {@code LocalDateTime} column) fails the compilation. The converter class must expose a no-argument
     * constructor, as the {@code DatabaseConverterManager} instantiates it reflectively.
     * <p>
     * The default - the raw {@link DatabaseTypeConverter} interface - is a sentinel meaning "none": Daobab
     * then picks the converter automatically from the column type.
     */
    Class<? extends DatabaseTypeConverter> typeConverterClass() default DatabaseTypeConverter.class;
}
