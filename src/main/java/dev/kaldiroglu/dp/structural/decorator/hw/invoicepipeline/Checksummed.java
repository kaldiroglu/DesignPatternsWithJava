package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Appends a SHA-256 of everything below it, and verifies it on the way back.
 * <p>
 * <strong>Where this stage sits is the whole exercise.</strong> A checksum is worth having
 * only if it covers the bytes that actually travel, so it belongs <em>outermost</em>. Put it
 * underneath the compression and it certifies bytes nobody will ever transmit: the file can
 * be corrupted in flight and still pass, because the corruption happens to the compressed
 * form that the digest never saw.
 */
public final class Checksummed extends PipelineStage {

    private static final int DIGEST_LENGTH = 32;

    public Checksummed(Pipeline inner) {
        super(inner);
    }

    @Override
    protected byte[] forward(byte[] bytes) {
        byte[] digest = sha256(bytes);
        byte[] out = Arrays.copyOf(bytes, bytes.length + DIGEST_LENGTH);
        System.arraycopy(digest, 0, out, bytes.length, DIGEST_LENGTH);
        return out;
    }

    @Override
    protected byte[] backward(byte[] bytes) {
        if (bytes.length < DIGEST_LENGTH) {
            throw new IllegalArgumentException("too short to carry a checksum");
        }
        int split = bytes.length - DIGEST_LENGTH;
        byte[] payload = Arrays.copyOf(bytes, split);
        byte[] found = Arrays.copyOfRange(bytes, split, bytes.length);
        if (!Arrays.equals(sha256(payload), found)) {
            throw new IllegalStateException("checksum does not match: the file was altered");
        }
        return payload;
    }

    /** The digest of an arbitrary block, so a test can show what a chain did and did not cover. */
    public static byte[] sha256(byte[] bytes) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("every JVM ships SHA-256", e);
        }
    }
}
