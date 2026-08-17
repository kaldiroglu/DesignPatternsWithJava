package dev.kaldiroglu.dp.structural.decorator.middleware.solution.functional;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeedException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Variation 2: the same solution with no class per concern.
 * <p>
 * A decorator is a function from a component to a component. Say that literally — a
 * {@code PriceFeed -> PriceFeed} — and the abstract base class, the subclasses and the
 * constructors all disappear, while the structure stays exactly the same. Each factory
 * below returns a lambda that captures the next feed and returns a new one.
 * <p>
 * This works in Java because {@link PriceFeed} has a single abstract method, so
 * {@code sku -> ...} <em>is</em> a {@code PriceFeed}. It is not a different solution; it is
 * the same solution written with the language's own tools. Every web framework you have used
 * builds its middleware pipeline this way.
 *
 * <h2>When to prefer it</h2>
 * Concerns that are a few lines long and hold no state read better as lambdas. Concerns
 * with real state, invariants or their own tests — the cache below is arguably one — read
 * better as classes. Both are Decorator.
 */
@FunctionalInterface
public interface PriceFeedMiddleware extends UnaryOperator<PriceFeed> {

    /**
     * Wraps {@code base} in the given middleware so that the <b>first listed is
     * outermost</b> — the request enters at the top of the list and travels down.
     * <p>
     * The fold runs backwards for exactly that reason: the last middleware must be applied
     * first so that it ends up innermost.
     */
    static PriceFeed apply(PriceFeed base, List<PriceFeedMiddleware> middleware) {
        PriceFeed feed = base;
        for (int i = middleware.size() - 1; i >= 0; i--) {
            feed = middleware.get(i).apply(feed);
        }
        return feed;
    }

    static PriceFeed apply(PriceFeed base, PriceFeedMiddleware... middleware) {
        return apply(base, List.of(middleware));
    }

    // --- the concerns, as functions ---------------------------------------------------

    static PriceFeedMiddleware logging(CallLog log, String name) {
        return next -> sku -> {
            log.record(name + ": asking for " + sku);
            try {
                Quote quote = next.quoteFor(sku);
                log.record(name + ": got " + quote);
                return quote;
            } catch (PriceFeedException e) {
                log.record(name + ": failed for " + sku + " — " + e.getMessage());
                throw e;
            }
        };
    }

    static PriceFeedMiddleware timing(Clock clock, Metrics metrics) {
        return next -> sku -> {
            Instant startedAt = clock.now();
            try {
                return next.quoteFor(sku);
            } finally {
                metrics.record(sku, Duration.between(startedAt, clock.now()));
            }
        };
    }

    static PriceFeedMiddleware retrying(int maxAttempts) {
        return next -> sku -> {
            PriceFeedException last = null;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    return next.quoteFor(sku);
                } catch (PriceFeedException e) {
                    if (!e.isRetryable()) {
                        throw e;
                    }
                    last = e;
                }
            }
            throw last;
        };
    }

    static PriceFeedMiddleware caching(Clock clock, Duration ttl) {
        // The state lives in the closure. One map per pipeline built, which is the same
        // lifetime a CachingPriceFeed instance would have had.
        Map<String, Quote> quotes = new HashMap<>();
        Map<String, Instant> storedAt = new HashMap<>();
        return next -> sku -> {
            Instant at = storedAt.get(sku);
            if (at != null && Duration.between(at, clock.now()).compareTo(ttl) < 0) {
                return quotes.get(sku);
            }
            Quote quote = next.quoteFor(sku);
            quotes.put(sku, quote);
            storedAt.put(sku, clock.now());
            return quote;
        };
    }
}
