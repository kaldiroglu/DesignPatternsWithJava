package dev.kaldiroglu.dp.structural.facade.hw.transcode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Here the knowledge the facade hides is the order of the calls. */
class VideoConverterTest {

    @Test
    @DisplayName("one call becomes six, in the only order that works")
    void theOrderIsTheKnowledge() {
        Pipeline pipeline = new Pipeline();
        new VideoConverter(pipeline).convert("in.mov", "out.mp4");

        assertEquals(List.of("demux", "decodeAudio", "decodeVideo", "scale", "encode", "mux"),
                pipeline.steps());
    }

    @Test
    @DisplayName("demuxing comes first and muxing last — everything else depends on it")
    void theInvariants() {
        Pipeline pipeline = new Pipeline();
        new VideoConverter(pipeline).convert("in.mov", "out.mp4");
        List<String> steps = pipeline.steps();

        assertEquals("demux", steps.getFirst());
        assertEquals("mux", steps.getLast());
        assertTrue(steps.indexOf("decodeVideo") < steps.indexOf("scale"), "decode before scale");
        assertTrue(steps.indexOf("scale") < steps.indexOf("encode"), "scale before encode");
    }

    @Test
    @DisplayName("the caller names the facade and nothing else")
    void oneDependency() {
        var method = java.util.Arrays.stream(VideoConverter.class.getDeclaredMethods())
                .filter(m -> m.getName().equals("convert")).findFirst().orElseThrow();

        for (Class<?> p : method.getParameterTypes()) {
            assertEquals(String.class, p, "the signature is strings only");
        }
    }
}
