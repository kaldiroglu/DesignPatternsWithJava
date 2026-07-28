package dev.kaldiroglu.dp.structural.proxy.hw.pricing;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A <em>caching proxy</em> — GoF do not name this kind, and it is the one most people meet
 * first.
 * <p>
 * It implements {@link PriceService} and holds one, so it is substitutable. What it adds is
 * that a repeated question inside the time-to-live is answered <strong>without the real
 * service being called at all</strong>.
 * <p>
 * That last point is what separates a proxy from a decorator. A decorator forwards and adds
 * to the answer; this forwards <em>sometimes</em>, and the whole benefit is in the times it
 * does not.
 * <p>
 * The hard question the exercise is really about is not the cache — it is
 * <strong>invalidation</strong>. A price that changed at the supplier is still wrong here for
 * up to {@code ttlMillis}, and no amount of pattern makes that go away. It is a decision
 * about how stale the business can afford to be.
 */
public class CachingPriceProxy implements PriceService {

    private record Entry(Money price, long storedAt) { }

    private final PriceService supplier;
    private final Clock clock;
    private final long ttlMillis;
    private final Map<String, Entry> cache = new HashMap<>();

    private int hits;
    private int misses;

    public CachingPriceProxy(PriceService supplier, Clock clock, long ttlMillis) {
        this.supplier = Objects.requireNonNull(supplier);
        this.clock = Objects.requireNonNull(clock);
        this.ttlMillis = ttlMillis;
    }

    @Override
    public Money priceOf(String sku) {
        Entry entry = cache.get(sku);
        if (entry != null && clock.millis() - entry.storedAt() < ttlMillis) {
            hits++;
            return entry.price();
        }
        misses++;
        Money price = supplier.priceOf(sku);      // the only line that costs money
        cache.put(sku, new Entry(price, clock.millis()));
        return price;
    }

    /** Throws the cache away — the manual half of the invalidation answer. */
    public void invalidate(String sku) {
        cache.remove(sku);
    }

    public int hits() {
        return hits;
    }

    public int misses() {
        return misses;
    }
}
