package io.daobab.generator;

import io.daobab.error.DaobabException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface SaveGenerated {


    static void saveGeneratedTo(String fileContent, String fpath, String catalog, String schema, String subfolder, String filename, FileType type, boolean override) {
        if (type == null) throw new DaobabException("File type must be provided");

        StringBuilder sbfol = new StringBuilder();

        sbfol.append(fpath).append(File.separator);

        StringBuilder properCatalog = JavaPackageResolver.resolveCatalog(catalog);
        StringBuilder properSchema = JavaPackageResolver.resolveSchema(schema);

        if (properCatalog.length() > 0) {
            sbfol.append(properCatalog);
            sbfol.append(File.separator);
        }

        if (properSchema.length() > 0) {
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
        Path p = Paths.get(sbfol.append(filename).append(type.getExtension()).toString());

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
