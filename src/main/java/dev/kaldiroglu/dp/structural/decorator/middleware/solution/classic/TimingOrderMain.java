package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.*;

import java.time.Duration;

/** Timing outside the cache measures the caller's wait; inside, the supplier's cost. */
public final class TimingOrderMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private TimingOrderMain() {
    }

    public static void main(String[] args) {
        Console.section("order matters: timing inside or outside the cache");

        ManualClock clockC = Clock.manual();
        SimulatedRemotePriceFeed supplierC = SimulatedRemotePriceFeed.withDefaults(clockC);
        Metrics outerMetrics = new Metrics();
        PriceFeed timingOutside = new TimingPriceFeed(
                new CachingPriceFeed(supplierC, clockC, TTL), clockC, outerMetrics);
        timingOutside.quoteFor(SKU);
        timingOutside.quoteFor(SKU);

        ManualClock clockD = Clock.manual();
        SimulatedRemotePriceFeed supplierD = SimulatedRemotePriceFeed.withDefaults(clockD);
        Metrics innerMetrics = new Metrics();
        PriceFeed timingInside = new CachingPriceFeed(
                new TimingPriceFeed(supplierD, clockD, innerMetrics), clockD, TTL);
        timingInside.quoteFor(SKU);
        timingInside.quoteFor(SKU);

        System.out.println("  Timing(Caching(feed)) : " + outerMetrics.size()
                + " samples — what the caller waited for, cache hits included");
        System.out.println("  Caching(Timing(feed)) : " + innerMetrics.size()
                + " sample  — what the supplier itself cost, hits never reach it");
        System.out.println("  Both are useful numbers. They are different numbers, and the chain picks.");
    }
}
