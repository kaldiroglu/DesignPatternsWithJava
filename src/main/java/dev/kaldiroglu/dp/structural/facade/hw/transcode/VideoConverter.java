package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/**
 * The Facade — and here the knowledge being hidden is the <strong>order</strong>.
 * <p>
 * Six subsystem classes, each simple on its own. The difficulty is that they must be called
 * in one particular sequence: demux before decoding, decode before scaling, scale before
 * encoding, encode before muxing. Get it wrong and you do not get an error — you get a file
 * that is subtly wrong, or nothing at all.
 * <p>
 * That is a kind of complexity a client cannot discover from the subsystem's own interfaces,
 * and it is the most valuable thing a facade can absorb. Compare
 * {@code facade.hw.checkout}, where the facade owns failure handling, and
 * {@code facade.hw.reporting}, where it owns a protocol.
 */
public class VideoConverter {

    private final Demuxer demuxer;
    private final AudioDecoder audio;
    private final VideoDecoder video;
    private final Scaler scaler;
    private final Encoder encoder;
    private final Muxer muxer;

    public VideoConverter(Pipeline pipeline) {
        this.demuxer = new Demuxer(pipeline);
        this.audio = new AudioDecoder(pipeline);
        this.video = new VideoDecoder(pipeline);
        this.scaler = new Scaler(pipeline);
        this.encoder = new Encoder(pipeline);
        this.muxer = new Muxer(pipeline);
    }

    /** One call. The sequence below is the whole point of the class. */
    public void convert(String source, String target) {
        demuxer.demux();
        audio.decodeAudio();
        video.decodeVideo();
        scaler.scale();
        encoder.encode();
        muxer.mux();
    }
}
