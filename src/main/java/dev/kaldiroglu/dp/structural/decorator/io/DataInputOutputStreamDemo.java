package dev.kaldiroglu.dp.structural.decorator.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The Decorator solution as the Java library itself uses it.
 * <p>
 * The GoF name {@code java.io} as a known use, and it is the clearest one a Java developer
 * already has on disk. The four roles map onto classes everyone has used:
 * <table border="1">
 *   <caption>The solution's roles in java.io</caption>
 *   <tr><th>GoF role</th><th>java.io</th></tr>
 *   <tr><td>Component</td><td>{@link java.io.InputStream}, {@link java.io.OutputStream}</td></tr>
 *   <tr><td>ConcreteComponent</td><td>{@link FileInputStream}, {@link FileOutputStream}</td></tr>
 *   <tr><td>Decorator</td><td>{@link java.io.FilterInputStream}, {@link java.io.FilterOutputStream}</td></tr>
 *   <tr><td>ConcreteDecorator</td><td>{@link DataInputStream}, {@link BufferedInputStream}, {@link java.io.PrintStream}</td></tr>
 * </table>
 * <p>
 * Not every wrapper in the package is a decorator: {@link java.io.ObjectOutputStream} extends
 * {@code OutputStream} directly rather than {@code FilterOutputStream}, so it is a component
 * in its own right, not a decoration of one.
 * <p>
 * The demonstration below is about the solution rather than about the library.
 * {@link #writeInvoice} and {@link #readInvoice} are written once and never change. They are
 * then run over two <em>different</em> stacks of decorators, and the second stack compresses
 * the file on the way through. The two methods do not know this, cannot find out, and were
 * not recompiled for it. That is what "attach responsibilities to an object dynamically" buys.
 */
public class DataInputOutputStreamDemo {

    private static final String[] items = {
            "Thinking in Java", "JSF Applied", "Java Tutorial", "Java Security", "Swing Programming"
    };
    private static final double[] prices = {34.99, 29.99, 35.99, 32.99, 40.99};
    private static final int[] units = {2, 3, 6, 2, 5};
    private static final char SEPARATOR = ':';
    private static final String HEADER = "   - - - I N V O I C E - - -    ";

    /** What the invoice must add up to, so the demo can check itself out loud. */
    static final double EXPECTED_TOTAL = 646.82;

    public static void main(String[] args) throws IOException {
        // Temporary files, so this runs on any machine. The first version of this demo
        // hardcoded a path on one particular Desktop, which is a reliable way to make a
        // demonstration fail in front of a room.
        Path plain = Files.createTempFile("invoice-plain", ".dat");
        Path compressed = Files.createTempFile("invoice-gzip", ".dat");
        plain.toFile().deleteOnExit();
        compressed.toFile().deleteOnExit();

        // Three layers: the file, buffering, then data formatting.
        // FileOutputStream -> BufferedOutputStream -> DataOutputStream
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(plain.toFile())))) {
            writeInvoice(out);
        }

        // The same invoice, one decorator deeper. Note where compression sits: below the
        // buffer and below the formatting, because it should compress the finished bytes.
        // FileOutputStream -> GZIPOutputStream -> BufferedOutputStream -> DataOutputStream
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(
                        new GZIPOutputStream(
                                new FileOutputStream(compressed.toFile()))))) {
            writeInvoice(out);
        }

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(plain.toFile())))) {
            report("plain  ", Files.size(plain), readInvoice(in));
        }

        // Read back through the mirror image of the chain that wrote it. Every layer added on
        // the way out needs its counterpart on the way in, in the opposite order. That is the
        // one real obligation the solution places on the caller, and it is worth saying aloud.
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        new GZIPInputStream(
                                new FileInputStream(compressed.toFile()))))) {
            report("gzipped", Files.size(compressed), readInvoice(in));
        }

        System.out.printf("Compression saved %d bytes, and writeInvoice() never learned about it.%n",
                Files.size(plain) - Files.size(compressed));

        // Closing the outermost decorator closed every stream underneath it: close() is
        // forwarded down the chain, so the FileOutputStream at the bottom is released even
        // though nobody kept a reference to it. Decoration doing its job, quietly.
    }

    /** Writes the invoice. It knows about data formatting and nothing else. */
    static void writeInvoice(DataOutputStream out) throws IOException {
        out.writeUTF(HEADER);
        for (int i = 0; i < items.length; i++) {
            out.writeChars(items[i]);
            out.writeChar(SEPARATOR);
            out.writeChar('\t');
            out.writeInt(units[i]);
            out.writeChar('\t');
            out.writeDouble(prices[i]);
            out.writeChar('\n');
        }
    }

    /** Reads the invoice back and returns its total. */
    static double readInvoice(DataInputStream in) throws IOException {
        double totalPrice = 0;

        System.out.println(in.readUTF());
        for (int i = 0; i < items.length; i++) {
            char c;
            while ((c = in.readChar()) != SEPARATOR) {
                System.out.print(c);
            }
            System.out.print(c);              // the ':'
            System.out.print(in.readChar());  // the '\t'

            int unit = in.readInt();
            System.out.print(unit);
            System.out.print(in.readChar());  // the '\t'

            double priceRead = in.readDouble();
            totalPrice += unit * priceRead;
            System.out.print(priceRead);
            System.out.print(in.readChar());  // the '\n' that ends the row
        }
        return totalPrice;
    }

    private static void report(String label, long bytes, double total) {
        System.out.printf("%s  total: $%.2f  file: %d bytes  [%s]%n%n",
                label, total, bytes,
                Math.abs(total - EXPECTED_TOTAL) < 0.005 ? "total as expected" : "TOTAL WRONG");
    }
}
