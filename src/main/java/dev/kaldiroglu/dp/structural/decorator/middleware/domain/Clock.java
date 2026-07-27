package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

import java.time.Instant;

/**
 * Where the code gets "now" from.
 * <p>
 * Three of the five concerns in this example — timing, cache expiry and rate limiting —
 * are about time. Injecting the clock is what lets their tests state exact facts ("after
 * 61 seconds the entry is gone") instead of sleeping and hoping.
 */
@FunctionalInterface
public interface Clock {

    Instant now();

    /** A clock that only moves when a test tells it to. */
    static ManualClock manual() {
        return new ManualClock(Instant.parse("2026-07-27T09:00:00Z"));
    }

    /** The real thing, for the demos. */
    static Clock system() {
        return Instant::now;
    }
}
