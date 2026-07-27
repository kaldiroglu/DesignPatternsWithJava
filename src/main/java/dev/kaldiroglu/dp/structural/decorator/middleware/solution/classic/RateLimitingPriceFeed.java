package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.RateLimitExceededException;

import java.time.Duration;
import java.time.Instant;

/**
 * Refuses to make more than a fixed number of calls per time window.
 * <p>
 * Contracts with suppliers really do say "1000 calls an hour", and exceeding the quota
 * costs money or gets the account throttled. The interesting question is not how to count
 * — it is <em>what</em> to count, and that is decided entirely by where this decorator
 * sits relative to the cache. See {@code OrderingTest} for both answers.
 */
public final class RateLimitingPriceFeed extends PriceFeedDecorator {

    private final Clock clock;
    private final int limit;
    private final Duration window;
    private Instant windowStartedAt;
    private int callsInWindow;

    public RateLimitingPriceFeed(PriceFeed inner, Clock clock, int limit, Duration window) {
        super(inner);
        this.clock = clock;
        this.limit = limit;
        this.window = window;
        this.windowStartedAt = clock.now();
    }

    @Override
    public Quote quoteFor(String sku) {
        if (Duration.between(windowStartedAt, clock.now()).compareTo(window) >= 0) {
            windowStartedAt = clock.now();
            callsInWindow = 0;
        }
        if (callsInWindow >= limit) {
            throw new RateLimitExceededException(limit);
        }
        callsInWindow++;
        return inner().quoteFor(sku);
    }

    public int callsInWindow() {
        return callsInWindow;
    }
}
