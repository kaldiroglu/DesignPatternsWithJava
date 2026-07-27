package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/** Prepends a fixed customer header, and strips it on the way back. */
public final class WithHeader extends PipelineStage {

    private final byte[] header;

    public WithHeader(Pipeline inner, String header) {
        super(inner);
        this.header = (header + "\n").getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected byte[] forward(byte[] bytes) {
        byte[] out = Arrays.copyOf(header, header.length + bytes.length);
        System.arraycopy(bytes, 0, out, header.length, bytes.length);
        return out;
    }

    @Override
    protected byte[] backward(byte[] bytes) {
        if (bytes.length < header.length
                || !Arrays.equals(header, Arrays.copyOf(bytes, header.length))) {
            throw new IllegalArgumentException("header missing: this chain is not the mirror "
                    + "of the one that wrote the file");
        }
        return Arrays.copyOfRange(bytes, header.length, bytes.length);
    }
}
