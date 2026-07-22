package io.daobab.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a database assembled from {@link DaobabTable} definitions.
 * <p>
 * During the compilation the daobab annotation processor generates a single interface named
 * after {@link #name()} with the 'Tables' suffix, exposing every entity of the database as an
 * initialized field ready to be used in the queries - mirroring the hand written
 * {@code MetaDataTables} interface. The tables can be listed explicitly with {@link #tables()}:
 * <pre>{@code
 * @DaobabDataBase(
 *     name = "Library",
 *     tables = {BookDef.class, AuthorDef.class})
 * public interface LibraryConfig {
 * }
 * }</pre>
 * or picked up from a whole package with {@link #tablesPackage()} - every {@link DaobabTable}
 * interface of that package (compiled in the same compilation) is used:
 * <pre>{@code
 * @DaobabDataBase(name = "Library", tablesPackage = "com.acme.library.def")
 * public interface LibraryConfig {
 * }
 * }</pre>
 * When neither {@link #tables()} nor {@link #tablesPackage()} is given, the package of the annotated
 * element is scanned by default (and a note is logged), which is handy when the definitions and this
 * element share a package. Either form produces the {@code LibraryTables} interface:
 * <pre>{@code
 * public interface LibraryTables extends QueryWhisperer {
 *
 *     BookEntity tabBook = new BookEntity();
 *     AuthorEntity tabAuthor = new AuthorEntity();
 * }
 * }</pre>
 * where {@code BookEntity} and {@code AuthorEntity} are the entities generated out of the
 * {@code BookDef} and {@code AuthorDef} definitions.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 * @see DaobabTable
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface DaobabDataBase {

    /**
     * Database name. The generated interface is named after it with the 'Tables' suffix
     * (e.g. {@code name = "Library"} produces the {@code LibraryTables} interface).
     */
    String name();

    /**
     * The entity definition interfaces of the database, each annotated with {@link DaobabTable}.
     * The generated interface exposes one initialized field per definition. May be combined with
     * {@link #tablesPackage()}. When neither is set, the annotated element's own package is scanned
     * (a note is logged).
     */
    Class<?>[] tables() default {};

    /**
     * Package scanned for {@link DaobabTable} definitions: every such interface of the package
     * (compiled in the same compilation as this element) becomes a table of the database, in
     * addition to the ones listed in {@link #tables()}. When neither {@code tables} nor
     * {@code tablesPackage} is set, the package of the annotated element is scanned by default.
     */
    String tablesPackage() default "";

    /**
     * Package of the generated interface. By default, the package of the annotated element.
     */
    String targetPackage() default "";
}
