package dev.kaldiroglu.dp.structural.bridge.notifications.domain;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * The wire. Every raw send that any design makes goes through here, so that all the
 * designs in this project are measured on the same instrument.
 * <p>
 * Failures are <em>scripted</em>: {@link #failNext(int)} queues up outages that the next
 * sends will hit. Nothing is random, so every test and every demo produces the same
 * output every time.
 */
public final class TransportLog {

    /** One raw send, as the vendor SDK saw it. */
    public record Sent(String channel, String address, String body) {
    }

    private final List<Sent> sends = new ArrayList<>();
    private final Deque<Boolean> scriptedFailures = new ArrayDeque<>();
    private int connectionsOpened;

    public void failNext(int count) {
        for (int i = 0; i < count; i++) {
            scriptedFailures.add(Boolean.TRUE);
        }
    }

    /** Called by the vendor SDKs below. Returns false when a scripted outage is due. */
    boolean record(String channel, String address, String body) {
        sends.add(new Sent(channel, address, body));
        return scriptedFailures.poll() == null;
    }

    void openConnection() {
        connectionsOpened++;
    }

    public List<Sent> sends() {
        return List.copyOf(sends);
    }

    public int sendCount() {
        return sends.size();
    }

    public long sendCount(String channel) {
        return sends.stream().filter(s -> s.channel().equals(channel)).count();
    }

    /** How many times a transport had to be opened. Matters for the shared-implementor variation. */
    public int connectionsOpened() {
        return connectionsOpened;
    }

    public void reset() {
        sends.clear();
        scriptedFailures.clear();
        connectionsOpened = 0;
    }
}
