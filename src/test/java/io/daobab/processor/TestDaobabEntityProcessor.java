package io.daobab.processor;

import io.daobab.model.Entity;
import io.daobab.model.PrimaryKey;
import io.daobab.target.database.MockDataBase;
import org.junit.jupiter.api.Test;

import javax.tools.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        assertTrue(Files.exists(result.sourcesDir().resolve("apttest/Book.java")));
        assertTrue(Files.exists(result.sourcesDir().resolve("apttest/column/Title.java")));

        try (URLClassLoader loader = new URLClassLoader(
                new URL[]{result.classesDir().toUri().toURL()}, Entity.class.getClassLoader())) {

            Class<?> bookClass = loader.loadClass("apttest.Book");
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

            Class<?> bookClass = loader.loadClass("apttest.Book");
            Class<?> magazineClass = loader.loadClass("apttest.Magazine");
            Class<?> titleColumn = loader.loadClass("apttest.column.Title");

            assertTrue(titleColumn.isAssignableFrom(bookClass));
            assertTrue(titleColumn.isAssignableFrom(magazineClass));
            //Magazine has no primary key column
            assertFalse(PrimaryKey.class.isAssignableFrom(magazineClass));
        }
    }

    @Test
    void conflictingColumnTypeFailsCompilation() throws Exception {
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

        assertFalse(result.success());
        assertTrue(result.diagnostics().contains("conflicts"), "unexpected diagnostics: " + result.diagnostics());
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
    void definitionWithoutSuffixRequiresExplicitEntityName() throws Exception {
        String definition = """
                package apttest;
                
                import io.daobab.annotation.DaobabTable;
                
                @DaobabTable
                public interface Book {
                    String title();
                }
                """;

        CompilationResult result = compile(List.of("apttest/Book.java"), List.of(definition));

        assertFalse(result.success());
        assertTrue(result.diagnostics().contains("collide"), "unexpected diagnostics: " + result.diagnostics());

        //the same definition compiles once the entity name is explicit
        String fixed = definition.replace("@DaobabTable", "@DaobabTable(entityName = \"BookEntity\")");
        CompilationResult fixedResult = compile(List.of("apttest/Book.java"), List.of(fixed));

        assertTrue(fixedResult.success(), "compilation failed: " + fixedResult.diagnostics());
        assertNotEquals(null, fixedResult.classesDir().resolve("apttest/BookEntity.class"));
        assertTrue(Files.exists(fixedResult.classesDir().resolve("apttest/BookEntity.class")));
    }

    private record CompilationResult(boolean success, Path classesDir, Path sourcesDir, String diagnostics) {
    }
}
