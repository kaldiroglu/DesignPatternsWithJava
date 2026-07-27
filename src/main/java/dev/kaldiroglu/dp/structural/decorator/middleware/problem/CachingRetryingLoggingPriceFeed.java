package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Caching, retrying and logging, in one class whose name lists its contents.
 * <p>
 * By now the design has told on itself. The name has to spell out both the concerns and
 * their order, because both are frozen into the type. And the cache code here is the
 * first copy of a cache that will be copied again the first time somebody wants caching
 * without retrying.
 *
 * <h2>The arithmetic</h2>
 * With {@code n} concerns whose order matters, covering every useful combination takes
 * the sum over k = 1..n of {@code n!/(n-k)!} classes:
 * <pre>
 *   n = 2   4 classes      n = 4    64 classes
 *   n = 3  15 classes      n = 5   325 classes
 * </pre>
 * This example has five concerns. Nobody writes 325 classes, of course — what happens
 * instead is that teams write the six or seven combinations they need today, and every
 * new requirement is a new class copied from an old one.
 */
public class CachingRetryingLoggingPriceFeed extends RetryingLoggingPriceFeed {

    private final Clock clock;
    private final Duration ttl;
    private final Map<String, Quote> cache = new HashMap<>();
    private final Map<String, Instant> cachedAt = new HashMap<>();

    public CachingRetryingLoggingPriceFeed(PriceFeed supplier, int maxAttempts, CallLog log,
                                           Clock clock, Duration ttl) {
        super(supplier, maxAttempts, log);
        this.clock = clock;
        this.ttl = ttl;
    }

    @Override
    public Quote quoteFor(String sku) {
        Instant at = cachedAt.get(sku);
        if (at != null && Duration.between(at, clock.now()).compareTo(ttl) < 0) {
            log().record("cache hit for " + sku);
            return cache.get(sku);
        }

        Quote quote = super.quoteFor(sku);
        cache.put(sku, quote);
        cachedAt.put(sku, clock.now());
        return quote;
    }
}
