package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.SimulatedRemotePriceFeed;

import java.time.Duration;

/**
 * Naive design 3, part 1: a base class for "our" price feed, to be extended once per
 * concern.
 * <p>
 * The idea is sound in outline — give each concern its own class — and it is only one
 * step away from the Decorator solution. The step it misses is the one that matters:
 * these classes are joined by <em>inheritance</em>, fixed at compile time, instead of by
 * <em>a reference</em>, chosen at run time. There is no field here: {@code super.quoteFor}
 * is the supplier, because the supplier is the top of the hierarchy. So a class that is
 * {@code final}, such as {@code VendorPriceFeed}, cannot take its place.
 */
public class BasicPriceFeed extends SimulatedRemotePriceFeed {

    public BasicPriceFeed(ManualClock clock) {
        super(clock, Duration.ofMillis(200));
    }
}
