package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

import java.time.Duration;

/** Does a cache hit spend quota? Only if the limiter sits outside the cache. */
public final class RateLimitPlacementMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private RateLimitPlacementMain() {
    }

    public static void main(String[] args) {
        Console.section("order matters again: does a cache hit spend quota?");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);

        RateLimitingPriceFeed limiterOutside = new RateLimitingPriceFeed(
                new CachingPriceFeed(supplier, clock, TTL), clock, 10, Duration.ofHours(1));
        limiterOutside.quoteFor(SKU);
        limiterOutside.quoteFor(SKU);
        limiterOutside.quoteFor(SKU);

        ManualClock clock2 = Clock.manual();
        SimulatedRemotePriceFeed supplier2 = SimulatedRemotePriceFeed.withDefaults(clock2);
        RateLimitingPriceFeed limiterInside =
                new RateLimitingPriceFeed(supplier2, clock2, 10, Duration.ofHours(1));
        PriceFeed quotaSpentOnMisses = new CachingPriceFeed(limiterInside, clock2, TTL);
        quotaSpentOnMisses.quoteFor(SKU);
        quotaSpentOnMisses.quoteFor(SKU);
        quotaSpentOnMisses.quoteFor(SKU);

        System.out.println("  RateLimit(Cache(feed)) : " + limiterOutside.callsInWindow()
                + " of the quota used for 3 requests — cache hits count against it");
        System.out.println("  Cache(RateLimit(feed)) : " + limiterInside.callsInWindow()
                + " of the quota used for 3 requests — only real calls count");
        System.out.println("  The supplier's contract limits calls to the supplier, so the second is right.");
    }
}
