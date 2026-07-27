package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

import java.nio.charset.StandardCharsets;

/**
 * Homework 1 — the invoice pipeline.
 * <p>
 * Four requirements arrived over six months and not one of them changed {@link PlainInvoice}.
 * The interesting part is not that the stages compose, it is that two of the orderings are
 * simply <em>wrong</em>, and nothing in the type system says so.
 */
public class Main {

    private static final long CUSTOMER_KEY = 20260728L;

    public static void main(String[] args) {
        byte[] invoice = sampleInvoice();
        System.out.println("plain invoice                          " + invoice.length + " bytes");

        // The order the customer asked for, and the order that is correct.
        Pipeline correct =
                new Checksummed(
                        new Encrypted(
                                new Compressed(
                                        new WithHeader(new PlainInvoice(), "CUSTOMER 4417")),
                                CUSTOMER_KEY));

        // The same four stages, two of them swapped.
        Pipeline wrong =
                new Checksummed(
                        new Compressed(
                                new Encrypted(
                                        new WithHeader(new PlainInvoice(), "CUSTOMER 4417"),
                                        CUSTOMER_KEY)));

        byte[] good = correct.process(invoice);
        byte[] bad = wrong.process(invoice);

        System.out.println("compress, then encrypt                 " + good.length + " bytes");
        System.out.println("encrypt, then compress                 " + bad.length + " bytes");
        System.out.println("cost of getting the order wrong        "
                + (bad.length - good.length) + " bytes");
        System.out.println();
        System.out.println("Encrypted bytes are high-entropy, so compressing them afterwards");
        System.out.println("achieves nothing. The compiler has no opinion about this.");

        byte[] back = correct.undo(good);
        System.out.println("\nround trip through the mirror chain:  "
                + (java.util.Arrays.equals(invoice, back) ? "byte-identical" : "FAILED"));
    }

    /** Repetitive on purpose: real invoices are, which is why compression is worth having. */
    static byte[] sampleInvoice() {
        StringBuilder text = new StringBuilder("INVOICE 4417\n");
        for (int line = 1; line <= 40; line++) {
            text.append("item ").append(line)
                .append("  qty 2  unit 34.99  vat 20%  total 83.98\n");
        }
        return text.toString().getBytes(StandardCharsets.UTF_8);
    }
}
