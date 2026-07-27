package dev.kaldiroglu.dp.structural.decorator.gof.stream.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * A ConcreteComponent: it writes blocks and does nothing else. No subclass of it exists
 * in this package, and none is needed.
 */
public final class FileStream extends Stream {

    private final List<String> blocks = new ArrayList<>();

    public FileStream() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public FileStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        blocks.add(takeBuffer());
    }

    public List<String> blocks() {
        return List.copyOf(blocks);
    }

    public String contents() {
        return String.join("", blocks);
    }
}
