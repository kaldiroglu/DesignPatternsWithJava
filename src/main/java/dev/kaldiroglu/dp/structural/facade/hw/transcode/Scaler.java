package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/** A subsystem class: resizes the frames. */
public class Scaler {

    private final Pipeline pipeline;

    public Scaler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void scale() {
        pipeline.record("scale");
    }
}
