package io.daobab.generator;

import io.daobab.error.DaobabException;
import io.daobab.generator.template.TemplateLanguage;
import io.daobab.generator.template.TemplateProvider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes a generated source file to disk under {@code path/catalog/schema/subfolder}, creating the directory
 * tree, using the language's file extension and honoring the {@code override} flag - an existing file is skipped
 * (with a console message) unless overriding is requested.
 *
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public interface SaveGenerated {


    /**
     * Writes {@code fileContent} to {@code path/catalog/schema/subfolder/filename.ext} (UTF-8).
     *
     * @param fileContent the source to write
     * @param fpath       the base output path
     * @param catalog     the database catalog (becomes a sub-directory)
     * @param schema      the database schema (becomes a sub-directory)
     * @param subfolder   an optional extra sub-directory (e.g. {@code column})
     * @param filename    the file name (without extension)
     * @param type        the target language (decides the extension)
     * @param override    whether to overwrite an existing file
     * @throws DaobabException when {@code type} is {@code null}
     */
    static void saveGeneratedTo(String fileContent, String fpath, String catalog, String schema, String subfolder, String filename, TemplateLanguage type, boolean override) {
        if (type == null) throw new DaobabException("File type must be provided");

        StringBuilder sbfol = new StringBuilder();

        sbfol.append(fpath).append(File.separator);

        StringBuilder properCatalog = JavaPackageResolver.resolveCatalog(catalog);
        StringBuilder properSchema = JavaPackageResolver.resolveSchema(schema);

        if (!properCatalog.isEmpty()) {
            sbfol.append(properCatalog);
            sbfol.append(File.separator);
        }

        if (!properSchema.isEmpty()) {
            sbfol.append(properSchema);
            sbfol.append(File.separator);
        }


        if (subfolder != null && !subfolder.trim().isEmpty()) {
            sbfol.append(subfolder.trim().toLowerCase()).append(File.separator);
        }


        File stockDir = new File(sbfol.toString());

        try {
            stockDir.mkdirs();
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        Charset charset = StandardCharsets.UTF_8;
        Path p = Paths.get(sbfol.append(filename).append(TemplateProvider.getFileExtension(type)).toString());

        if (Files.exists(p) && !override) {
            System.out.println("File: " + p.getFileName() + " already exists and can't be overridden. Set setOverride(true) or delete the file.");
            return;
        } else if (Files.exists(p) && override) {
            System.out.println("Overriding a file: " + p + ".");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(p, charset)) {
            writer.write(fileContent, 0, fileContent.length());
        } catch (IOException x) {
            System.out.println(fileContent);
            System.err.format("IOException: %s%n", x);
            x.printStackTrace();
        }

    }

}
