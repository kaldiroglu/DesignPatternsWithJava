package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.*;

import java.time.Duration;

/** The fixed order in {@link FlaggedPriceFeed}: the limiter counts requests, not supplier calls. */
public final class RateLimitMain {

    private static final String SKU = "SKU-200";

    private RateLimitMain() {
    }

    public static void main(String[] args) {
        Console.section("a limit of three, with three attempts per request");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = new FlaggedPriceFeed(supplier, clock,
                false, false, true, false, true,   // logging, timing, retry, caching, rate limit
                new CallLog(), new Metrics(), 3,
                Duration.ofSeconds(60), 3, Duration.ofMinutes(1));

        int counted = 0;
        for (int request = 1; request <= 3; request++) {
            supplier.failNext(2);                  // each request meets two outages
            feed.quoteFor(SKU);
            counted++;
        }
        try {
            feed.quoteFor(SKU);
        } catch (RateLimitExceededException e) {
            System.out.println("  fourth request  : refused");
        }

        System.out.println("  limiter counted : " + counted);
        System.out.println("  supplier calls  : " + supplier.callCount());
    }
}
