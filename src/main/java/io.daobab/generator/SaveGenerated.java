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
        if (catalog != null && !"%".equalsIgnoreCase(catalog) && !catalog.trim().isEmpty()) {
            sbfol.append(catalog.trim().toLowerCase() + File.separator);
        }

        if (schema != null && !"%".equalsIgnoreCase(schema) && !schema.trim().isEmpty()) {
            sbfol.append(schema.trim().toLowerCase() + File.separator);
        }

        if (subfolder != null && !subfolder.trim().isEmpty()) {
            sbfol.append(subfolder.trim().toLowerCase() + File.separator);
        }


//        String folder = (fpath + (schema == null || schema.trim().isEmpty() ? File.separator + (subfolder == null ? "" : subfolder + File.separator) : File.separator + schema.trim().toLowerCase() + File.separator + (subfolder == null ? "" : subfolder + File.separator)));


        File stockDir = new File(sbfol.toString());

        try {
            stockDir.mkdirs();
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        Charset charset = StandardCharsets.UTF_8;
        Path p = Paths.get(sbfol.append(filename).append(type.getExtension()).toString());

        if (Files.exists(p) && !override) {
            System.out.println("File: " + p.getFileName() + " already exists and can't be overrided. If you want to override, setOverride(true) or simple delete file.");
            return;
        } else if (Files.exists(p) && override) {
            System.out.println("Overriding file: " + p.toString() + ".");
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
