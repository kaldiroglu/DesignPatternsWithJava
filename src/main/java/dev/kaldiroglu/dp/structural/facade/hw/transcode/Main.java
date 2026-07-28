package dev.kaldiroglu.dp.structural.facade.hw.transcode;

/** Homework 3 — when the thing being hidden is the order of the calls. */
public class Main {

    public static void main(String[] args) {
        Pipeline pipeline = new Pipeline();
        new VideoConverter(pipeline).convert("holiday.mov", "holiday.mp4");

        System.out.println("one call became: " + pipeline.steps());
        System.out.println("""

                Six subsystem classes, each easy. The hard part is that they only
                work in one order, and nothing in their interfaces says so.

                That is the most valuable thing a facade can absorb: knowledge a
                client could not have discovered from the subsystem itself.""");
    }
}
