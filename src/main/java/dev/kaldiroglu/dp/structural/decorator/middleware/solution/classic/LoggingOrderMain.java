package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.*;

/** Logging outside the retry tells one story; inside, it records every attempt. */
public final class LoggingOrderMain {

    private static final String SKU = "SKU-200";

    private LoggingOrderMain() {
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
    }
}
