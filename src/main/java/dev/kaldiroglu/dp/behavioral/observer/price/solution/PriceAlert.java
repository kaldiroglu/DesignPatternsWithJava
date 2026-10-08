package dev.kaldiroglu.dp.behavioral.observer.price.solution;

/** A <b>ConcreteObserver</b>: fires once when the price reaches its limit. */
public final class PriceAlert implements PriceListener {

    private final int limit;
    private boolean fired;

    public PriceAlert(int limit) {
        this.limit = limit;
    }

    @Override
    public void priceChanged(PriceChange change) {
        if (change.newPrice() >= limit) {
            fired = true;
        }
    }

    public boolean fired() {
        return fired;
    }
}
