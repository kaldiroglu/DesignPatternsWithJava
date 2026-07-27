package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

import java.time.Duration;
import java.time.Instant;

/**
 * Records how long the call took — including failures, which are usually the slow ones.
 * <p>
 * This decorator measures <em>everything inside it</em>. Put it outermost and it reports
 * what the caller actually waited for, cache hits and retries included; put it innermost
 * and it reports the supplier's own latency. Two useful numbers, one class, and the chain
 * decides which one you get.
 */
public final class TimingPriceFeed extends PriceFeedDecorator {

    private final Clock clock;
    private final Metrics metrics;

    public TimingPriceFeed(PriceFeed inner, Clock clock, Metrics metrics) {
        super(inner);
        this.clock = clock;
        this.metrics = metrics;
    }

    @Override
    public Quote quoteFor(String sku) {
        Instant startedAt = clock.now();
        try {
            return inner().quoteFor(sku);
        } finally {
            metrics.record(sku, Duration.between(startedAt, clock.now()));
        }
    }
}
