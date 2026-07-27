package dev.kaldiroglu.dp.structural.decorator.gof.stream.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * A second destination. Its only purpose here is to expose the second axis of the
 * problem: every transformation must be re-implemented for every destination.
 */
public class SocketStream extends Stream {

    private final List<String> packets = new ArrayList<>();

    public SocketStream() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public SocketStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        store(takeBuffer());
    }

    /** Sends one packet. Subclasses transform the buffer before calling this. */
    protected final void store(String packet) {
        packets.add(packet);
    }

    public List<String> packets() {
        return List.copyOf(packets);
    }

    public String contents() {
        return String.join("", packets);
    }
}
