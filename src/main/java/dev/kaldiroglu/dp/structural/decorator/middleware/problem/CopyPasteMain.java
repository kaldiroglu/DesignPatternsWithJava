package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.Console;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Clock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

/** Naive design 1: the cross-cutting code copied to each call site. */
public final class CopyPasteMain {

    private static final String SKU = "SKU-200";

    private CopyPasteMain() {
    }

    public static void main(String[] args) {
        Console.section("1. cross-cutting code copied to each call site");
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
}
