package dev.kaldiroglu.dp.structural.decorator.gof.stream.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * A second ConcreteComponent. Adding it costs one class, and every existing decorator
 * works with it immediately — {@code new CompressingStream(new SocketStream())} needs no
 * new code at all. In the {@code problem} package the same step required a
 * {@code CompressingSocketStream}.
 */
public final class SocketStream extends Stream {

    private final List<String> packets = new ArrayList<>();

    public SocketStream() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public SocketStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        packets.add(takeBuffer());
    }

    public List<String> packets() {
        return List.copyOf(packets);
    }

    public String contents() {
        return String.join("", packets);
    }
}
