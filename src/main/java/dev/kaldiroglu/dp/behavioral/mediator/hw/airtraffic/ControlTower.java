package dev.kaldiroglu.dp.behavioral.mediator.hw.airtraffic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Homework 2: the <b>Mediator</b> of an airport. Aircraft never talk to each other; they
 * ask the tower for the runway, and the tower gives it to one aircraft at a time.
 * <p>
 * Aircraft may ask from different threads, so every method is {@code synchronized}: the
 * check "is the runway free?" and the step "give it to this aircraft" happen together.
 * A mediator that many objects share is also a place where threads meet.
 */
public final class ControlTower {

    private final Deque<Aircraft> waiting = new ArrayDeque<>();
    private final List<String> log = new ArrayList<>();
    private Aircraft onRunway;

    public synchronized void request(Aircraft aircraft) {
        if (onRunway == null) {
            grant(aircraft);
        } else {
            waiting.add(aircraft);
            log.add(aircraft.callSign() + " waits");
        }
    }

    public synchronized void runwayClear(Aircraft aircraft) {
        if (onRunway != aircraft) {
            throw new IllegalStateException(aircraft.callSign() + " is not on the runway");
        }
        log.add(aircraft.callSign() + " clears the runway");
        onRunway = null;
        if (!waiting.isEmpty()) {
            grant(waiting.remove());
        }
    }

    private void grant(Aircraft aircraft) {
        onRunway = aircraft;
        log.add(aircraft.callSign() + " may " + aircraft.intent());
    }

    public synchronized List<String> log() {
        return List.copyOf(log);
    }
}
