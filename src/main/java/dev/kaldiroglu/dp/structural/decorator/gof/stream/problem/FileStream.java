package dev.kaldiroglu.dp.structural.decorator.gof.stream.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * Writes buffers to a "file" — here a list of blocks, so tests can read back exactly
 * what was written.
 */
public class FileStream extends Stream {

    private final List<String> blocks = new ArrayList<>();

    public FileStream() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public FileStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        store(takeBuffer());
    }

    /** Puts one block on "disk". Subclasses transform the buffer before calling this. */
    protected final void store(String block) {
        blocks.add(block);
    }

    public List<String> blocks() {
        return List.copyOf(blocks);
    }

    public String contents() {
        return String.join("", blocks);
    }
}
