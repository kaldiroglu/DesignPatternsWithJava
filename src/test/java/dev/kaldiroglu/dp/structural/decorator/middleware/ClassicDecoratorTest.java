package dev.kaldiroglu.dp.structural.decorator.middleware;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.FeedUnavailableException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.RateLimitExceededException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.UnknownSkuException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.VendorPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.CachingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.LoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RateLimitingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RetryingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.TimingPriceFeed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Each decorator, on its own. Being testable alone is the practical payoff of the
 * solution: none of these tests could be written against the flagged god-class, because
 * there is no way to have only one of its concerns.
 */
class ClassicDecoratorTest {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private final ManualClock clock = Clock.manual();
    private final SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);

    @Test
    @DisplayName("logging: records the request and the result, and rethrows failures")
    void logging() {
        CallLog log = new CallLog();
        PriceFeed feed = new LoggingPriceFeed(supplier, log, "orders");

        feed.quoteFor(SKU);
        assertEquals(2, log.size());
        assertTrue(log.lines().get(0).contains("asking for SKU-200"));

        assertThrows(UnknownSkuException.class, () -> feed.quoteFor("SKU-NOPE"));
        assertTrue(log.lines().get(3).contains("failed"));
        // The failure was logged AND propagated. A decorator that swallows what it
        // observes has stopped being transparent, and its clients cannot tell.
    }

    @Test
    @DisplayName("timing: measures the call, failures included")
    void timing() {
        Metrics metrics = new Metrics();
        PriceFeed feed = new TimingPriceFeed(supplier, clock, metrics);

        feed.quoteFor(SKU);
        assertEquals(Duration.ofMillis(200), metrics.samples().getFirst().elapsed());

        assertThrows(UnknownSkuException.class, () -> feed.quoteFor("SKU-NOPE"));
        assertEquals(2, metrics.size()); // the failure was timed too — it is in a finally block
    }

    @Test
    @DisplayName("retry: tries again on a retryable failure")
    void retryOnRetryableFailure() {
        supplier.failNext(2);
        PriceFeed feed = new RetryingPriceFeed(supplier, 3);

        assertEquals("249.00", feed.quoteFor(SKU).amount().toString());
        assertEquals(3, supplier.callCount());
    }

    @Test
    @DisplayName("retry: gives up after the last attempt")
    void retryGivesUp() {
        supplier.failNext(5);
        PriceFeed feed = new RetryingPriceFeed(supplier, 3);

        assertThrows(FeedUnavailableException.class, () -> feed.quoteFor(SKU));
        assertEquals(3, supplier.callCount());
    }

    @Test
    @DisplayName("retry: does not retry what cannot succeed")
    void retryRespectsRetryability() {
        PriceFeed feed = new RetryingPriceFeed(supplier, 3);

        assertThrows(UnknownSkuException.class, () -> feed.quoteFor("SKU-NOPE"));
        assertEquals(1, supplier.callCount()); // not 3 — an unknown SKU stays unknown
    }

    @Test
    @DisplayName("cache: serves from memory until the entry expires")
    void cacheHitAndExpiry() {
        CachingPriceFeed feed = new CachingPriceFeed(supplier, clock, TTL);

        feed.quoteFor(SKU);
        feed.quoteFor(SKU);
        assertEquals(1, supplier.callCount());
        assertEquals(1, feed.hits());

        clock.advance(Duration.ofSeconds(61));
        feed.quoteFor(SKU);
        assertEquals(2, supplier.callCount());
        assertEquals(2, feed.misses());
    }

    @Test
    @DisplayName("cache: a failure is not cached, so the next caller gets a fresh attempt")
    void failuresAreNotCached() {
        supplier.failNext(1);
        PriceFeed feed = new CachingPriceFeed(supplier, clock, TTL);

        assertThrows(FeedUnavailableException.class, () -> feed.quoteFor(SKU));
        assertEquals("249.00", feed.quoteFor(SKU).amount().toString());
        assertEquals(2, supplier.callCount());
    }

    @Test
    @DisplayName("rate limit: refuses past the quota, and forgives after the window")
    void rateLimit() {
        RateLimitingPriceFeed feed = new RateLimitingPriceFeed(supplier, clock, 2, Duration.ofSeconds(1));

        feed.quoteFor(SKU);
        feed.quoteFor(SKU);
        assertThrows(RateLimitExceededException.class, () -> feed.quoteFor(SKU));
        assertEquals(2, supplier.callCount());

        clock.advance(Duration.ofSeconds(2));
        feed.quoteFor(SKU);
        assertEquals(3, supplier.callCount());
    }

    @Test
    @DisplayName("a decorated feed is still a PriceFeed, so decorators nest without limit")
    void transparency() {
        PriceFeed feed = new TimingPriceFeed(
                new LoggingPriceFeed(
                        new CachingPriceFeed(
                                new RetryingPriceFeed(supplier, 3), clock, TTL),
                        new CallLog(), "orders"),
                clock, new Metrics());

        assertTrue(feed instanceof PriceFeed);
        assertEquals("249.00", feed.quoteFor(SKU).amount().toString());
    }

    @Test
    @DisplayName("decoration works on a final class, where subclassing cannot")
    void decoratesAFinalClass() {
        VendorPriceFeed vendor = new VendorPriceFeed();
        CallLog log = new CallLog();

        PriceFeed decorated = new CachingPriceFeed(new LoggingPriceFeed(vendor, log, "vendor"), clock, TTL);
        decorated.quoteFor("SKU-999");
        decorated.quoteFor("SKU-999");

        assertEquals(1, vendor.callCount()); // logging and caching added to a class we cannot touch
        assertEquals(2, log.size());
    }

    @Test
    @DisplayName("the same chain wraps any supplier, however it gets its prices")
    void theSameChainWrapsAnySupplier() {
        // Two suppliers with nothing in common but the interface. The vendor's is final,
        // holds no catalog and answers a flat rate without leaving the process; ours reads
        // a catalog and costs 200 ms of round trip. Neither knows a decorator exists.
        VendorPriceFeed vendor = new VendorPriceFeed();
        CallLog vendorLog = new CallLog();
        PriceFeed decoratedVendor = new CachingPriceFeed(
                new LoggingPriceFeed(vendor, vendorLog, "vendor"), clock, TTL);

        CallLog remoteLog = new CallLog();
        PriceFeed decoratedRemote = new CachingPriceFeed(
                new LoggingPriceFeed(supplier, remoteLog, "remote"), clock, TTL);

        // Same two requests through each chain.
        assertEquals("42.00", decoratedVendor.quoteFor("SKU-999").amount().toString());
        assertEquals("42.00", decoratedVendor.quoteFor("SKU-999").amount().toString());
        assertEquals("249.00", decoratedRemote.quoteFor(SKU).amount().toString());
        assertEquals("249.00", decoratedRemote.quoteFor(SKU).amount().toString());

        // One supplier call each: the cache answered the second request both times.
        assertEquals(1, vendor.callCount());
        assertEquals(1, supplier.callCount());
        assertEquals(2, vendorLog.size());
        assertEquals(2, remoteLog.size());
    }

    @Test
    @DisplayName("GoF Consequence 3: the decorated feed is not the same object as the feed")
    void identityIsNotPreserved() {
        PriceFeed decorated = new RetryingPriceFeed(supplier, 3);

        assertNotSame(supplier, decorated);
        // Practical consequence: code that stores feeds in a Set, compares them with ==,
        // or casts them back to SimulatedRemotePriceFeed will not find what it expects.
        assertTrue(decorated instanceof RetryingPriceFeed);
        assertTrue(!(decorated instanceof SimulatedRemotePriceFeed));
    }

    @Test
    @DisplayName("a decorator must decorate something")
    void nullInnerIsRejected() {
        assertThrows(NullPointerException.class, () -> new RetryingPriceFeed(null, 3));
    }
}
