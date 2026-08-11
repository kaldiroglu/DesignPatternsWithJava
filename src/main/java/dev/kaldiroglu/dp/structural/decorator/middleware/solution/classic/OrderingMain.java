package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

import java.time.Duration;

/**
 * Three orderings, measured. Two change the observable behavior and one does not — which
 * is the whole argument for reasoning about each pair rather than reciting a rule.
 */
public final class OrderingMain {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private OrderingMain() {
    }

    public static void main(String[] args) {
        Console.section("order matters: logging inside or outside the retry");

        ManualClock clockA = Clock.manual();
        SimulatedRemotePriceFeed supplierA = SimulatedRemotePriceFeed.withDefaults(clockA);
        supplierA.failNext(1);
        CallLog outsideLog = new CallLog();
        PriceFeed logOutside = new LoggingPriceFeed(new RetryingPriceFeed(supplierA, 3), outsideLog, "orders");
        logOutside.quoteFor(SKU);

        ManualClock clockB = Clock.manual();
        SimulatedRemotePriceFeed supplierB = SimulatedRemotePriceFeed.withDefaults(clockB);
        supplierB.failNext(1);
        CallLog insideLog = new CallLog();
        PriceFeed logInside = new RetryingPriceFeed(new LoggingPriceFeed(supplierB, insideLog, "orders"), 3);
        logInside.quoteFor(SKU);

        System.out.println("  Logging(Retrying(feed)) : " + outsideLog.size()
                + " log lines — one request, one story; the failure is invisible");
        System.out.println("  Retrying(Logging(feed)) : " + insideLog.size()
                + " log lines — every attempt is logged, including the one that failed");
        System.out.println("  Both made " + supplierA.callCount() + " supplier calls. Same classes,");
        System.out.println("  same settings, different observable behavior — decided by parentheses.");

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
