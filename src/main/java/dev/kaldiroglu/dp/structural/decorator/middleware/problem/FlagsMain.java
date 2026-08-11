package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

/** Naive design 2: one class, five boolean flags. */
public final class FlagsMain {

    private static final String SKU = "SKU-200";

    private FlagsMain() {
    }

    public static void main(String[] args) {
        Console.section("2. one class, five boolean flags");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = FlaggedPriceFeed.fullyEnabled(supplier, clock, new CallLog(), new Metrics());

        feed.quoteFor(SKU);
        feed.quoteFor(SKU);
        System.out.println("  works: " + supplier.callCount() + " supplier call for 2 requests");
        System.out.println("  but: 5 booleans = 32 configurations, and the cache/retry order");
        System.out.println("  is welded into one method that every concern has to share");
    }
}
