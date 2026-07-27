package dev.kaldiroglu.dp.structural.bridge.hw.routeplanner;

import java.util.List;

/** One candidate journey, already measured. */
public record Route(List<String> stops, int seconds, int tollMinor, boolean stepFree) {

    public String describe() {
        return String.join(" > ", stops)
                + "  [" + seconds + "s, " + tollMinor + " toll, "
                + (stepFree ? "step-free" : "steps") + "]";
    }
}
