package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.VendorPriceFeed;

import java.time.Duration;

/** Decorating a class that is final, where subclassing cannot go. */
public final class VendorFeedMain {

    private static final Duration TTL = Duration.ofSeconds(60);

    private VendorFeedMain() {
    }

    public static void main(String[] args) {
        Console.section("decorating a class you are not allowed to subclass");
        VendorPriceFeed vendor = new VendorPriceFeed(); // final: `extends VendorPriceFeed` will not compile
        ManualClock clock = Clock.manual();
        CallLog log = new CallLog();

        PriceFeed decorated = new CachingPriceFeed(new LoggingPriceFeed(vendor, log, "vendor"), clock, TTL);
        decorated.quoteFor("SKU-999");
        decorated.quoteFor("SKU-999");

        System.out.println("  vendor calls: " + vendor.callCount() + " for 2 requests, with logging and caching added");
        System.out.println("  subclassing was never an option here — decoration did not need one");
    }
}
