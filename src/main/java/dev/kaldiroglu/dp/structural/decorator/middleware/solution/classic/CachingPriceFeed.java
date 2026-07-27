package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Answers from memory while an entry is still fresh.
 * <p>
 * This is the decorator that sometimes does <em>not</em> forward at all — and that is what
 * makes its position in the chain matter more than any other. Everything inside it is
 * skipped on a hit: the supplier, obviously, but also any retrying, rate limiting or
 * timing that happens to sit further in.
 */
public final class CachingPriceFeed extends PriceFeedDecorator {

    private record Entry(Quote quote, Instant storedAt) {
    }

    private final Clock clock;
    private final Duration ttl;
    private final Map<String, Entry> entries = new HashMap<>();
    private int hits;
    private int misses;

    public CachingPriceFeed(PriceFeed inner, Clock clock, Duration ttl) {
        super(inner);
        this.clock = clock;
        this.ttl = ttl;
    }

    @Override
    public Quote quoteFor(String sku) {
        Entry entry = entries.get(sku);
        if (entry != null && Duration.between(entry.storedAt(), clock.now()).compareTo(ttl) < 0) {
            hits++;
            return entry.quote();
        }

        misses++;
        Quote quote = inner().quoteFor(sku);
        entries.put(sku, new Entry(quote, clock.now()));
        return quote;
        // A failure is deliberately not cached: the next caller should get a fresh attempt.
    }

    public int hits() {
        return hits;
    }

    public int misses() {
        return misses;
    }
}
