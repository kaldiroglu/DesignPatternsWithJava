package dev.kaldiroglu.dp.structural.decorator.gof.stream.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Codecs;

/**
 * A file stream that compresses <em>and</em> converts to 7-bit ASCII. Subclass three of six.
 * <p>
 * It extends {@link CompressingFileStream} but inherits nothing useful from it: the
 * transformation pipeline has to be written out again, in full, because the parent
 * applies only half of it and there is no way to insert a step. Note also that the
 * <em>order</em> of the two transformations is now frozen into the class name and body.
 * The opposite order would be a fourth class.
 */
public class CompressingASCII7FileStream extends CompressingFileStream {

    public CompressingASCII7FileStream() {
    }

    public CompressingASCII7FileStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        store(Codecs.toAscii7(Codecs.compress(takeBuffer())));
    }
}
