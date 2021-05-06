package io.daobab.statement.base;

import io.daobab.internallogger.ILoggerBean;

import java.util.List;
import java.util.function.Supplier;


public interface TestText {

    static void notify(List<String> list, ILoggerBean logger) {

        String lineSeparator = System.getProperty("line.separator");

        StringBuilder mg = new StringBuilder();
        mg.append(lineSeparator);

        int maxsize = 0;
        for (String s : list) {
            if (maxsize < s.length()) maxsize = s.length();
        }
        int height = list.size();

        int linewidth = 8 + maxsize;
        boolean fshown = false;

        for (int l = 0; l < height; l++) {
            StringBuilder row = new StringBuilder();
            row.append("***");

            for (int i = 0; i < ((maxsize - list.get(l).length()) / 2); i++) {
                row.append("*");
            }

            row.append(" ");
            row.append(list.get(l));
            row.append(" ");
            int rows = row.length();
            for (int i = rows; i < linewidth; i++) {
                row.append("*");
            }
            if (fshown) mg.append(lineSeparator);
            fshown = true;
            mg.append(row.toString());
        }
        n(() -> {
            if (logger == null || logger.getLog() == null) {
                logger.getLog().info(mg.toString());
            } else {
                logger.getLog().info(mg.toString());
            }
            return null;
        });
    }

    static void n(Supplier<Void> s) {
        if (s == null) return;
        s.get();
    }

}
