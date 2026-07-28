package dev.kaldiroglu.dp.structural.proxy.hw.remote;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Stands in for the network between the proxy and the warehouse.
 * <p>
 * Failures are <strong>scripted</strong> rather than random, so every run and every test
 * produces the same output. Latency is counted, not slept.
 */
public class Link {

    private final Deque<String> scriptedFailures = new ArrayDeque<>();
    private final long latencyMillis;
    private long elapsedMillis;
    private int roundTrips;

    public Link(long latencyMillis) {
        this.latencyMillis = latencyMillis;
    }

    public Link failNext(int times) {
        for (int i = 0; i < times; i++) {
            scriptedFailures.add("the warehouse did not answer");
        }
        return this;
    }

    /** One round trip. Costs time, and may not arrive. */
    void cross() {
        roundTrips++;
        elapsedMillis += latencyMillis;
        String failure = scriptedFailures.poll();
        if (failure != null) {
            throw new RemoteCallFailedException(failure);
        }
    }

    public int roundTrips() {
        return roundTrips;
    }

    public long elapsedMillis() {
        return elapsedMillis;
    }
}
