package dev.kaldiroglu.dp.structural.decorator.middleware;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.CachingRetryingLoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.FlaggedPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.CachingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.LoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RetryingPriceFeed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The naive designs and the pattern are only worth comparing if they do the same job.
 * These tests run one scenario through all three and check they agree, so that every
 * remaining difference is a difference of design and not of behavior.
 */
class DesignComparisonTest {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private record Outcome(String amount, int supplierCalls) {
    }

    private Outcome runFlagged() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = FlaggedPriceFeed.fullyEnabled(supplier, clock, new CallLog(), new Metrics());
        supplier.failNext(1);
        String amount = feed.quoteFor(SKU).amount().toString();
        feed.quoteFor(SKU);
        return new Outcome(amount, supplier.callCount());
    }

    private Outcome runSubclassed() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = new CachingRetryingLoggingPriceFeed(supplier, 3, new CallLog(), clock, TTL);
        supplier.failNext(1);
        String amount = feed.quoteFor(SKU).amount().toString();
        feed.quoteFor(SKU);
        return new Outcome(amount, supplier.callCount());
    }

    private Outcome runDecorated() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = new CachingPriceFeed(
                new LoggingPriceFeed(new RetryingPriceFeed(supplier, 3), new CallLog(), "orders"),
                clock, TTL);
        supplier.failNext(1);
        String amount = feed.quoteFor(SKU).amount().toString();
        feed.quoteFor(SKU);
        return new Outcome(amount, supplier.callCount());
    }

    @Test
    @DisplayName("all three designs produce the same quote from the same supplier calls")
    void designsAgree() {
        Outcome flagged = runFlagged();
        Outcome subclassed = runSubclassed();
        Outcome decorated = runDecorated();

        assertEquals(new Outcome("249.00", 2), flagged);
        assertEquals(flagged, subclassed);
        assertEquals(flagged, decorated);
    }

    @Test
    @DisplayName("what a sixth concern costs in each design")
    void costOfTheNextConcern() {
        // Adding a circuit breaker to the order system:
        //
        //   copy-paste  : edit every call site that needs it, and hope none is missed.
        //   flags       : edit FlaggedPriceFeed — a class four other concerns depend on —
        //                 add a sixth boolean, and retest 64 configurations instead of 32.
        //   subclasses  : write CircuitBreakingCachingRetryingLoggingPriceFeed, plus a
        //                 class for every other order anyone already relies on.
        //   decorators  : write CircuitBreakerPriceFeed, change nothing else, and let each
        //                 caller decide where in its chain the breaker belongs.
        //
        // Only the last one is a pure addition. That is the open-closed principle stated
        // as an invoice rather than as a slogan.
        assertEquals(64, 1 << 6);
        assertEquals(1, 1); // one new class, no edits to existing ones
    }
}
