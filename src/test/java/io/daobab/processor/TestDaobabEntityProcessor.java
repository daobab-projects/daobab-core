package io.daobab.processor;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.MockDataBase;
import org.junit.jupiter.api.Test;

import javax.tools.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Compiles the entity definitions with the daobab annotation processor
 * and verifies the generated entities, using nothing but the JDK compiler API.
 */
class TestDaobabEntityProcessor {

    private static final String BOOK_DEFINITION = """
            package apttest;
            
            import io.daobab.annotation.DaobabColumn;
            import io.daobab.annotation.DaobabTable;
            import java.sql.Timestamp;
            
            @DaobabTable(tableName = "BOOK")
            public interface BookDef {
            
                @DaobabColumn(primaryKey = true)
                Integer bookId();
            
                @DaobabColumn(name = "TITLE", size = 256, notNull = true)
                String title();
            
                Timestamp printDate();
            
                int pages();
            }
            """;

    private CompilationResult compile(List<String> fileNames, List<String> sources) throws IOException {
        Path workDir = Files.createTempDirectory("daobab-apt-test");
        Path srcDir = Files.createDirectories(workDir.resolve("src"));
        Path classesDir = Files.createDirectories(workDir.resolve("classes"));
        Path generatedDir = Files.createDirectories(workDir.resolve("generated"));

        List<Path> files = new ArrayList<>();
        for (int i = 0; i < fileNames.size(); i++) {
            Path file = srcDir.resolve(fileNames.get(i));
            Files.createDirectories(file.getParent());
            Files.writeString(file, sources.get(i), StandardCharsets.UTF_8);
            files.add(file);
        }

        String daobabClasses;
        try {
            daobabClasses = Paths.get(Entity.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot locate daobab classes", e);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            List<String> options = List.of(
                    "-classpath", daobabClasses,
                    "-d", classesDir.toString(),
                    "-s", generatedDir.toString(),
                    "-processor", DaobabEntityProcessor.class.getName());

            boolean success = Boolean.TRUE.equals(compiler.getTask(null, fileManager, diagnostics, options, null,
                    fileManager.getJavaFileObjectsFromPaths(files)).call());

            String messages = diagnostics.getDiagnostics().stream()
                    .map(Diagnostic::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
            return new CompilationResult(success, classesDir, generatedDir, messages);
        }
    }

    @Test
    void generatesWorkingEntity() throws Exception {
        CompilationResult result = compile(List.of("apttest/BookDef.java"), List.of(BOOK_DEFINITION));

        assertTrue(result.success(), "compilation failed: " + result.diagnostics());
        assertTrue(Files.exists(result.sourcesDir().resolve("apttest/BookEntity.java")));
        assertTrue(Files.exists(result.sourcesDir().resolve("apttest/Book.java")));
        assertTrue(Files.exists(result.sourcesDir().resolve("apttest/column/Title.java")));

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{result.classesDir().toUri().toURL()}, Entity.class.getClassLoader())) {

            Class<?> bookClass = loader.loadClass("apttest.BookEntity");
            Object book = bookClass.getConstructor().newInstance();

            //columns list: bookId, title, printDate, pages
            assertEquals(4, ((Entity) book).columns().size());

            //getter and setter delegate to the entity parameters map
            book = bookClass.getMethod("setTitle", String.class).invoke(book, "Dune");
            assertEquals("Dune", bookClass.getMethod("getTitle").invoke(book));

            //primitive definition method is boxed
            book = bookClass.getMethod("setPages", Integer.class).invoke(book, 412);
            assertEquals(412, bookClass.getMethod("getPages").invoke(book));

            //primary key support
            @SuppressWarnings({"rawtypes", "unchecked"})
            PrimaryKey pk = (PrimaryKey) book;
            pk = (PrimaryKey) pk.setId(7);
            assertEquals(7, pk.getId());

            //the generated entity works with the SQL generation end to end
            String sql = new MockDataBase().select((Entity) book).toSqlQuery();
            assertTrue(sql.contains("BOOK"), "unexpected sql: " + sql);
            assertTrue(sql.contains("BOOK_ID"), "unexpected sql: " + sql);
            assertTrue(sql.contains("PRINT_DATE"), "unexpected sql: " + sql);

            //the DTO round trip: entity -> dto -> entity (pk holds title, pages and the id)
            Class<?> dtoClass = loader.loadClass("apttest.Book");
            Object dto = bookClass.getMethod("toDto").invoke(pk);
            assertEquals(dtoClass, dto.getClass());
            assertEquals("Dune", dtoClass.getMethod("getTitle").invoke(dto));
            assertEquals(412, dtoClass.getMethod("getPages").invoke(dto));

            Object bookBack = bookClass.getMethod("fromDto", dtoClass).invoke(null, dto);
            assertEquals("Dune", bookClass.getMethod("getTitle").invoke(bookBack));
            assertEquals(7, bookClass.getMethod("getBookId").invoke(bookBack));

            //the DTO can also be built directly
            Object builder = dtoClass.getMethod("builder").invoke(null);
            builder.getClass().getMethod("title", String.class).invoke(builder, "Solaris");
            Object builtDto = builder.getClass().getMethod("build").invoke(builder);
            assertEquals("Solaris", dtoClass.getMethod("getTitle").invoke(builtDto));
        }
    }

    @Test
    void twoDefinitionsShareColumnInterface() throws Exception {
        String bookDef = BOOK_DEFINITION;
        String magazineDef = """
                package apttest;
                
                import io.daobab.annotation.DaobabTable;
                
                @DaobabTable
                public interface MagazineDef {
                    String title();
                    Integer issueNumber();
                }
                """;

        CompilationResult result = compile(
                List.of("apttest/BookDef.java", "apttest/MagazineDef.java"),
                List.of(bookDef, magazineDef));

        assertTrue(result.success(), "compilation failed: " + result.diagnostics());

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{result.classesDir().toUri().toURL()}, Entity.class.getClassLoader())) {

            Class<?> bookClass = loader.loadClass("apttest.BookEntity");
            Class<?> magazineClass = loader.loadClass("apttest.MagazineEntity");
            Class<?> titleColumn = loader.loadClass("apttest.column.Title");

            assertTrue(titleColumn.isAssignableFrom(bookClass));
            assertTrue(titleColumn.isAssignableFrom(magazineClass));
            //Magazine has no primary key column
            assertFalse(PrimaryKey.class.isAssignableFrom(magazineClass));
        }
    }

    private static Method titleGetter(Class<?> entity) {
        return Arrays.stream(entity.getMethods())
                .filter(m -> m.getName().startsWith("getTitle") && m.getParameterCount() == 0)
                .findFirst()
                .orElseThrow(() -> new AssertionError("no title getter on " + entity.getName()));
    }

    @Test
    void columnInterfaceDocumentsEveryUsingTable() throws Exception {
        //Book.title (VARCHAR 256) and Magazine.title (no size) share the Title column interface
        String magazineDef = """
                package apttest;
                
                import io.daobab.annotation.DaobabTable;
                
                @DaobabTable
                public interface MagazineDef {
                    String title();
                }
                """;

        CompilationResult result = compile(
                List.of("apttest/BookDef.java", "apttest/MagazineDef.java"),
                List.of(BOOK_DEFINITION, magazineDef));

        assertTrue(result.success(), "compilation failed: " + result.diagnostics());

        String title = Files.readString(result.sourcesDir().resolve("apttest/column/Title.java"));

        //the doc table sits above the col...() method
        int tableDoc = title.indexOf("<table>");
        int colMethod = title.indexOf("default Column");
        assertTrue(tableDoc >= 0, "no documentation table in:\n" + title);
        assertTrue(tableDoc < colMethod, "the documentation table must sit above colTitle()");

        //every table using the column is listed, with its size
        assertTrue(title.contains("<td>BOOK</td>"), "missing BOOK row in:\n" + title);
        assertTrue(title.contains("<td>MAGAZINE</td>"), "missing MAGAZINE row in:\n" + title);
        assertTrue(title.contains("<td>256</td>"), "missing BOOK size in:\n" + title);
    }

    @Test
    void conflictingColumnTypeIsDisambiguated() throws Exception {
        //Book.title is String, Magazine.title is Integer: instead of failing, the processor
        //disambiguates the colliding column with a type suffix, just like the generator.
        String magazineDef = """
                package apttest;

                import io.daobab.annotation.DaobabTable;
                
                @DaobabTable
                public interface MagazineDef {
                    Integer title();
                }
                """;

        CompilationResult result = compile(
                List.of("apttest/BookDef.java", "apttest/MagazineDef.java"),
                List.of(BOOK_DEFINITION, magazineDef));

        assertTrue(result.success(), "compilation should disambiguate, not fail: " + result.diagnostics());

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{result.classesDir().toUri().toURL()}, Entity.class.getClassLoader())) {

            Class<?> bookClass = loader.loadClass("apttest.BookEntity");
            Class<?> magazineClass = loader.loadClass("apttest.MagazineEntity");

            //the processing order decides which entity keeps the plain 'Title' interface and which
            //one gets the type-qualified variant, so assert on the resolved accessors, not on the names
            Method bookTitle = titleGetter(bookClass);
            Method magazineTitle = titleGetter(magazineClass);

            assertEquals(String.class, bookTitle.getReturnType());
            assertEquals(Integer.class, magazineTitle.getReturnType());
            //the two columns resolved to two distinct interfaces/accessors, no conflict
            assertNotEquals(bookTitle.getName(), magazineTitle.getName());
        }
    }

    @Test
    void compositePrimaryKeyFailsCompilation() throws Exception {
        String definition = """
                package apttest;
                
                import io.daobab.annotation.DaobabColumn;
                import io.daobab.annotation.DaobabTable;
                
                @DaobabTable
                public interface OrderLineDef {
                    @DaobabColumn(primaryKey = true)
                    Integer orderId();
                
                    @DaobabColumn(primaryKey = true)
                    Integer lineNumber();
                }
                """;

        CompilationResult result = compile(List.of("apttest/OrderLineDef.java"), List.of(definition));

        assertFalse(result.success());
        assertTrue(result.diagnostics().contains("Composite primary keys"), "unexpected diagnostics: " + result.diagnostics());
    }

    @Test
    void definitionWithoutSuffixRequiresExplicitDtoName() throws Exception {
        String definition = """
                package apttest;

                import io.daobab.annotation.DaobabTable;

                @DaobabTable
                public interface Book {
                    String title();
                }
                """;

        //the generated DTO would collide with the definition itself
        CompilationResult result = compile(List.of("apttest/Book.java"), List.of(definition));

        assertFalse(result.success());
        assertTrue(result.diagnostics().contains("collide"), "unexpected diagnostics: " + result.diagnostics());

        //the same definition compiles once the DTO name is explicit
        String fixed = definition.replace("@DaobabTable", "@DaobabTable(dtoName = \"BookDto\")");
        CompilationResult fixedResult = compile(List.of("apttest/Book.java"), List.of(fixed));

        assertTrue(fixedResult.success(), "compilation failed: " + fixedResult.diagnostics());
        assertTrue(Files.exists(fixedResult.classesDir().resolve("apttest/BookEntity.class")));
        assertTrue(Files.exists(fixedResult.classesDir().resolve("apttest/BookDto.class")));

        //and also when the DTO generation is disabled
        String noDto = definition.replace("@DaobabTable", "@DaobabTable(generateDto = false)");
        CompilationResult noDtoResult = compile(List.of("apttest/Book.java"), List.of(noDto));

        assertTrue(noDtoResult.success(), "compilation failed: " + noDtoResult.diagnostics());
        assertTrue(Files.exists(noDtoResult.classesDir().resolve("apttest/BookEntity.class")));
        //no DTO source is generated
        assertFalse(Files.exists(noDtoResult.sourcesDir().resolve("apttest/Book.java")));
    }

    private record CompilationResult(boolean success, Path classesDir, Path sourcesDir, String diagnostics) {
    }
}
