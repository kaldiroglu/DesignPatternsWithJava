package dev.kaldiroglu.dp.behavioral.templateMethod.hw.recordfile;

import java.io.IOException;
import java.io.StringReader;

/**
 * Reads a file of customers, then a file with a bad line. The template method closes the
 * reader even when a line fails; the subclass only parses one line.
 */
public final class Main {

    public static void main(String[] args) throws IOException {
        CustomerFileReader reader = new CustomerFileReader();
        System.out.println("Customers: "
                + reader.readAll(new StringReader("Ayse;Istanbul\n\nDeniz ; Izmir\n")));

        boolean[] closed = {false};
        StringReader bad = new StringReader("Ayse;Istanbul\nnot a customer\n") {
            @Override
            public void close() {
                closed[0] = true;
                super.close();
            }
        };
        try {
            reader.readAll(bad);
        } catch (IllegalArgumentException e) {
            System.out.println("Bad line: " + e.getMessage());
        }
        System.out.println("Reader closed after the bad line: " + closed[0]);
    }
}
