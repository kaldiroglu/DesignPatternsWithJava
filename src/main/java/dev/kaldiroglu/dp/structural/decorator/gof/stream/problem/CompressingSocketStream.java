package dev.kaldiroglu.dp.structural.decorator.gof.stream.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Codecs;

/**
 * A socket stream that compresses. Subclass four of six.
 * <p>
 * Its body is character for character the body of {@link CompressingFileStream}. Only
 * the superclass differs. This is the second axis of the explosion: the transformations
 * multiply by the destinations, because a subclass can only be a subclass of one thing.
 */
public class CompressingSocketStream extends SocketStream {

    public CompressingSocketStream() {
    }

    public CompressingSocketStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        store(Codecs.compress(takeBuffer()));
    }
}
