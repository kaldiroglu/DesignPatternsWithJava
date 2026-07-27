package dev.kaldiroglu.dp.structural.decorator.gof.stream.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Codecs;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

/**
 * A ConcreteDecorator: converts the buffer to 7-bit ASCII on its way out (GoF p. 183).
 */
public final class ASCII7Stream extends StreamDecorator {

    public ASCII7Stream(Stream component) {
        this(component, DEFAULT_BUFFER_SIZE);
    }

    public ASCII7Stream(Stream component, int bufferSize) {
        super(component, bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        forward(Codecs.toAscii7(takeBuffer()));
    }
}
