package dev.kaldiroglu.dp.structural.decorator.gof.stream.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

import java.util.Objects;

/**
 * The Decorator of GoF's stream example (p. 183).
 * <p>
 * A stream decorator has a buffer of its own. When that buffer fills it transforms the
 * contents and hands them to the stream it wraps, which buffers them in turn. Data
 * therefore flows outward through the chain, one transformation per link.
 * <p>
 * {@link #close()} is the detail people forget: a decorator must flush its own buffer
 * <em>and then</em> close what it wraps, or the last block is silently lost.
 */
public abstract class StreamDecorator extends Stream {

    private final Stream component;

    protected StreamDecorator(Stream component, int bufferSize) {
        super(bufferSize);
        this.component = Objects.requireNonNull(component, "a decorator must decorate something");
    }

    /** Passes a transformed buffer on to the wrapped stream. */
    protected final void forward(String data) {
        component.putString(data);
    }

    @Override
    protected void handleBufferFull() {
        forward(takeBuffer()); // the default decorator changes nothing
    }

    @Override
    public void close() {
        super.close();
        component.close();
    }
}
