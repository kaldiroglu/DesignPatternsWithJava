package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/** Where timings go. */
public final class Metrics {

    /** One measured call. */
    public record Sample(String sku, Duration elapsed) {
    }

    private final List<Sample> samples = new ArrayList<>();

    public void record(String sku, Duration elapsed) {
        samples.add(new Sample(sku, elapsed));
    }

    public List<Sample> samples() {
        return List.copyOf(samples);
    }

    public int size() {
        return samples.size();
    }

    public Duration slowest() {
        return samples.stream().map(Sample::elapsed).max(Duration::compareTo).orElse(Duration.ZERO);
    }
}
