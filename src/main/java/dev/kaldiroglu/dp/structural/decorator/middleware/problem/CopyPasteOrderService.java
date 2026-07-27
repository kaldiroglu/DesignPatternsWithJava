package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.FeedUnavailableException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Naive design 1: write the cross-cutting code at the call site, once per call site.
 * <p>
 * This is how the problem actually starts in real code. Nobody sets out to build a mess:
 * somebody adds a retry loop where the failure happened, somebody else adds a cache where
 * the slowness hurt, and each addition is locally reasonable.
 * <p>
 * Read the two methods below and look for the differences. They were the same code once.
 */
public final class CopyPasteOrderService {

    private static final Duration CACHE_TTL = Duration.ofSeconds(60);

    private final PriceFeed feed;
    private final Clock clock;
    private final CallLog log;
    private final Map<String, Quote> cache = new HashMap<>();
    private final Map<String, Instant> cachedAt = new HashMap<>();

    public CopyPasteOrderService(PriceFeed feed, Clock clock, CallLog log) {
        this.feed = feed;
        this.clock = clock;
        this.log = log;
    }

    /** Prices an item for a new order. */
    public Quote priceForOrder(String sku) {
        Instant cachedTime = cachedAt.get(sku);
        if (cachedTime != null && Duration.between(cachedTime, clock.now()).compareTo(CACHE_TTL) < 0) {
            log.record("order: cache hit for " + sku);
            return cache.get(sku);
        }

        FeedUnavailableException last = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                log.record("order: calling supplier for " + sku + ", attempt " + attempt);
                Quote quote = feed.quoteFor(sku);
                cache.put(sku, quote);
                cachedAt.put(sku, clock.now());
                return quote;
            } catch (FeedUnavailableException e) {
                last = e;
                log.record("order: attempt " + attempt + " failed for " + sku);
            }
        }
        throw last;
    }

    /**
     * Prices an item for a reorder. Written three months later by copying the method above.
     * <p>
     * Four things drifted, and every one of them is a real bug:
     * <ol>
     *   <li>the retry count is 2 here and 3 above — nobody decided that, it just happened;</li>
     *   <li>failures are not logged, so a reorder that retries silently looks healthy;</li>
     *   <li>the quote is put in {@code cache} but no timestamp is put in {@code cachedAt},
     *       so an entry written here can never be read back — the lookup above needs the
     *       timestamp. The reorder path therefore pays the supplier for every single call
     *       while appearing, in review, to have a cache;</li>
     *   <li>the TTL check reads {@code <=} rather than {@code <}, an off-by-one nobody will find.</li>
     * </ol>
     * The deeper point is not that this code is bad. It is that <em>nothing in the design
     * stops it</em>: the concerns have no home of their own, so they live wherever they
     * were typed, and there is no single place to fix them.
     */
    public Quote priceForReorder(String sku) {
        Instant cachedTime = cachedAt.get(sku);
        if (cachedTime != null && Duration.between(cachedTime, clock.now()).compareTo(CACHE_TTL) <= 0) {
            log.record("reorder: cache hit for " + sku);
            return cache.get(sku);
        }

        FeedUnavailableException last = null;
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                log.record("reorder: calling supplier for " + sku + ", attempt " + attempt);
                Quote quote = feed.quoteFor(sku);
                cache.put(sku, quote);
                return quote;
            } catch (FeedUnavailableException e) {
                last = e;
            }
        }
        throw last;
    }
}
