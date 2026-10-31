package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.*;

import java.time.Duration;

/** The pair whose order changes nothing, which is why each pair needs its own reasoning. */
public final class CacheRetryOrderMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private CacheRetryOrderMain() {
    }

    public static void main(String[] args) {
        Console.section("and sometimes the order does not matter — check, do not assume");

        ManualClock clockE = Clock.manual();
        SimulatedRemotePriceFeed supplierE = SimulatedRemotePriceFeed.withDefaults(clockE);
        supplierE.failNext(1);
        PriceFeed cacheOutside = new CachingPriceFeed(new RetryingPriceFeed(supplierE, 3), clockE, TTL);
        cacheOutside.quoteFor(SKU);
        cacheOutside.quoteFor(SKU);

        ManualClock clockF = Clock.manual();
        SimulatedRemotePriceFeed supplierF = SimulatedRemotePriceFeed.withDefaults(clockF);
        supplierF.failNext(1);
        PriceFeed retryOutside = new RetryingPriceFeed(new CachingPriceFeed(supplierF, clockF, TTL), 3);
        retryOutside.quoteFor(SKU);
        retryOutside.quoteFor(SKU);

        System.out.println("  Caching(Retrying(feed)) : " + supplierE.callCount() + " supplier calls");
        System.out.println("  Retrying(Caching(feed)) : " + supplierF.callCount() + " supplier calls");
        System.out.println("  Identical — because failures are not cached, so a retry that passes");
        System.out.println("  back through the cache finds nothing there and goes on to the supplier.");
        System.out.println("  Ordering is something you reason about per pair, not a blanket rule.");
    }
}
