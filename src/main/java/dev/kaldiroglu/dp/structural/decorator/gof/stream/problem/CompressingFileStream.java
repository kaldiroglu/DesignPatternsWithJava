package dev.kaldiroglu.dp.structural.decorator.gof.stream.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Codecs;

/** A file stream that compresses. Subclass one of six. */
public class CompressingFileStream extends FileStream {

    public CompressingFileStream() {
    }

    public CompressingFileStream(int bufferSize) {
        super(bufferSize);
    }

    @Override
    protected void handleBufferFull() {
        store(Codecs.compress(takeBuffer()));
    }
}
