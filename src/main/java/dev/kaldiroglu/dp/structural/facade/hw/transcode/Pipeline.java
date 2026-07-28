package dev.kaldiroglu.dp.structural.facade.hw.transcode;

import java.util.ArrayList;
import java.util.List;

/** Records what the subsystem was asked to do, in order, so a test can check the order. */
public final class Pipeline {

    private final List<String> steps = new ArrayList<>();

    void record(String step) {
        steps.add(step);
    }

    public List<String> steps() {
        return List.copyOf(steps);
    }
}
