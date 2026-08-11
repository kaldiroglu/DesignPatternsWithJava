package dev.kaldiroglu.dp.structural.decorator.middleware.solution.fluent;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeedException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

import java.time.Duration;

/** The same chain, read in the order a request travels. */
public final class FluentMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private FluentMain() {
    }

    public static void main(String[] args) {
        Console.section("the same chain, read in the order a request travels");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();
        Metrics metrics = new Metrics();

        PriceFeed feed = PriceFeedPipeline.around(supplier)
                .withTiming(clock, metrics)
                .withLogging(log, "orders")
                .withCache(clock, TTL)
                .withRetry(3)
                .build();

        supplier.failNext(1);
        feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        System.out.println("  supplier calls : " + supplier.callCount() + "  (identical again)");
        System.out.println("  reads top-down instead of inside-out; the behavior is unchanged");

        try {
            feed.quoteFor("SKU-NOPE");
        } catch (PriceFeedException e) {
            System.out.println("  unknown sku    : " + e.getMessage() + " — not retried, because it is not retryable");
        }
    }
}
