package dev.kaldiroglu.dp.structural.decorator.middleware;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Metrics;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeedException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.VendorPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.CachingRetryingLoggingPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.CopyPasteOrderService;
import dev.kaldiroglu.dp.structural.decorator.middleware.problem.FlaggedPriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.*;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.fluent.PriceFeedPipeline;
import dev.kaldiroglu.dp.structural.decorator.middleware.solution.functional.PriceFeedMiddleware;

import java.time.Duration;

/**
 * Runs every design in this project against the same scenario, printing the number that
 * matters each time: how many times the supplier was actually called.
 */
public final class Demo {

    private static final String SKU = "SKU-200";
    private static final Duration TTL = Duration.ofSeconds(60);

    private Demo() {
    }

    public static void main(String[] args) {
        heading("THE PROBLEM");
        copyPaste();
        flags();
        subclasses();

        heading("THE SOLUTION — classic decorators");
        classic();
        ordering();
        rateLimitPlacement();
        vendorFeed();

        heading("THE SOLUTION — variation: functional");
        functional();

        heading("THE SOLUTION — variation: fluent assembly");
        fluent();
    }

    // ---------------------------------------------------------------- problem

    private static void copyPaste() {
        section("1. cross-cutting code copied to each call site");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();
        CopyPasteOrderService service = new CopyPasteOrderService(supplier, clock, log);

        supplier.failNext(1);
        service.priceForOrder(SKU);
        int afterOrder = log.size();

        supplier.failNext(1);
        service.priceForReorder("SKU-100");

        System.out.println("  priceForOrder logged   " + afterOrder + " lines (retry included)");
        System.out.println("  priceForReorder logged " + (log.size() - afterOrder)
                + " lines — the failure was never logged, because that line was not copied");
        System.out.println("  the two methods now differ in retry count, logging, and whether");
        System.out.println("  their cache works at all — the reorder copy never writes a timestamp");
    }

    private static void flags() {
        section("2. one class, five boolean flags");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        PriceFeed feed = FlaggedPriceFeed.fullyEnabled(supplier, clock, new CallLog(), new Metrics());

        feed.quoteFor(SKU);
        feed.quoteFor(SKU);
        System.out.println("  works: " + supplier.callCount() + " supplier call for 2 requests");
        System.out.println("  but: 5 booleans = 32 configurations, and the cache/retry order");
        System.out.println("  is welded into one method that every concern has to share");
    }

    private static void subclasses() {
        section("3. a subclass per combination");
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

    // ---------------------------------------------------------------- solution

    private static void classic() {
        section("the same behavior, assembled from five independent decorators");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);
        CallLog log = new CallLog();
        Metrics metrics = new Metrics();

        PriceFeed feed =
                new TimingPriceFeed(
                        new LoggingPriceFeed(
                                new CachingPriceFeed(
                                        new RetryingPriceFeed(supplier, 3),
                                        clock, TTL),
                                log, "orders"),
                        clock, metrics);

        supplier.failNext(1);
        feed.quoteFor(SKU);
        feed.quoteFor(SKU);

        System.out.println("  supplier calls : " + supplier.callCount() + "  (1 retried failure, then a cache hit)");
        System.out.println("  log lines      : " + log.size());
        System.out.println("  timed calls    : " + metrics.size() + ", slowest " + metrics.slowest().toMillis() + " ms");
        System.out.println("  classes needed : 5 decorators + 1 base, for every combination and order");
    }

    private static void ordering() {
        section("order matters: logging inside or outside the retry");

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

        section("order matters: timing inside or outside the cache");

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

        section("and sometimes the order does not matter — check, do not assume");

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

    private static void rateLimitPlacement() {
        section("order matters again: does a cache hit spend quota?");
        ManualClock clock = Clock.manual();
        SimulatedRemotePriceFeed supplier = SimulatedRemotePriceFeed.withDefaults(clock);

        PriceFeed quotaSpentOnHits = new RateLimitingPriceFeed(new CachingPriceFeed(supplier, clock, TTL), clock, 10, Duration.ofHours(1));
        var limiterOutside = (dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic.RateLimitingPriceFeed) quotaSpentOnHits;
        limiterOutside.quoteFor(SKU);
        limiterOutside.quoteFor(SKU);
        limiterOutside.quoteFor(SKU);

        ManualClock clock2 = Clock.manual();
        SimulatedRemotePriceFeed supplier2 = SimulatedRemotePriceFeed.withDefaults(clock2);
        var limiterInside = new
                RateLimitingPriceFeed(supplier2, clock2, 10, Duration.ofHours(1));
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

    private static void vendorFeed() {
        section("decorating a class you are not allowed to subclass");
        VendorPriceFeed vendor = new VendorPriceFeed(); // final: `extends VendorPriceFeed` will not compile
        ManualClock clock = Clock.manual();
        CallLog log = new CallLog();

        PriceFeed decorated = new CachingPriceFeed(new LoggingPriceFeed(vendor, log, "vendor"), clock, TTL);
        decorated.quoteFor("SKU-999");
        decorated.quoteFor("SKU-999");

        System.out.println("  vendor calls: " + vendor.callCount() + " for 2 requests, with logging and caching added");
        System.out.println("  subclassing was never an option here — decoration did not need one");
    }

    private static void functional() {
        section("decorators as functions: no class per concern");
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

    private static void fluent() {
        section("the same chain, read in the order a request travels");
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

    // ---------------------------------------------------------------- output

    private static void heading(String title) {
        System.out.println("\n" + "=".repeat(72));
        System.out.println(title);
        System.out.println("=".repeat(72));
    }

    private static void section(String title) {
        System.out.println("\n--- " + title + " " + "-".repeat(Math.max(0, 68 - title.length())));
    }
}
