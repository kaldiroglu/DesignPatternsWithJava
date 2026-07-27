package dev.kaldiroglu.dp.structural.decorator.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The library's own decorators, measured. The same two methods are run over three different
 * stacks and produce the same invoice every time, while the bytes on disk differ. That gap
 * between "same answer" and "different work" is the whole benefit of the pattern.
 */
class DataInputOutputStreamDemoTest {

    /** Silences the demo's own printing so the test output stays readable. */
    private static double readQuietly(DataInputStream in) throws IOException {
        PrintStream original = System.out;
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        try {
            return DataInputOutputStreamDemo.readInvoice(in);
        } finally {
            System.setOut(original);
        }
    }

    private static Path writeThrough(boolean compressed) throws IOException {
        Path file = Files.createTempFile("invoice", ".dat");
        file.toFile().deleteOnExit();

        OutputStream sink = new FileOutputStream(file.toFile());
        if (compressed) {
            sink = new GZIPOutputStream(sink);
        }
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sink))) {
            DataInputOutputStreamDemo.writeInvoice(out);
        }
        return file;
    }

    @Test
    @DisplayName("the plain chain round-trips the invoice")
    void plainChainRoundTrips() throws IOException {
        Path file = writeThrough(false);

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file.toFile())))) {
            assertEquals(DataInputOutputStreamDemo.EXPECTED_TOTAL, readQuietly(in), 0.005);
        }
    }

    @Test
    @DisplayName("adding a compression decorator changes the bytes and not the invoice")
    void compressionChangesTheBytesOnly() throws IOException {
        Path plain = writeThrough(false);
        Path compressed = writeThrough(true);

        // Different work: the file really is smaller.
        assertTrue(Files.size(compressed) < Files.size(plain),
                "gzip should shrink the invoice: " + Files.size(compressed) + " vs " + Files.size(plain));
        assertNotEquals(Files.size(plain), Files.size(compressed));

        // Same answer: writeInvoice was not recompiled, reconfigured or told anything.
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(
                new GZIPInputStream(new FileInputStream(compressed.toFile()))))) {
            assertEquals(DataInputOutputStreamDemo.EXPECTED_TOTAL, readQuietly(in), 0.005);
        }
    }

    @Test
    @DisplayName("the library's stream classes really are decorators")
    void theRolesAreWhereTheSlidesSayTheyAre() {
        // ConcreteDecorator extends Decorator ...
        assertTrue(java.io.FilterOutputStream.class.isAssignableFrom(DataOutputStream.class));
        assertTrue(java.io.FilterInputStream.class.isAssignableFrom(DataInputStream.class));
        assertTrue(java.io.FilterInputStream.class.isAssignableFrom(BufferedInputStream.class));
        assertTrue(java.io.FilterOutputStream.class.isAssignableFrom(PrintStream.class));

        // ... Decorator is itself a Component ...
        assertTrue(OutputStream.class.isAssignableFrom(java.io.FilterOutputStream.class));

        // ... and ConcreteComponent is a Component that decorates nothing.
        assertTrue(OutputStream.class.isAssignableFrom(FileOutputStream.class));
        assertEquals(OutputStream.class, FileOutputStream.class.getSuperclass());

        // A caution for the slide: not every wrapper in the package is a decorator.
        assertNotEquals(java.io.FilterOutputStream.class,
                java.io.ObjectOutputStream.class.getSuperclass());
        assertEquals(OutputStream.class, java.io.ObjectOutputStream.class.getSuperclass());
    }
}
