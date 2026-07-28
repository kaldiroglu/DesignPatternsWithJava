package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/** A subsystem class: turns the audio stream into samples. */
public class AudioDecoder {

    private final Pipeline pipeline;

    public AudioDecoder(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void decodeAudio() {
        pipeline.record("decodeAudio");
    }
}
