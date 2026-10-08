package dev.kaldiroglu.dp.behavioral.observer.price.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>Subject</b>: a feed that tells its listeners about every change.
 * <p>
 * It knows its listeners only as {@link PriceListener}s — not the chart, the ticker or the
 * alert. Any object can subscribe or unsubscribe while the program runs. And because the
 * feed calls them at the moment the price changes, no change is ever missed and nobody is
 * late.
 * <p>
 * It notifies a copy of the list, so a listener that unsubscribes during a notification
 * does not break the loop.
 */
public final class PriceFeed {

    private final String symbol;
    private int price;
    private final List<PriceListener> listeners = new ArrayList<>();

    public PriceFeed(String symbol, int price) {
        this.symbol = symbol;
        this.price = price;
    }

    public void subscribe(PriceListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(PriceListener listener) {
        listeners.remove(listener);
    }

    public void setPrice(int newPrice) {
        if (newPrice == price) {
            return;                     // no change, no notification
        }
        PriceChange change = new PriceChange(symbol, price, newPrice);
        price = newPrice;
        for (PriceListener listener : List.copyOf(listeners)) {
            listener.priceChanged(change);
        }
    }

    public int price() {
        return price;
    }
}
