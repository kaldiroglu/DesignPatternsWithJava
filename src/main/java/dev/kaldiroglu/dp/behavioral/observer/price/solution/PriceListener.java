package dev.kaldiroglu.dp.behavioral.observer.price.solution;

/**
 * The <b>Observer</b>: anything that wants to hear about price changes.
 * <p>
 * One method, so a lambda can be a listener too.
 */
@FunctionalInterface
public interface PriceListener {

    void priceChanged(PriceChange change);
}
