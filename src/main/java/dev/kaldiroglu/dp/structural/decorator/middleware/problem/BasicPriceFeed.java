package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

/**
 * Naive design 3, part 1: a base class for "our" price feed, to be extended once per
 * concern.
 * <p>
 * The idea is sound in outline — give each concern its own class — and it is only one
 * step away from the Decorator pattern. The step it misses is the one that matters:
 * these classes are joined by <em>inheritance</em>, fixed at compile time, instead of by
 * <em>a reference</em>, chosen at run time.
 */
public class BasicPriceFeed implements PriceFeed {

    private final PriceFeed supplier;

    public BasicPriceFeed(PriceFeed supplier) {
        this.supplier = supplier;
    }

    @Override
    public Quote quoteFor(String sku) {
        return supplier.quoteFor(sku);
    }
}
