package dev.kaldiroglu.dp.structural.decorator.gof.stream.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Codecs;

/** A file stream that converts to 7-bit ASCII. Subclass two of six. */
public class ASCII7FileStream extends FileStream {

    public ASCII7FileStream() {
    }

    public ASCII7FileStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        store(Codecs.toAscii7(takeBuffer()));
    }
}
