package io.daobab.statement.base;

import org.junit.jupiter.api.Test;

public class TestMatrix {

    @Test
    public void test() {
        boolean[][] matrix = new boolean[256][256];
        boolean[][] matrix2 = new boolean[256][256];

        matrix[10][23] = true;
        matrix[1][13] = true;
        matrix[0][223] = true;
        matrix2[1][203] = true;
        matrix2[0][223] = true;

        int xxx = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 256; i++) {
            for (short x = 0; x < 256; x++) {
                for (short y = 0; y < 256; y++)
                    if (matrix[x][y]) {
                        xxx++;
                        // System.out.println((x<<8)+y);
                    }
            }
        }

        long stop = System.currentTimeMillis();

        System.out.println("czas " + (stop - start));

        System.out.println("koniec");
        System.out.println(10 * 256 + 23);
        System.out.println(1 * 256 + 13);
        System.out.println(0 * 256 + 223);

    }
}
