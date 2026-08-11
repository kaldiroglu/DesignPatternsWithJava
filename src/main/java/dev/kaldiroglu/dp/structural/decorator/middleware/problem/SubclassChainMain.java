package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

import java.time.Duration;

/** Naive design 3: a subclass per combination. */
public final class SubclassChainMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private SubclassChainMain() {
    }

    public static void main(String[] args) {
        Console.section("3. a subclass per combination");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();
        PriceFeed feed = new CachingRetryingLoggingPriceFeed(supplier, 3, log, clock, TTL);

        supplier.failNext(1);
        feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        System.out.println("  works: " + supplier.callCount() + " supplier calls (1 failure retried, then cached)");
        System.out.println("  but: the class name has to list its contents AND their order,");
        System.out.println("  and covering 5 concerns in every order would take 325 classes");
    }
}
