package dev.kaldiroglu.dp.structural.decorator.middleware;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.RateLimitExceededException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.CachingRetryingLoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.CopyPasteOrderService;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.FlaggedPriceFeed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The naive designs work. Every test here passes, and that is the point: an argument for a
 * solution is worth nothing if the alternative was never given a fair hearing. What these
 * tests then measure is the cost.
 */
class ProblemTest {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    // ---------------------------------------------------------- design 1: copy-paste

    @Test
    @DisplayName("copy-paste: both call sites work, and have already drifted apart")
    void copyPasteDrift() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();
        CopyPasteOrderService service = new CopyPasteOrderService(supplier, clock, log);

        supplier.failNext(1);
        assertEquals("249.00", service.priceForOrder(SKU).amount().toString());
        int orderLines = log.size();

        log.clear();
        supplier.failNext(1);
        assertEquals("19.90", service.priceForReorder("SKU-100").amount().toString());
        int reorderLines = log.size();

        // Same job, same failure, different amount of evidence left behind: the reorder
        // path never logged the failed attempt, because that line was not copied.
        assertEquals(3, orderLines);
        assertEquals(2, reorderLines);
        assertNotEquals(orderLines, reorderLines);
    }

    @Test
    @DisplayName("copy-paste: the reorder cache does nothing, because the timestamp is not written")
    void copyPasteCacheNeverWorks() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CopyPasteOrderService service = new CopyPasteOrderService(supplier, clock, new CallLog());

        service.priceForReorder(SKU);
        service.priceForReorder(SKU); // one second later would do; the clock is irrelevant here

        // Two supplier calls for two identical requests, back to back. The reorder path
        // stores the quote but not its timestamp, and the lookup needs the timestamp — so
        // nothing it writes can ever be read back. The cache costs memory and returns
        // nothing, on the path that runs most often.
        assertEquals(2, supplier.callCount());

        // And it is not a caching problem that time will fix: five hours later, still a miss.
        clock.advance(Duration.ofHours(5));
        service.priceForReorder(SKU);
        assertEquals(3, supplier.callCount());

        // The order path, whose copy of the same code does write the timestamp, caches
        // correctly — which is why the fault never shows up in a test of "the cache".
        supplier.resetCallCount();
        service.priceForOrder("SKU-100");
        service.priceForOrder("SKU-100");
        assertEquals(1, supplier.callCount());
    }

    // ---------------------------------------------------------- design 2: flags

    @Test
    @DisplayName("flags: it works — one class, everything switched on")
    void flaggedFeedWorks() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = FlaggedPriceFeed.fullyEnabled(supplier, clock, new CallLog(), new Metrics());

        supplier.failNext(1);
        feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        assertEquals(2, supplier.callCount()); // one retried failure, then a cache hit
    }

    @Test
    @DisplayName("flags: turning caching off silently changes what the metrics mean")
    void flagsAreEntangled() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        Metrics withCache = new Metrics();
        Metrics withoutCache = new Metrics();

        PriceFeed cached = new FlaggedPriceFeed(supplier, clock, false, true, false, true, false,
                new CallLog(), withCache, 1, TTL, 10, Duration.ofSeconds(1));
        cached.quoteFor(SKU);
        cached.quoteFor(SKU);

        ManualClock clock2 = Clock.manual();
        SimulatedRemotePriceFeed supplier2 = SimulatedRemotePriceFeed.withDefaults(clock2);
        PriceFeed uncached = new FlaggedPriceFeed(supplier2, clock2, false, true, false, false, false,
                new CallLog(), withoutCache, 1, TTL, 10, Duration.ofSeconds(1));
        uncached.quoteFor(SKU);
        uncached.quoteFor(SKU);

        // Both recorded two samples, but they measure different things: with the cache on,
        // the second sample is a 0 ms cache hit. One boolean quietly redefined the metric,
        // and no signature changed to warn anybody.
        assertEquals(2, withCache.samples().size());
        assertEquals(2, withoutCache.samples().size());
        assertEquals(Duration.ZERO, withCache.samples().get(1).elapsed());
        assertEquals(Duration.ofMillis(200), withoutCache.samples().get(1).elapsed());
    }

    @Test
    @DisplayName("flags: five booleans are thirty-two behaviors, and nobody tests thirty-two")
    void flagsAreCombinatorial() {
        assertEquals(32, 1 << 5);
    }

    @Test
    @DisplayName("flags: the retry loop sits inside the rate limiter, so one slot buys three calls")
    void retriesAreInvisibleToTheRateLimiter() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);

        // A quota of three calls per window, and up to three attempts per call. Caching and
        // the rest are off, so nothing else can absorb or add a call and the count is exact.
        FlaggedPriceFeed feed = new FlaggedPriceFeed(
                supplier, clock,
                false, false, true, false, true,   // logging, timing, retry, caching, rate limit
                new CallLog(), new Metrics(), 3,
                TTL, 3, Duration.ofMinutes(1));    // window long enough not to roll over

        // Three requests, each of which meets two outages before it is answered.
        for (int request = 1; request <= 3; request++) {
            supplier.failNext(2);
            assertEquals("249.00", feed.quoteFor(SKU).amount().toString());
        }

        // The quota of three is now spent: the limiter counted three requests.
        assertThrows(RateLimitExceededException.class, () -> feed.quoteFor(SKU));

        // The supplier, however, was called nine times: three requests of three attempts
        // each. callsInWindow++ runs once, before the retry loop, so every attempt after
        // the first is invisible to the limiter — a limit of 3 permitted 3x the traffic it
        // exists to prevent. Were the limiter inside the loop, this would be 3.
        assertEquals(9, supplier.callCount());
    }

    @Test
    @DisplayName("flags: the constructor is the design review — thirteen parameters")
    void theConstructorIsTheDesignReview() {
        var constructor = FlaggedPriceFeed.class.getConstructors()[0];

        // Five switches and eight collaborators and settings. Every caller must have an
        // opinion about all thirteen, and the compiler will happily accept a wrong one.
        assertEquals(13, constructor.getParameterCount());
        assertEquals(5, java.util.Arrays.stream(constructor.getParameterTypes())
                .filter(type -> type == boolean.class)
                .count());
    }

    // ---------------------------------------------------------- design 3: subclasses

    @Test
    @DisplayName("subclasses: the combination class works")
    void subclassCombinationWorks() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = new CachingRetryingLoggingPriceFeed(supplier, 3, new CallLog(), clock, TTL);

        supplier.failNext(1);
        feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        assertEquals(2, supplier.callCount());
    }

    @Test
    @DisplayName("subclasses: the order is frozen into the type, so a second order is a second class")
    void subclassOrderIsFrozen() {
        // CachingRetryingLoggingPriceFeed puts caching outside retrying, and there is no
        // way to ask it for the opposite. Nothing can be passed, set or overridden — the
        // order is the shape of the inheritance chain. Getting the other order means
        // writing RetryingCachingLoggingPriceFeed and copying most of both.
        //
        // With n concerns whose order matters, full coverage is every non-empty subset in
        // every order — the sum over k of P(n, k) = n!/(n-k)!. Every row of the table on
        // the slide is asserted here.
        assertEquals(4, permutationsUpTo(2));
        assertEquals(15, permutationsUpTo(3));
        assertEquals(64, permutationsUpTo(4));
        assertEquals(325, permutationsUpTo(5));

        // And the term-by-term breakdown of the figure that gets quoted: 5 + 20 + 60 +
        // 120 + 120. If order did NOT matter it would be 2^5 - 1 = 31 instead.
        assertEquals(325, 5 + 20 + 60 + 120 + 120);
        assertEquals(31, (1 << 5) - 1);
    }

    @Test
    @DisplayName("subclasses: a final vendor class cannot be extended at all")
    void subclassingCannotReachAFinalClass() {
        // The line below does not compile, and no amount of design discipline changes that:
        //
        //     class LoggingVendorFeed extends VendorPriceFeed { }
        //     //                              ^^^^^^^^^^^^^^^ cannot inherit from final class
        //
        // Every design in the problem package is built on subclassing, so every one of them
        // stops here. See ClassicDecoratorTest#decoratesAFinalClass for what decoration does
        // with the same class.
        assertTrue(java.lang.reflect.Modifier.isFinal(
                dev.kaldiroglu.dp.structural.decorator.middleware.domain.VendorPriceFeed.class.getModifiers()));
    }

    private static int permutationsUpTo(int n) {
        int total = 0;
        for (int k = 1; k <= n; k++) {
            int p = 1;
            for (int i = 0; i < k; i++) {
                p *= (n - i);
            }
            total += p;
        }
        return total;
    }
}
