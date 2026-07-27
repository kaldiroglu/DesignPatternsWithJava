package dev.kaldiroglu.dp.structural.decorator.gof.stream.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Codecs;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

/**
 * A ConcreteDecorator: compresses the buffer on its way out (GoF p. 183).
 * <p>
 * It works with a file stream, a socket stream, or another decorator, because it depends
 * only on {@link Stream}. One class, every destination.
 */
public final class CompressingStream extends StreamDecorator {

    public CompressingStream(Stream component) {
        this(component, DEFAULT_BUFFER_SIZE);
    }

    public CompressingStream(Stream component, int bufferSize) {
        super(component, bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        forward(Codecs.compress(takeBuffer()));
    }
}
