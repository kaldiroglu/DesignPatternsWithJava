package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.FeedUnavailableException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.RateLimitExceededException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Naive design 2: one class that does everything, switched by flags.
 * <p>
 * This is the usual answer to design 1 — "the duplication is the problem, so let us put
 * the code in one place" — and it is a real improvement: the logic exists once. What it
 * does not fix is anything else.
 *
 * <h2>What the flags cost</h2>
 * <ul>
 *   <li><b>2<sup>5</sup> = 32 configurations</b> from five booleans, of which the tests
 *       will cover perhaps four. The rest are untested code paths that ship anyway.</li>
 *   <li><b>Every new concern edits this file.</b> Adding a circuit breaker means changing
 *       a class that caching, retrying and rate limiting all depend on — and re-testing all
 *       32 configurations. This is precisely the open-closed principle being violated.</li>
 *   <li><b>The order is welded in.</b> Read {@link #quoteFor(String)}: the cache is checked
 *       before the rate limiter, and retrying happens inside both. A caller who needs the
 *       other order cannot have it — not by configuration, not at all, without editing
 *       this method and breaking it for everyone else.</li>
 *   <li><b>The concerns are entangled.</b> The timing code reads {@code metrics != null},
 *       the cache writes need the clock the rate limiter also uses, and a change to one
 *       branch can silently alter another. None of them can be tested alone.</li>
 * </ul>
 */
public final class FlaggedPriceFeed implements PriceFeed {

    private final PriceFeed supplier;
    private final Clock clock;

    // --- the switches ---------------------------------------------------------------
    private final boolean loggingEnabled;
    private final boolean timingEnabled;
    private final boolean retryEnabled;
    private final boolean cachingEnabled;
    private final boolean rateLimitEnabled;

    // --- the settings each switch needs ---------------------------------------------
    private final CallLog log;
    private final Metrics metrics;
    private final int maxAttempts;
    private final Duration cacheTtl;
    private final int rateLimit;
    private final Duration rateLimitWindow;

    // --- the state each switch needs ------------------------------------------------
    private final Map<String, Quote> cache = new HashMap<>();
    private final Map<String, Instant> cachedAt = new HashMap<>();
    private Instant windowStartedAt;
    private int callsInWindow;

    public FlaggedPriceFeed(PriceFeed supplier, Clock clock,
                            boolean loggingEnabled, boolean timingEnabled, boolean retryEnabled,
                            boolean cachingEnabled, boolean rateLimitEnabled,
                            CallLog log, Metrics metrics, int maxAttempts,
                            Duration cacheTtl, int rateLimit, Duration rateLimitWindow) {
        this.supplier = supplier;
        this.clock = clock;
        this.loggingEnabled = loggingEnabled;
        this.timingEnabled = timingEnabled;
        this.retryEnabled = retryEnabled;
        this.cachingEnabled = cachingEnabled;
        this.rateLimitEnabled = rateLimitEnabled;
        this.log = log;
        this.metrics = metrics;
        this.maxAttempts = maxAttempts;
        this.cacheTtl = cacheTtl;
        this.rateLimit = rateLimit;
        this.rateLimitWindow = rateLimitWindow;
        this.windowStartedAt = clock.now();
    }

    /** Everything on, with the settings the order system happens to use. */
    public static FlaggedPriceFeed fullyEnabled(PriceFeed supplier, Clock clock, CallLog log, Metrics metrics) {
        return new FlaggedPriceFeed(supplier, clock, true, true, true, true, true,
                log, metrics, 3, Duration.ofSeconds(60), 10, Duration.ofSeconds(1));
    }

    @Override
    public Quote quoteFor(String sku) {
        Instant startedAt = clock.now();

        if (loggingEnabled) {
            log.record("asked for " + sku);
        }

        if (cachingEnabled) {
            Instant at = cachedAt.get(sku);
            if (at != null && Duration.between(at, clock.now()).compareTo(cacheTtl) < 0) {
                if (loggingEnabled) {
                    log.record("cache hit for " + sku);
                }
                if (timingEnabled) {
                    metrics.record(sku, Duration.between(startedAt, clock.now()));
                }
                return cache.get(sku);
            }
        }

        if (rateLimitEnabled) {
            if (Duration.between(windowStartedAt, clock.now()).compareTo(rateLimitWindow) >= 0) {
                windowStartedAt = clock.now();
                callsInWindow = 0;
            }
            if (callsInWindow >= rateLimit) {
                throw new RateLimitExceededException(rateLimit);
            }
            callsInWindow++;
        }

        int attempts = retryEnabled ? maxAttempts : 1;
        FeedUnavailableException last = null;
        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                Quote quote = supplier.quoteFor(sku);
                if (cachingEnabled) {
                    cache.put(sku, quote);
                    cachedAt.put(sku, clock.now());
                }
                if (timingEnabled) {
                    metrics.record(sku, Duration.between(startedAt, clock.now()));
                }
                if (loggingEnabled) {
                    log.record("got " + quote);
                }
                return quote;
            } catch (FeedUnavailableException e) {
                last = e;
                if (loggingEnabled) {
                    log.record("attempt " + attempt + " failed for " + sku);
                }
            }
        }

        if (timingEnabled) {
            metrics.record(sku, Duration.between(startedAt, clock.now()));
        }
        throw last;
    }
}
