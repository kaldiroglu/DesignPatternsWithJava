package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Four stages give twenty-four orderings and the compiler accepts all of them. These tests
 * measure the two that are wrong, so the homework's claim rests on numbers.
 */
class InvoicePipelineTest {

    private static final long KEY = 20260728L;
    private final byte[] invoice = Main.sampleInvoice();

    private Pipeline header() {
        return new WithHeader(new PlainInvoice(), "CUSTOMER 4417");
    }

    @Test
    @DisplayName("the chain runs backwards as the exact mirror of the chain that wrote it")
    void roundTrip() {
        Pipeline chain = new Checksummed(new Encrypted(new Compressed(header()), KEY));
        assertArrayEquals(invoice, chain.undo(chain.process(invoice)));
    }

    @Test
    @DisplayName("encrypting before compressing throws the compression away")
    void encryptedBytesDoNotCompress() {
        int compressThenEncrypt =
                new Encrypted(new Compressed(header()), KEY).process(invoice).length;
        int encryptThenCompress =
                new Compressed(new Encrypted(header(), KEY)).process(invoice).length;

        assertTrue(compressThenEncrypt < encryptThenCompress,
                compressThenEncrypt + " should be far smaller than " + encryptThenCompress);
        // Not a little worse — the compression achieved nothing at all, so the "compressed"
        // form is larger than the input it was given.
        assertTrue(encryptThenCompress > invoice.length);
        assertTrue(compressThenEncrypt < invoice.length / 4);
    }

    @Test
    @DisplayName("a checksum is only worth having over the bytes that actually travel")
    void checksumMustBeOutermost() {
        byte[] transmitted = new Checksummed(new Compressed(header())).process(invoice);

        // The digest carried in the file is the digest of the compressed bytes ...
        byte[] payload = Arrays.copyOf(transmitted, transmitted.length - 32);
        byte[] carried = Arrays.copyOfRange(transmitted, transmitted.length - 32, transmitted.length);
        assertArrayEquals(Checksummed.sha256(payload), carried);

        // ... and not of the plain ones. Put the stage underneath the compression and it
        // certifies bytes nobody will ever transmit.
        assertFalse(Arrays.equals(Checksummed.sha256(invoice), carried));
    }

    @Test
    @DisplayName("a byte altered in flight is caught on the way back")
    void corruptionIsDetected() {
        Pipeline chain = new Checksummed(new Compressed(header()));
        byte[] transmitted = chain.process(invoice);
        transmitted[10] ^= 0x01;

        assertThrows(IllegalStateException.class, () -> chain.undo(transmitted));
    }

    @Test
    @DisplayName("reading with a chain that is not the mirror image fails loudly")
    void theWrongReadChainIsRejected() {
        byte[] written = new Compressed(header()).process(invoice);
        Pipeline wrongWayRound = new WithHeader(new Compressed(new PlainInvoice()), "CUSTOMER 4417");

        assertThrows(RuntimeException.class, () -> wrongWayRound.undo(written));
    }

    @Test
    @DisplayName("adding a stage never changed the invoice itself")
    void theComponentNeverChanged() {
        assertEquals(2, PlainInvoice.class.getDeclaredMethods().length); // process, undo
        assertEquals(0, PlainInvoice.class.getDeclaredFields().length);
    }
}
