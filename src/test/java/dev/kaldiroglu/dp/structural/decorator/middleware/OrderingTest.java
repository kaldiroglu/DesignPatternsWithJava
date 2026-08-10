package dev.kaldiroglu.dp.structural.decorator.middleware;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.CachingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.LoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RateLimitingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RetryingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.TimingPriceFeed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * The point of this class.
 * <p>
 * A decorator chain is not a set of features, it is a <em>sequence</em>. The same
 * decorators with the same settings in a different order are a different program. Nothing
 * in the type system says so, no compiler warning appears, and the difference shows up in
 * production as a metric that means something other than what its name suggests.
 * <p>
 * The last test is as important as the others: sometimes the order genuinely does not
 * matter, and the honest answer is to check rather than to recite a rule.
 */
class OrderingTest {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    @Test
    @DisplayName("logging outside the retry hides the failure; inside, it shows every attempt")
    void loggingAroundRetry() {
        ManualClock clockA = Clock.manual();
        SimulatedRemotePriceFeed supplierA = SimulatedRemotePriceFeed.withDefaults(clockA);
        supplierA.failNext(1);
        CallLog outside = new CallLog();
        PriceFeed logOutside = new LoggingPriceFeed(new RetryingPriceFeed(supplierA, 3), outside, "orders");
        logOutside.quoteFor(SKU);

        ManualClock clockB = Clock.manual();
        SimulatedRemotePriceFeed supplierB = SimulatedRemotePriceFeed.withDefaults(clockB);
        supplierB.failNext(1);
        CallLog inside = new CallLog();
        PriceFeed logInside = new RetryingPriceFeed(new LoggingPriceFeed(supplierB, inside, "orders"), 3);
        logInside.quoteFor(SKU);

        // Logging(Retrying(feed)): "asking", "got" — one line in, one line out.
        assertEquals(2, outside.size());
        // Retrying(Logging(feed)): "asking", "failed", "asking", "got" — one pair per attempt.
        assertEquals(4, inside.size());

        // Both did exactly the same work against the supplier. Only the evidence differs,
        // and on a bad night the evidence is the only thing you have.
        assertEquals(2, supplierA.callCount());
        assertEquals(2, supplierB.callCount());
    }

    @Test
    @DisplayName("timing outside the cache measures the caller's wait; inside, the supplier's latency")
    void timingAroundCache() {
        ManualClock clockA = Clock.manual();
        SimulatedRemotePriceFeed supplierA = SimulatedRemotePriceFeed.withDefaults(clockA);
        Metrics outer = new Metrics();
        PriceFeed timingOutside = new TimingPriceFeed(
                new CachingPriceFeed(supplierA, clockA, TTL), clockA, outer);
        timingOutside.quoteFor(SKU);
        timingOutside.quoteFor(SKU);

        ManualClock clockB = Clock.manual();
        SimulatedRemotePriceFeed supplierB = SimulatedRemotePriceFeed.withDefaults(clockB);
        Metrics inner = new Metrics();
        PriceFeed timingInside = new CachingPriceFeed(
                new TimingPriceFeed(supplierB, clockB, inner), clockB, TTL);
        timingInside.quoteFor(SKU);
        timingInside.quoteFor(SKU);

        // Outside: two samples — a 200 ms miss and a 0 ms hit. Average 100 ms.
        assertEquals(2, outer.size());
        assertEquals(Duration.ofMillis(200), outer.samples().get(0).elapsed());
        assertEquals(Duration.ZERO, outer.samples().get(1).elapsed());

        // Inside: one sample — the cache hit never reached the timer at all.
        assertEquals(1, inner.size());
        assertEquals(Duration.ofMillis(200), inner.samples().getFirst().elapsed());

        // Both are legitimate. A dashboard labeled "price feed latency" that silently
        // switched from one to the other would show a 50% improvement and mean nothing.
        assertNotEquals(outer.size(), inner.size());
    }

    @Test
    @DisplayName("a rate limiter outside the cache spends quota on cache hits; inside, it does not")
    void rateLimitAroundCache() {
        ManualClock clockA = Clock.manual();
        SimulatedRemotePriceFeed supplierA = SimulatedRemotePriceFeed.withDefaults(clockA);
        RateLimitingPriceFeed limiterOutside = new RateLimitingPriceFeed(
                new CachingPriceFeed(supplierA, clockA, TTL), clockA, 10, Duration.ofHours(1));
        limiterOutside.quoteFor(SKU);
        limiterOutside.quoteFor(SKU);
        limiterOutside.quoteFor(SKU);

        ManualClock clockB = Clock.manual();
        SimulatedRemotePriceFeed supplierB = SimulatedRemotePriceFeed.withDefaults(clockB);
        RateLimitingPriceFeed limiterInside = new RateLimitingPriceFeed(
                supplierB, clockB, 10, Duration.ofHours(1));
        PriceFeed cacheOutside = new CachingPriceFeed(limiterInside, clockB, TTL);
        cacheOutside.quoteFor(SKU);
        cacheOutside.quoteFor(SKU);
        cacheOutside.quoteFor(SKU);

        assertEquals(3, limiterOutside.callsInWindow()); // three requests, three counted
        assertEquals(1, limiterInside.callsInWindow());  // three requests, one real call

        // The supplier's contract limits calls *to the supplier*, so the second chain is
        // the correct one — and the first spends quota three times faster than necessary.
        assertEquals(1, supplierA.callCount());
        assertEquals(1, supplierB.callCount());
    }

    @Test
    @DisplayName("and sometimes the order changes nothing — cache and retry, here")
    void cacheAndRetryHappenToAgree() {
        ManualClock clockA = Clock.manual();
        SimulatedRemotePriceFeed supplierA = SimulatedRemotePriceFeed.withDefaults(clockA);
        supplierA.failNext(1);
        PriceFeed cacheOutside = new CachingPriceFeed(new RetryingPriceFeed(supplierA, 3), clockA, TTL);
        cacheOutside.quoteFor(SKU);
        cacheOutside.quoteFor(SKU);

        ManualClock clockB = Clock.manual();
        SimulatedRemotePriceFeed supplierB = SimulatedRemotePriceFeed.withDefaults(clockB);
        supplierB.failNext(1);
        PriceFeed retryOutside = new RetryingPriceFeed(new CachingPriceFeed(supplierB, clockB, TTL), 3);
        retryOutside.quoteFor(SKU);
        retryOutside.quoteFor(SKU);

        // Identical, because failures are never cached: the retry that passes back through
        // the cache finds nothing stored and goes on to the supplier, exactly as it would
        // have done from the other side.
        assertEquals(2, supplierA.callCount());
        assertEquals(2, supplierB.callCount());

        // The lesson is not "order always matters". It is that order is a design decision
        // you must reason about for each pair — the pattern makes it visible and cheap to
        // change, where the naive designs made it invisible and expensive.
    }
}
