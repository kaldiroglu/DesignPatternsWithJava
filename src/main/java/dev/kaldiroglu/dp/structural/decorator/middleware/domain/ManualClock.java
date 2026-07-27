package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

import java.time.Duration;
import java.time.Instant;

/** A clock that stands still until {@link #advance(Duration)} is called. */
public final class ManualClock implements Clock {

    private Instant now;

    public ManualClock(Instant start) {
        this.now = start;
    }

    @Override
    public Instant now() {
        return now;
    }

    public void advance(Duration amount) {
        now = now.plus(amount);
    }
}
