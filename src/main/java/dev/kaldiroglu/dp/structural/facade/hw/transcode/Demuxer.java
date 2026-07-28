package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/** A subsystem class: splits the container into streams. */
public class Demuxer {

    private final Pipeline pipeline;

    public Demuxer(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void demux() {
        pipeline.record("demux");
    }
}
