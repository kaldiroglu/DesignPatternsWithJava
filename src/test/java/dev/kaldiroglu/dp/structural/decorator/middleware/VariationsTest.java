package dev.kaldiroglu.dp.structural.decorator.middleware;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.CachingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.LoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RetryingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.TimingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.fluent.PriceFeedPipeline;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.functional.PriceFeedMiddleware;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The two variations produce the same chain as the classic one. They are ways of writing
 * the solution, not different patterns — which is exactly the claim these tests check.
 */
class VariationsTest {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    /** One scenario, run against whatever chain is handed in. */
    private record Result(int supplierCalls, int logLines, int samples, String amount) {
    }

    private Result run(java.util.function.BiFunction<SimulatedRemotePriceFeed, Fixtures, PriceFeed> assemble) {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        Fixtures fixtures = new Fixtures(clock, new CallLog(), new Metrics());

        PriceFeed feed = assemble.apply(supplier, fixtures);
        supplier.failNext(1);
        Quote first = feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        return new Result(supplier.callCount(), fixtures.log().size(),
                fixtures.metrics().size(), first.amount().toString());
    }

    private record Fixtures(ManualClock clock, CallLog log, Metrics metrics) {
    }

    @Test
    @DisplayName("classic, functional and fluent build behaviorally identical chains")
    void allThreeAgree() {
        Result classic = run((supplier, f) ->
                new TimingPriceFeed(
                        new LoggingPriceFeed(
                                new CachingPriceFeed(
                                        new RetryingPriceFeed(supplier, 3),
                                        f.clock(), TTL),
                                f.log(), "orders"),
                        f.clock(), f.metrics()));

        Result functional = run((supplier, f) -> PriceFeedMiddleware.apply(supplier,
                PriceFeedMiddleware.timing(f.clock(), f.metrics()),
                PriceFeedMiddleware.logging(f.log(), "orders"),
                PriceFeedMiddleware.caching(f.clock(), TTL),
                PriceFeedMiddleware.retrying(3)));

        Result fluent = run((supplier, f) -> PriceFeedPipeline.around(supplier)
                .withTiming(f.clock(), f.metrics())
                .withLogging(f.log(), "orders")
                .withCache(f.clock(), TTL)
                .withRetry(3)
                .build());

        assertEquals(classic, functional);
        assertEquals(classic, fluent);

        // Two supplier calls (one retried failure, then a cache hit); four log lines,
        // because logging sits outside the cache and so speaks for the hit as well; two
        // timing samples, because timing is outermost and sees both requests.
        assertEquals(new Result(2, 4, 2, "249.00"), classic);
    }

    @Test
    @DisplayName("functional: the first middleware in the list is the outermost layer")
    void functionalOrderIsFirstIsOutermost() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        supplier.failNext(1);
        CallLog log = new CallLog();

        // logging listed first => logging is outermost => one pair of lines per request
        PriceFeed feed = PriceFeedMiddleware.apply(supplier,
                PriceFeedMiddleware.logging(log, "orders"),
                PriceFeedMiddleware.retrying(3));
        feed.quoteFor(SKU);
        assertEquals(2, log.size());

        // reversed => logging is innermost => one pair of lines per attempt
        ManualClock clock2 = Clock.manual();
        SimulatedRemotePriceFeed supplier2 = SimulatedRemotePriceFeed.withDefaults(clock2);
        supplier2.failNext(1);
        CallLog log2 = new CallLog();
        PriceFeed reversed = PriceFeedMiddleware.apply(supplier2,
                PriceFeedMiddleware.retrying(3),
                PriceFeedMiddleware.logging(log2, "orders"));
        reversed.quoteFor(SKU);
        assertEquals(4, log2.size());
    }

    @Test
    @DisplayName("functional: a decorator can be an anonymous lambda, with no type of its own")
    void aDecoratorCanBeALambda() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);

        // PriceFeed has one method, so this lambda IS a PriceFeed — and wrapping one in
        // another is all the solution has ever asked for.
        PriceFeed uppercasing = sku -> supplier.quoteFor(sku.toUpperCase());

        assertEquals("249.00", uppercasing.quoteFor("sku-200").amount().toString());
        assertTrue(uppercasing instanceof PriceFeed);
    }

    @Test
    @DisplayName("fluent: the builder reads top-down and produces the hand-written chain")
    void fluentMatchesHandWritten() {
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();

        PriceFeed built = PriceFeedPipeline.around(supplier)
                .withLogging(log, "orders")  // first listed, so outermost
                .withRetry(3)
                .build();

        assertTrue(built instanceof LoggingPriceFeed); // the outermost object is the first one named

        supplier.failNext(1);
        built.quoteFor(SKU);
        assertEquals(2, log.size()); // logging outside the retry, as written
    }
}
