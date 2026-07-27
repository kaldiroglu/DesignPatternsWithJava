package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

import java.util.Random;

/**
 * Exclusive-ors the bytes against a keystream derived from the customer's key.
 * <p>
 * <strong>This is not encryption</strong> and must never be used as such. It is here because
 * it has the one property the exercise needs and real encryption also has: the output is
 * high-entropy, so it does not compress. Everything the homework teaches about ordering
 * follows from that property, not from the algorithm.
 */
public final class Encrypted extends PipelineStage {

    private final long key;

    public Encrypted(Pipeline inner, long key) {
        super(inner);
        this.key = key;
    }

    @Override
    protected byte[] forward(byte[] bytes) {
        return xor(bytes);
    }

    @Override
    protected byte[] backward(byte[] bytes) {
        return xor(bytes); // exclusive-or is its own inverse
    }

    private byte[] xor(byte[] bytes) {
        Random keystream = new Random(key);
        byte[] out = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            out[i] = (byte) (bytes[i] ^ (byte) keystream.nextInt(256));
        }
        return out;
    }
}
