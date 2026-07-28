package dev.kaldiroglu.dp.structural.flyweight;

import dev.kaldiroglu.dp.structural.flyweight.quote.solution.FeedHandler;
import dev.kaldiroglu.dp.structural.flyweight.quote.solution.Instrument;
import dev.kaldiroglu.dp.structural.flyweight.quote.solution.InstrumentRegistry;
import dev.kaldiroglu.dp.structural.flyweight.quote.solution.Quote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * A market data feed is the case where Flyweight pays for itself in production and looks
 * pointless in a demo — because a demo uses string literals, which the compiler has already
 * interned. These tests decode fields the way a real handler does, so the counts are the
 * counts a real handler would see.
 */
class QuoteFlyweightTest {

    private static final int TICKS = 10_000;

    private static InstrumentRegistry registryWithApple() {
        InstrumentRegistry registry = new InstrumentRegistry();
        registry.define("AAPL", "NASDAQ", "USD", "US0378331005", "Technology", "Apple Inc.",
                new BigDecimal("0.01"), 100, LocalTime.of(9, 30), LocalTime.of(16, 0));
        return registry;
    }

    // ------------------------------------------------------------------ the problem

    @Test
    @DisplayName("string literals hide the problem: the compiler already interned them")
    void literalsMakeTheProblemInvisible() {
        String one = "AAPL";
        String two = "AAPL";

        assertSame(one, two, "which is why a toy example shows no saving to make");
    }

    @Test
    @DisplayName("decoded off the wire, every tick allocates its own symbol")
    void decodedFieldsAreDistinctObjects() {
        dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.reset();

        String first = dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("AAPL");
        String second = dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("AAPL");

        assertEquals(first, second, "equal");
        assertNotSame(first, second, "and not the same object — this is the real situation");
        assertEquals(2, dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decodeCount());
    }

    @Test
    @DisplayName("the problem retains one metadata object per tick")
    void problemRetainsMetadataPerTick() {
        var feed = new dev.kaldiroglu.dp.structural.flyweight.quote.problem.FeedHandler();

        for (int i = 0; i < TICKS; i++) {
            feed.onTick(
                    dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("AAPL"),
                    dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("NASDAQ"),
                    dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("USD"),
                    dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("US0378331005"),
                    dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("Technology"),
                    dev.kaldiroglu.dp.structural.flyweight.quote.problem.Wire.decode("Apple Inc."),
                    new BigDecimal("0.01"), 100,
                    new BigDecimal("190.00"), new BigDecimal("190.05"), new BigDecimal("190.02"), 1);
        }

        Map<String, Boolean> distinct = new IdentityHashMap<>();
        for (var q : feed.quotes()) {
            distinct.put(q.symbol(), Boolean.TRUE);
        }

        assertEquals(TICKS, feed.total());
        assertEquals(TICKS, distinct.size(), "one instrument, ten thousand symbol objects");
    }

    // ------------------------------------------------------------------ the solution

    @Test
    @DisplayName("the solution retains one metadata object, however many ticks arrive")
    void solutionRetainsOneInstrument() {
        InstrumentRegistry registry = registryWithApple();
        FeedHandler feed = new FeedHandler(registry);
        Instrument aapl = registry.get("AAPL", "NASDAQ");

        for (int i = 0; i < TICKS; i++) {
            feed.onTick(aapl, new BigDecimal("190.00"), new BigDecimal("190.05"),
                    new BigDecimal("190.02"), 1);
        }

        Map<String, Boolean> distinct = new IdentityHashMap<>();
        for (Quote q : feed.quotes()) {
            distinct.put(q.instrument().symbol(), Boolean.TRUE);
        }

        assertEquals(TICKS, feed.total(), "the same ten thousand events");
        assertEquals(1, distinct.size(), "and one symbol object");
        assertEquals(1, feed.distinctInstruments());
    }

    @Test
    @DisplayName("the per-event object carries six fields instead of thirteen")
    void theEventShrank() {
        long problemFields = java.util.Arrays.stream(
                        dev.kaldiroglu.dp.structural.flyweight.quote.problem.Quote.class
                                .getDeclaredFields())
                .filter(f -> !f.isSynthetic()).count();
        long solutionFields = java.util.Arrays.stream(Quote.class.getRecordComponents()).count();

        assertEquals(13, problemFields);
        assertEquals(6, solutionFields);
    }

    @Test
    @DisplayName("defining the same instrument twice returns the same object")
    void definingTwiceIsIdempotent() {
        InstrumentRegistry registry = registryWithApple();

        Instrument again = registry.define("AAPL", "NASDAQ", "USD", "US0378331005", "Technology",
                "Apple Inc.", new BigDecimal("0.01"), 100, LocalTime.of(9, 30), LocalTime.of(16, 0));

        assertSame(registry.get("AAPL", "NASDAQ"), again);
        assertEquals(1, registry.size());
    }

    @Test
    @DisplayName("a conflicting redefinition is refused rather than silently dropped")
    void conflictingRedefinitionThrows() {
        InstrumentRegistry registry = registryWithApple();

        // The earlier version keyed on symbol@exchange and quietly returned the first
        // instrument, so a corrected sector or tick size vanished with nothing in the log.
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () ->
                registry.define("AAPL", "NASDAQ", "USD", "US0378331005", "Consumer Electronics",
                        "Apple Inc.", new BigDecimal("0.01"), 100,
                        LocalTime.of(9, 30), LocalTime.of(16, 0)));

        assertTrue(thrown.getMessage().contains("AAPL@NASDAQ"));
        assertEquals("Technology", registry.get("AAPL", "NASDAQ").sector(),
                "and the holders' instrument did not change under them");
    }

    @Test
    @DisplayName("two threads racing on the first tick still end up with one instrument")
    void theRegistryIsThreadSafe() throws Exception {
        InstrumentRegistry registry = new InstrumentRegistry();
        int threads = 8;
        CountDownLatch start = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            for (int i = 0; i < threads; i++) {
                pool.submit(() -> {
                    try {
                        start.await();
                        registry.define("AAPL", "NASDAQ", "USD", "US0378331005", "Technology",
                                "Apple Inc.", new BigDecimal("0.01"), 100,
                                LocalTime.of(9, 30), LocalTime.of(16, 0));
                    } catch (Throwable t) {
                        failure.compareAndSet(null, t);
                    }
                });
            }
            start.countDown();
            pool.shutdown();
            assertTrue(pool.awaitTermination(10, TimeUnit.SECONDS));
        }

        assertEquals(null, failure.get(), "no thread saw a conflict");
        assertEquals(1, registry.size(), "eight threads, one instrument");
    }
}
