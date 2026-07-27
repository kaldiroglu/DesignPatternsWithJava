package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Stands in for the supplier's remote service.
 * <p>
 * It is the measuring instrument of this whole example. It counts the calls it receives,
 * so a design that caches can be shown to make fewer calls and a design that retries can
 * be shown to make more — as numbers, not adjectives.
 * <p>
 * Failures are <em>scripted</em>: {@link #failNext(int)} queues up a number of outages
 * that the next calls will hit. Nothing is random, so every test and every demo produces
 * the same output every time.
 */
public final class SimulatedRemotePriceFeed implements PriceFeed {

    private final Map<String, String> catalog = new HashMap<>();
    private final ManualClock clock;
    private final Duration latency;
    private final Deque<PriceFeedException> scriptedFailures = new ArrayDeque<>();
    private int callCount;

    public SimulatedRemotePriceFeed(ManualClock clock, Duration latency) {
        this.clock = clock;
        this.latency = latency;
        catalog.put("SKU-100", "19.90");
        catalog.put("SKU-200", "249.00");
        catalog.put("SKU-300", "7.45");
    }

    /** A feed with a 200 ms round trip, which is realistic and inconvenient. */
    public static SimulatedRemotePriceFeed withDefaults(ManualClock clock) {
        return new SimulatedRemotePriceFeed(clock, Duration.ofMillis(200));
    }

    /** Queues {@code count} outages for the next calls. */
    public SimulatedRemotePriceFeed failNext(int count) {
        for (int i = 0; i < count; i++) {
            scriptedFailures.add(new FeedUnavailableException("supplier did not answer"));
        }
        return this;
    }

    @Override
    public Quote quoteFor(String sku) {
        callCount++;
        clock.advance(latency); // a remote call costs time, and the clock says so

        PriceFeedException scripted = scriptedFailures.poll();
        if (scripted != null) {
            throw scripted;
        }

        String amount = catalog.get(sku);
        if (amount == null) {
            throw new UnknownSkuException(sku);
        }
        return Quote.of(sku, amount);
    }

    /** How many times the supplier was actually called. */
    public int callCount() {
        return callCount;
    }

    public void resetCallCount() {
        callCount = 0;
    }
}
