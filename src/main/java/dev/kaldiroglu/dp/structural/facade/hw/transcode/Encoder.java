package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/** A subsystem class: compresses frames and samples again. */
public class Encoder {

    private final Pipeline pipeline;

    public Encoder(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void encode() {
        pipeline.record("encode");
    }
}
