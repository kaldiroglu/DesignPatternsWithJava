package dev.kaldiroglu.dp.structural.decorator.middleware.solution.fluent;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.CachingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.LoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RateLimitingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RetryingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.TimingPriceFeed;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Variation 3: the same decorators, assembled in reading order.
 * <p>
 * Hand-written chains are correct but they read backwards, because the outermost
 * decorator is written first and the innermost is buried deepest:
 * <pre>{@code
 * new TimingPriceFeed(new RetryingPriceFeed(new CachingPriceFeed(feed, clock, ttl), 3), clock, metrics)
 * }</pre>
 * The same chain through this builder:
 * <pre>{@code
 * PriceFeedPipeline.around(feed)
 *     .withTiming(clock, metrics)   // outermost — sees everything below it
 *     .withRetry(3)
 *     .withCache(clock, ttl)        // innermost — closest to the supplier
 *     .build();
 * }</pre>
 * <b>The first call is the outermost layer</b>, so the list reads in the direction a
 * request travels. This is the convention ASP.NET Core and Express use for exactly the
 * same reason, and it is worth stating out loud, because the opposite convention is just
 * as defensible and the two are indistinguishable until something breaks.
 * <p>
 * Nothing here is new solution machinery. The builder collects the decorators and applies
 * them in reverse; it is convenience, not design. That is the point worth making to
 * students: this variation changes how the code <em>reads</em>, not how it <em>works</em>,
 * and {@code FluentTest} proves the two produce identical chains.
 */
public final class PriceFeedPipeline {

    private final PriceFeed base;
    private final List<UnaryOperator<PriceFeed>> layers = new ArrayList<>();

    private PriceFeedPipeline(PriceFeed base) {
        this.base = base;
    }

    /** Starts a pipeline around the feed that will sit innermost. */
    public static PriceFeedPipeline around(PriceFeed base) {
        return new PriceFeedPipeline(base);
    }

    public PriceFeedPipeline withLogging(CallLog log, String name) {
        layers.add(inner -> new LoggingPriceFeed(inner, log, name));
        return this;
    }

    public PriceFeedPipeline withTiming(Clock clock, Metrics metrics) {
        layers.add(inner -> new TimingPriceFeed(inner, clock, metrics));
        return this;
    }

    public PriceFeedPipeline withRetry(int maxAttempts) {
        layers.add(inner -> new RetryingPriceFeed(inner, maxAttempts));
        return this;
    }

    public PriceFeedPipeline withCache(Clock clock, Duration ttl) {
        layers.add(inner -> new CachingPriceFeed(inner, clock, ttl));
        return this;
    }

    public PriceFeedPipeline withRateLimit(Clock clock, int limit, Duration window) {
        layers.add(inner -> new RateLimitingPriceFeed(inner, clock, limit, window));
        return this;
    }

    /** Applies the layers back to front, so the first one added ends up outermost. */
    public PriceFeed build() {
        PriceFeed feed = base;
        for (int i = layers.size() - 1; i >= 0; i--) {
            feed = layers.get(i).apply(feed);
        }
        return feed;
    }
}
