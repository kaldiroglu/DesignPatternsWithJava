package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/** A subsystem class: turns the video stream into frames. */
public class VideoDecoder {

    private final Pipeline pipeline;

    public VideoDecoder(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void decodeVideo() {
        pipeline.record("decodeVideo");
    }
}
