package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/** A subsystem class: puts the streams back into a container. */
public class Muxer {

    private final Pipeline pipeline;

    public Muxer(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void mux() {
        pipeline.record("mux");
    }
}
