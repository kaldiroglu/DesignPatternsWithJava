package dev.kaldiroglu.dp.structural.decorator.middleware.solution.functional;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

import java.time.Duration;

/** Decorators as functions: no class per concern, because PriceFeed has one method. */
public final class FunctionalMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private FunctionalMain() {
    }

    public static void main(String[] args) {
        Console.section("decorators as functions: no class per concern");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();
        Metrics metrics = new Metrics();

        PriceFeed feed = PriceFeedMiddleware.apply(supplier,
                PriceFeedMiddleware.timing(clock, metrics),   // outermost
                PriceFeedMiddleware.logging(log, "orders"),
                PriceFeedMiddleware.caching(clock, TTL),
                PriceFeedMiddleware.retrying(3));             // innermost

        supplier.failNext(1);
        feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        System.out.println("  supplier calls : " + supplier.callCount() + "  (identical to the classic chain)");
        System.out.println("  written as     : four lambdas, listed outermost first");

        // A one-off concern does not even need a factory: a decorator is a lambda.
        PriceFeed withUppercaseSku = sku -> feed.quoteFor(sku.toUpperCase());
        System.out.println("  ad-hoc decorator: " + withUppercaseSku.quoteFor("sku-200"));
    }
}
