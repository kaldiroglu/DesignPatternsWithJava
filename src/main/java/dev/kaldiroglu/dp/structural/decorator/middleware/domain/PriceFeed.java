package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

/**
 * What the order system needs from a supplier: the price of one item.
 * <p>
 * The whole example turns on how small this interface is. One method, one argument, one
 * return value — and every design below, naive or not, is an attempt to add logging,
 * retrying, caching, timing and rate limiting <em>around</em> it.
 * <p>
 * It is a functional interface on purpose. That costs nothing here, and it makes the
 * {@code solution.functional} package possible: where an interface has a single method, a
 * decorator can be a lambda.
 */
@FunctionalInterface
public interface PriceFeed {

    /**
     * @param sku the item to price
     * @return the supplier's current quote
     * @throws FeedUnavailableException if the supplier could not be reached (worth retrying)
     * @throws UnknownSkuException      if the supplier does not sell this item (not worth retrying)
     */
    Quote quoteFor(String sku);
}
