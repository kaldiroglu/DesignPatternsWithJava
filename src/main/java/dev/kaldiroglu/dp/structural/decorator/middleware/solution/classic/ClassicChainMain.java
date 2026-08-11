package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

import java.time.Duration;

/** The same behavior as the three naive designs, assembled from five decorators. */
public final class ClassicChainMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private ClassicChainMain() {
    }

    public static void main(String[] args) {
        Console.section("the same behavior, assembled from five independent decorators");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();
        Metrics metrics = new Metrics();

        PriceFeed feed =
                new TimingPriceFeed(
                        new LoggingPriceFeed(
                                new CachingPriceFeed(
                                        new RetryingPriceFeed(supplier, 3),
                                        clock, TTL),
                                log, "orders"),
                        clock, metrics);

        supplier.failNext(1);
        feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        System.out.println("  supplier calls : " + supplier.callCount() + "  (1 retried failure, then a cache hit)");
        System.out.println("  log lines      : " + log.size());
        System.out.println("  timed calls    : " + metrics.size() + ", slowest " + metrics.slowest().toMillis() + " ms");
        System.out.println("  classes needed : 5 decorators + 1 base, for every combination and order");
    }
}
